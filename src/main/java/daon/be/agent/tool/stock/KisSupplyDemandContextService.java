package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04160001InvestorTradeByStockDailyRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04160001InvestorTradeByStockDailyResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04400000ForeignInstitutionTotalRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04400000ForeignInstitutionTotalResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisHhptj04160200InvestorTrendEstimateRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisHhptj04160200InvestorTrendEstimateResponse;
import daon.be.agent.tool.stock.dto.SupplyDemandContextDto;
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
public class KisSupplyDemandContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_SUPPLY_DEMAND_CONTEXT";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String INDEX_MARKET_CODE = "U";
    private static final String DEFAULT_MARKET_INDEX_CODE = "0001";
    private static final String ALL_MARKET_CODE = "0000";

    private final KisQuotationApiClient quotationApiClient;

    public SupplyDemandContextDto getSupplyDemandContext(String stockCode, String startDate, String marketIndexCode) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedStartDate = normalizeDate(startDate);
        String normalizedMarketIndexCode = defaultIfBlank(marketIndexCode, DEFAULT_MARKET_INDEX_CODE);
        OffsetDateTime requestedAt = now();
        List<SupplyDemandContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime dailyStockCalledAt = now();
        KisFhptj04160001InvestorTradeByStockDailyResponse dailyStockResponse = quotationApiClient.inquireInvestorTradeByStockDaily(
                new KisFhptj04160001InvestorTradeByStockDailyRequest(STOCK_MARKET_CODE, normalizedStockCode, normalizedStartDate, "0", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHPTJ04160001_INVESTOR_TRADE_BY_STOCK_DAILY, dailyStockCalledAt, dailyStockResponse.rtCd(), dailyStockResponse.msg1()));

        OffsetDateTime estimateCalledAt = now();
        KisHhptj04160200InvestorTrendEstimateResponse estimateResponse = quotationApiClient.inquireInvestorTrendEstimate(
                new KisHhptj04160200InvestorTrendEstimateRequest(normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.HHPTJ04160200_INVESTOR_TREND_ESTIMATE, estimateCalledAt, estimateResponse.rtCd(), estimateResponse.msg1()));

        OffsetDateTime rankCalledAt = now();
        KisFhptj04400000ForeignInstitutionTotalResponse rankResponse = quotationApiClient.inquireForeignInstitutionTotal(
                new KisFhptj04400000ForeignInstitutionTotalRequest(STOCK_MARKET_CODE, "16449", ALL_MARKET_CODE, "0", "0", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHPTJ04400000_FOREIGN_INSTITUTION_TOTAL, rankCalledAt, rankResponse.rtCd(), rankResponse.msg1()));

        OffsetDateTime marketFlowCalledAt = now();
        KisFhptj04040000InvestorDailyByMarketResponse marketFlowResponse = quotationApiClient.inquireInvestorDailyByMarket(
                new KisFhptj04040000InvestorDailyByMarketRequest(
                        INDEX_MARKET_CODE,
                        normalizedMarketIndexCode,
                        normalizedStartDate,
                        investorMarketCode(normalizedMarketIndexCode),
                        normalizedStartDate,
                        normalizedMarketIndexCode
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPTJ04040000_INVESTOR_DAILY_BY_MARKET, marketFlowCalledAt, marketFlowResponse.rtCd(), marketFlowResponse.msg1()));

        validateSuccess(KisEndpoint.FHPTJ04160001_INVESTOR_TRADE_BY_STOCK_DAILY, dailyStockResponse.rtCd(), dailyStockResponse.msg1());
        validateSuccess(KisEndpoint.HHPTJ04160200_INVESTOR_TREND_ESTIMATE, estimateResponse.rtCd(), estimateResponse.msg1());
        validateSuccess(KisEndpoint.FHPTJ04400000_FOREIGN_INSTITUTION_TOTAL, rankResponse.rtCd(), rankResponse.msg1());
        validateSuccess(KisEndpoint.FHPTJ04040000_INVESTOR_DAILY_BY_MARKET, marketFlowResponse.rtCd(), marketFlowResponse.msg1());

        List<String> cautions = List.of(
                "종목별 외인기관 추정가집계는 확정 수급이 아니라 장중 가집계입니다.",
                "종목별 투자자매매동향(일별)은 당일 15:40 이후 조회 제한과 산출 시간 변동이 있을 수 있습니다."
        );

        return new SupplyDemandContextDto(
                new SupplyDemandContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        SupplyDemandContextDto.DataFreshness.MIXED_INTRADAY_AND_DAILY,
                        cautions
                ),
                identity(normalizedStockCode, dailyStockResponse.output1(), rankResponse.output()),
                intradayEstimate(estimateResponse.output2()),
                dailyInvestorFlows(dailyStockResponse.output2()),
                marketInstitutionForeignRanks(normalizedStockCode, rankResponse.output()),
                marketFlow(marketFlowResponse.output()),
                cautions
        );
    }

    private SupplyDemandContextDto.StockIdentityDto identity(
            String stockCode,
            KisFhptj04160001InvestorTradeByStockDailyResponse.Output1 stockSummary,
            KisFhptj04400000ForeignInstitutionTotalResponse.Output rank
    ) {
        return new SupplyDemandContextDto.StockIdentityDto(
                firstNonBlank(rank == null ? null : rank.mkscShrnIscd(), stockCode),
                rank == null ? null : rank.htsKorIsnm(),
                stockSummary == null ? null : stockSummary.rprsMrktKorName()
        );
    }

    private SupplyDemandContextDto.IntradayEstimatedInvestorFlowDto intradayEstimate(
            List<KisHhptj04160200InvestorTrendEstimateResponse.Output2> outputs
    ) {
        if (outputs == null || outputs.isEmpty()) {
            return null;
        }
        KisHhptj04160200InvestorTrendEstimateResponse.Output2 output = outputs.getFirst();
        return new SupplyDemandContextDto.IntradayEstimatedInvestorFlowDto(
                output.bsopHourGb(),
                number(output.frgnFakeNtbyQty()),
                number(output.orgnFakeNtbyQty()),
                number(output.sumFakeNtbyQty()),
                true
        );
    }

    private List<SupplyDemandContextDto.DailyInvestorFlowDto> dailyInvestorFlows(
            List<KisFhptj04160001InvestorTradeByStockDailyResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new SupplyDemandContextDto.DailyInvestorFlowDto(
                        output.stckBsopDate(),
                        decimal(output.stckClpr()),
                        number(output.frgnNtbyQty()),
                        decimal(output.frgnNtbyTrPbmn()),
                        number(output.prsnNtbyQty()),
                        decimal(output.prsnNtbyTrPbmn()),
                        number(output.orgnNtbyQty()),
                        decimal(output.orgnNtbyTrPbmn())
                ))
                .toList();
    }

    private List<SupplyDemandContextDto.InstitutionForeignRankDto> marketInstitutionForeignRanks(
            String stockCode,
            KisFhptj04400000ForeignInstitutionTotalResponse.Output output
    ) {
        if (output == null || !stockCode.equals(output.mkscShrnIscd())) {
            return List.of();
        }
        return List.of(new SupplyDemandContextDto.InstitutionForeignRankDto(
                output.mkscShrnIscd(),
                output.htsKorIsnm(),
                number(output.ntbyQty()),
                decimal(output.stckPrpr()),
                number(output.frgnNtbyQty()),
                decimal(output.frgnNtbyTrPbmn()),
                number(output.orgnNtbyQty()),
                decimal(output.orgnNtbyTrPbmn())
        ));
    }

    private SupplyDemandContextDto.MarketInvestorFlowDto marketFlow(
            List<KisFhptj04040000InvestorDailyByMarketResponse.Output> outputs
    ) {
        if (outputs == null || outputs.isEmpty()) {
            return null;
        }
        KisFhptj04040000InvestorDailyByMarketResponse.Output output = outputs.getFirst();
        return new SupplyDemandContextDto.MarketInvestorFlowDto(
                output.stckBsopDate(),
                number(output.frgnNtbyQty()),
                decimal(output.frgnNtbyTrPbmn()),
                number(output.prsnNtbyQty()),
                decimal(output.prsnNtbyTrPbmn()),
                number(output.orgnNtbyQty()),
                decimal(output.orgnNtbyTrPbmn())
        );
    }

    private SupplyDemandContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new SupplyDemandContextDto.KisApiCallSummary(
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

    private String investorMarketCode(String marketIndexCode) {
        return "1001".equals(marketIndexCode) ? "KSQ" : "KSP";
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

    private String normalizeRequired(String value, String name) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + "는 필수입니다.");
        }
        return value.trim();
    }

    private String normalizeDate(String value) {
        if (isBlank(value)) {
            return LocalDate.now(SEOUL).minusDays(10).format(DATE_FORMAT);
        }
        return value.trim().replace("-", "");
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
