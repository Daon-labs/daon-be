package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600001CompProgramTradeDailyRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600001CompProgramTradeDailyResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600101CompProgramTradeTodayRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600101CompProgramTradeTodayResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650101ProgramTradeByStockRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650101ProgramTradeByStockResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650201ProgramTradeByStockDailyRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650201ProgramTradeByStockDailyResponse;
import daon.be.agent.data.source.kis.quotation.program.KisHhppg046600C1InvestorProgramTradeTodayRequest;
import daon.be.agent.data.source.kis.quotation.program.KisHhppg046600C1InvestorProgramTradeTodayResponse;
import daon.be.agent.tool.stock.dto.ProgramTradingContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisProgramTradingContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_PROGRAM_TRADING_CONTEXT";
    private static final String DEFAULT_MARKET = "J";
    private static final String DEFAULT_MARKET_CLASS = "K";
    private static final String DEFAULT_INPUT_HOUR = "090000";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";
    private static final String DEFAULT_INVESTOR_MARKET_CLASS = "1";

    private final KisQuotationApiClient quotationApiClient;

    public ProgramTradingContextDto getProgramTradingContext(
            String stockCode,
            String startDate,
            String endDate,
            String market,
            String inputHour
    ) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        String normalizedStartDate = normalizeDate(startDate, LocalDate.now(SEOUL).minusDays(10).format(DATE_FORMAT));
        String normalizedEndDate = normalizeDate(endDate, LocalDate.now(SEOUL).format(DATE_FORMAT));
        String normalizedInputHour = defaultIfBlank(inputHour, DEFAULT_INPUT_HOUR);
        OffsetDateTime requestedAt = now();
        List<ProgramTradingContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime stockIntradayCalledAt = now();
        KisFhppg04650101ProgramTradeByStockResponse stockIntradayResponse = quotationApiClient.inquireProgramTradeByStock(
                new KisFhppg04650101ProgramTradeByStockRequest(normalizedMarket, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPPG04650101_PROGRAM_TRADE_BY_STOCK, stockIntradayCalledAt, stockIntradayResponse.rtCd(), stockIntradayResponse.msg1()));

        OffsetDateTime stockDailyCalledAt = now();
        KisFhppg04650201ProgramTradeByStockDailyResponse stockDailyResponse = quotationApiClient.inquireProgramTradeByStockDaily(
                new KisFhppg04650201ProgramTradeByStockDailyRequest(normalizedMarket, normalizedStockCode, normalizedStartDate)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPPG04650201_PROGRAM_TRADE_BY_STOCK_DAILY, stockDailyCalledAt, stockDailyResponse.rtCd(), stockDailyResponse.msg1()));

        OffsetDateTime marketIntradayCalledAt = now();
        KisFhppg04600101CompProgramTradeTodayResponse marketIntradayResponse = quotationApiClient.inquireCompProgramTradeToday(
                new KisFhppg04600101CompProgramTradeTodayRequest(
                        normalizedMarket,
                        DEFAULT_MARKET_CLASS,
                        ZERO,
                        ALL_MARKET_CODE,
                        normalizedMarket,
                        normalizedInputHour
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPPG04600101_COMP_PROGRAM_TRADE_TODAY, marketIntradayCalledAt, marketIntradayResponse.rtCd(), marketIntradayResponse.msg1()));

        OffsetDateTime marketDailyCalledAt = now();
        KisFhppg04600001CompProgramTradeDailyResponse marketDailyResponse = quotationApiClient.inquireCompProgramTradeDaily(
                new KisFhppg04600001CompProgramTradeDailyRequest(normalizedMarket, DEFAULT_MARKET_CLASS, normalizedStartDate, normalizedEndDate)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPPG04600001_COMP_PROGRAM_TRADE_DAILY, marketDailyCalledAt, marketDailyResponse.rtCd(), marketDailyResponse.msg1()));

        OffsetDateTime investorCalledAt = now();
        KisHhppg046600C1InvestorProgramTradeTodayResponse investorResponse = quotationApiClient.inquireInvestorProgramTradeToday(
                new KisHhppg046600C1InvestorProgramTradeTodayRequest(normalizedMarket, DEFAULT_INVESTOR_MARKET_CLASS)
        );
        apiCalls.add(apiCall(KisEndpoint.HHPPG046600_C1_INVESTOR_PROGRAM_TRADE_TODAY, investorCalledAt, investorResponse.rtCd(), investorResponse.msg1()));

        validateSuccess(KisEndpoint.FHPPG04650101_PROGRAM_TRADE_BY_STOCK, stockIntradayResponse.rtCd(), stockIntradayResponse.msg1());
        validateSuccess(KisEndpoint.FHPPG04650201_PROGRAM_TRADE_BY_STOCK_DAILY, stockDailyResponse.rtCd(), stockDailyResponse.msg1());
        validateSuccess(KisEndpoint.FHPPG04600101_COMP_PROGRAM_TRADE_TODAY, marketIntradayResponse.rtCd(), marketIntradayResponse.msg1());
        validateSuccess(KisEndpoint.FHPPG04600001_COMP_PROGRAM_TRADE_DAILY, marketDailyResponse.rtCd(), marketDailyResponse.msg1());
        validateSuccess(KisEndpoint.HHPPG046600_C1_INVESTOR_PROGRAM_TRADE_TODAY, investorResponse.rtCd(), investorResponse.msg1());

        List<String> interpretationHints = List.of(
                "프로그램매매 데이터는 수급 원인 후보이며 가격 변동의 단독 원인으로 단정하지 않습니다.",
                "시장 전체 프로그램매매 시간 API는 최근 30분 중심의 장중 스냅샷입니다."
        );

        return new ProgramTradingContextDto(
                new ProgramTradingContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        ProgramTradingContextDto.DataFreshness.MIXED_INTRADAY_AND_DAILY,
                        interpretationHints
                ),
                new ProgramTradingContextDto.StockIdentityDto(normalizedStockCode, normalizedMarket),
                stockIntradayProgramTrades(stockIntradayResponse.output()),
                stockDailyProgramTrades(stockDailyResponse.output()),
                marketIntradayProgramTrades(marketIntradayResponse.output1()),
                marketDailyProgramTrades(marketDailyResponse.output()),
                new ProgramTradingContextDto.ProgramTradeInvestorBreakdownDto(investorProgramTrades(investorResponse.output1())),
                interpretationHints
        );
    }

    private List<ProgramTradingContextDto.StockProgramTradePointDto> stockIntradayProgramTrades(
            List<KisFhppg04650101ProgramTradeByStockResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ProgramTradingContextDto.StockProgramTradePointDto(
                        output.bsopHour(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        number(output.wholSmtnSelnVol()),
                        number(output.wholSmtnShnuVol()),
                        number(output.wholSmtnNtbyQty()),
                        decimal(output.wholSmtnSelnTrPbmn()),
                        decimal(output.wholSmtnShnuTrPbmn()),
                        decimal(output.wholSmtnNtbyTrPbmn())
                ))
                .toList();
    }

    private List<ProgramTradingContextDto.StockProgramTradeDailyDto> stockDailyProgramTrades(
            List<KisFhppg04650201ProgramTradeByStockDailyResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ProgramTradingContextDto.StockProgramTradeDailyDto(
                        output.stckBsopDate(),
                        decimal(output.stckClpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        decimal(output.acmlTrPbmn()),
                        number(output.wholSmtnSelnVol()),
                        number(output.wholSmtnShnuVol()),
                        number(output.wholSmtnNtbyQty()),
                        decimal(output.wholSmtnSelnTrPbmn()),
                        decimal(output.wholSmtnShnuTrPbmn()),
                        decimal(output.wholSmtnNtbyTrPbmn())
                ))
                .toList();
    }

    private List<ProgramTradingContextDto.MarketProgramTradePointDto> marketIntradayProgramTrades(
            List<KisFhppg04600101CompProgramTradeTodayResponse.Output1> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ProgramTradingContextDto.MarketProgramTradePointDto(
                        output.bsopHour(),
                        decimal(output.arbtSmtnNtbyTrPbmn()),
                        decimal(output.nabtSmtnNtbyTrPbmn()),
                        decimal(output.wholSmtnNtbyTrPbmn()),
                        decimal(output.bstpNmixPrpr()),
                        decimal(output.bstpNmixPrdyVrss()),
                        output.prdyVrssSign()
                ))
                .toList();
    }

    private List<ProgramTradingContextDto.MarketProgramTradeDailyDto> marketDailyProgramTrades(
            List<KisFhppg04600001CompProgramTradeDailyResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> {
                    Long arbitrageNetBuyQuantity = number(output.arbtSmtnNtbyQty());
                    Long nonArbitrageNetBuyQuantity = number(output.nabtSmtnNtbyQty());
                    BigDecimal arbitrageNetBuyAmount = decimal(output.arbtSmtnNtbyTrPbmn());
                    BigDecimal nonArbitrageNetBuyAmount = decimal(output.nabtSmtnNtbyTrPbmn());
                    return new ProgramTradingContextDto.MarketProgramTradeDailyDto(
                            output.stckBsopDate(),
                            arbitrageNetBuyQuantity,
                            nonArbitrageNetBuyQuantity,
                            sum(arbitrageNetBuyQuantity, nonArbitrageNetBuyQuantity),
                            arbitrageNetBuyAmount,
                            nonArbitrageNetBuyAmount,
                            sum(arbitrageNetBuyAmount, nonArbitrageNetBuyAmount)
                    );
                })
                .toList();
    }

    private List<ProgramTradingContextDto.InvestorProgramTradeDto> investorProgramTrades(
            List<KisHhppg046600C1InvestorProgramTradeTodayResponse.Output1> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ProgramTradingContextDto.InvestorProgramTradeDto(
                        output.invrClsCode(),
                        output.invrClsName(),
                        number(output.allSelnQty()),
                        number(output.allShnuQty()),
                        number(output.allNtbyQty()),
                        decimal(output.allSelnAmt()),
                        decimal(output.allShnuAmt()),
                        decimal(output.allNtbyAmt()),
                        decimal(output.arbtNtbyAmt()),
                        decimal(output.nabtNtbyAmt())
                ))
                .toList();
    }

    private ProgramTradingContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new ProgramTradingContextDto.KisApiCallSummary(
                endpoint.apiKoreanName(),
                endpoint.trId(),
                endpoint.url(),
                calledAt,
                success,
                success ? null : msg1
        );
    }

    private void validateSuccess(KisEndpoint endpoint, String rtCd, String msg1) {
        if (!KisResponseValidator.isSuccess(rtCd)) {
            throw new IllegalStateException(endpoint.apiKoreanName() + " KIS 응답 실패: " + msg1);
        }
    }

    private BigDecimal decimal(String value) {
        if (isBlank(value)) {
            return null;
        }
        return new BigDecimal(value.trim().replace(",", ""));
    }

    private Long number(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Long.parseLong(value.trim().replace(",", ""));
    }

    private BigDecimal sum(BigDecimal first, BigDecimal second) {
        if (first == null || second == null) {
            return null;
        }
        return first.add(second);
    }

    private Long sum(Long first, Long second) {
        if (first == null || second == null) {
            return null;
        }
        return first + second;
    }

    private String normalizeRequired(String value, String name) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + "는 필수입니다.");
        }
        return value.trim();
    }

    private String normalizeDate(String value, String defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        return value.trim().replace("-", "");
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
