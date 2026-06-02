package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.index.KisFhkup03500200IndexMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhkup03500200IndexMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02100000IndexPriceRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02100000IndexPriceResponse;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02120000IndexDailyPriceRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02120000IndexDailyPriceResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04030000InvestorTimeByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04030000InvestorTimeByMarketResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketResponse;
import daon.be.agent.tool.stock.dto.MarketIndustryContextDto;
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
public class KisMarketIndustryContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_MARKET_INDUSTRY_CONTEXT";
    private static final String DEFAULT_MARKET_CODE = "U";
    private static final String DEFAULT_INDUSTRY_CODE = "0001";
    private static final String DEFAULT_INPUT_HOUR = "153000";
    private static final String ALL_MARKET_CODE = "0000";

    private final KisQuotationApiClient quotationApiClient;

    public MarketIndustryContextDto getMarketIndustryContext(
            String marketCode,
            String industryCode,
            String inputHour,
            String startDate
    ) {
        String normalizedMarketCode = defaultIfBlank(marketCode, DEFAULT_MARKET_CODE);
        String normalizedIndustryCode = defaultIfBlank(industryCode, DEFAULT_INDUSTRY_CODE);
        String normalizedHour = normalizeHour(inputHour);
        String normalizedStartDate = normalizeStartDate(startDate);
        OffsetDateTime requestedAt = now();
        List<MarketIndustryContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime indexPriceCalledAt = now();
        KisFhpup02100000IndexPriceResponse indexPriceResponse = quotationApiClient.inquireIndexPrice(
                new KisFhpup02100000IndexPriceRequest(normalizedMarketCode, normalizedIndustryCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPUP02100000_INDEX_PRICE, indexPriceCalledAt, indexPriceResponse.rtCd(), indexPriceResponse.msg1()));

        OffsetDateTime minuteChartCalledAt = now();
        KisFhkup03500200IndexMinuteChartResponse minuteChartResponse = quotationApiClient.inquireIndexMinuteChart(
                new KisFhkup03500200IndexMinuteChartRequest(normalizedMarketCode, "", normalizedIndustryCode, normalizedHour, "N")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKUP03500200_INDEX_MINUTE_CHART, minuteChartCalledAt, minuteChartResponse.rtCd(), minuteChartResponse.msg1()));

        OffsetDateTime dailyPriceCalledAt = now();
        KisFhpup02120000IndexDailyPriceResponse dailyPriceResponse = quotationApiClient.inquireIndexDailyPrice(
                new KisFhpup02120000IndexDailyPriceRequest("D", normalizedMarketCode, normalizedIndustryCode, normalizedStartDate)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPUP02120000_INDEX_DAILY_PRICE, dailyPriceCalledAt, dailyPriceResponse.rtCd(), dailyPriceResponse.msg1()));

        OffsetDateTime intradayInvestorCalledAt = now();
        KisFhptj04030000InvestorTimeByMarketResponse intradayInvestorResponse = quotationApiClient.inquireInvestorTimeByMarket(
                new KisFhptj04030000InvestorTimeByMarketRequest(normalizedIndustryCode, ALL_MARKET_CODE)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPTJ04030000_INVESTOR_TIME_BY_MARKET, intradayInvestorCalledAt, intradayInvestorResponse.rtCd(), intradayInvestorResponse.msg1()));

        OffsetDateTime dailyInvestorCalledAt = now();
        KisFhptj04040000InvestorDailyByMarketResponse dailyInvestorResponse = quotationApiClient.inquireInvestorDailyByMarket(
                new KisFhptj04040000InvestorDailyByMarketRequest(
                        normalizedMarketCode,
                        normalizedIndustryCode,
                        normalizedStartDate,
                        investorMarketCode(normalizedIndustryCode),
                        normalizedStartDate,
                        normalizedIndustryCode
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPTJ04040000_INVESTOR_DAILY_BY_MARKET, dailyInvestorCalledAt, dailyInvestorResponse.rtCd(), dailyInvestorResponse.msg1()));

        validateSuccess(KisEndpoint.FHPUP02100000_INDEX_PRICE, indexPriceResponse.rtCd(), indexPriceResponse.msg1());
        validateSuccess(KisEndpoint.FHKUP03500200_INDEX_MINUTE_CHART, minuteChartResponse.rtCd(), minuteChartResponse.msg1());
        validateSuccess(KisEndpoint.FHPUP02120000_INDEX_DAILY_PRICE, dailyPriceResponse.rtCd(), dailyPriceResponse.msg1());
        validateSuccess(KisEndpoint.FHPTJ04030000_INVESTOR_TIME_BY_MARKET, intradayInvestorResponse.rtCd(), intradayInvestorResponse.msg1());
        validateSuccess(KisEndpoint.FHPTJ04040000_INVESTOR_DAILY_BY_MARKET, dailyInvestorResponse.rtCd(), dailyInvestorResponse.msg1());

        List<MarketIndustryContextDto.OhlcvDto> dailyBars = dailyBars(dailyPriceResponse.output2());
        List<String> hints = List.of(
                "시장/업종 수급은 시장별 투자자매매동향 기준이며 종목별 수급과 다를 수 있습니다.",
                "업종 코드는 입력값을 사용하며, 종목별 업종 resolver는 아직 결합하지 않았습니다."
        );

        return new MarketIndustryContextDto(
                new MarketIndustryContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        MarketIndustryContextDto.DataFreshness.INTRADAY_SNAPSHOT,
                        hints
                ),
                marketName(normalizedIndustryCode),
                industrySnapshot(normalizedIndustryCode, indexPriceResponse.output(), minuteChartResponse.output1()),
                minuteBars(minuteChartResponse.output2()),
                dailyBars,
                intradayInvestorFlow(intradayInvestorResponse.output()),
                dailyInvestorFlows(dailyInvestorResponse.output()),
                relativeStrength(indexPriceResponse.output(), dailyBars),
                hints
        );
    }

    private MarketIndustryContextDto.IndustryIndexSnapshotDto industrySnapshot(
            String industryCode,
            KisFhpup02100000IndexPriceResponse.Output price,
            List<KisFhkup03500200IndexMinuteChartResponse.Output1> minuteSummaries
    ) {
        String industryName = minuteSummaries == null || minuteSummaries.isEmpty() ? null : minuteSummaries.getFirst().htsKorIsnm();
        return new MarketIndustryContextDto.IndustryIndexSnapshotDto(
                industryCode,
                industryName,
                decimal(price.bstpNmixPrpr()),
                decimal(price.bstpNmixPrdyVrss()),
                price.prdyVrssSign(),
                decimal(price.bstpNmixPrdyCtrt()),
                number(price.acmlVol()),
                decimal(price.acmlTrPbmn()),
                decimal(price.bstpNmixOprc()),
                decimal(price.bstpNmixHgpr()),
                decimal(price.bstpNmixLwpr()),
                number(price.ascnIssuCnt()),
                number(price.downIssuCnt()),
                number(price.stnrIssuCnt())
        );
    }

    private List<MarketIndustryContextDto.OhlcvDto> minuteBars(List<KisFhkup03500200IndexMinuteChartResponse.Output2> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new MarketIndustryContextDto.OhlcvDto(
                        output.stckBsopDate(),
                        output.stckCntgHour(),
                        decimal(output.bstpNmixOprc()),
                        decimal(output.bstpNmixHgpr()),
                        decimal(output.bstpNmixLwpr()),
                        decimal(output.bstpNmixPrpr()),
                        number(output.cntgVol()),
                        decimal(output.acmlTrPbmn()),
                        null
                ))
                .toList();
    }

    private List<MarketIndustryContextDto.OhlcvDto> dailyBars(List<KisFhpup02120000IndexDailyPriceResponse.Output2> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new MarketIndustryContextDto.OhlcvDto(
                        output.stckBsopDate(),
                        null,
                        decimal(output.bstpNmixOprc()),
                        decimal(output.bstpNmixHgpr()),
                        decimal(output.bstpNmixLwpr()),
                        decimal(output.bstpNmixPrpr()),
                        number(output.acmlVol()),
                        decimal(output.acmlTrPbmn()),
                        decimal(output.bstpNmixPrdyCtrt())
                ))
                .toList();
    }

    private MarketIndustryContextDto.MarketInvestorFlowDto intradayInvestorFlow(
            List<KisFhptj04030000InvestorTimeByMarketResponse.Output> outputs
    ) {
        if (outputs == null || outputs.isEmpty()) {
            return null;
        }
        KisFhptj04030000InvestorTimeByMarketResponse.Output output = outputs.getFirst();
        return new MarketIndustryContextDto.MarketInvestorFlowDto(
                null,
                "INTRADAY",
                number(output.frgnNtbyQty()),
                decimal(output.frgnNtbyTrPbmn()),
                number(output.prsnNtbyQty()),
                decimal(output.prsnNtbyTrPbmn()),
                number(output.orgnNtbyQty()),
                decimal(output.orgnNtbyTrPbmn())
        );
    }

    private List<MarketIndustryContextDto.MarketInvestorFlowDto> dailyInvestorFlows(
            List<KisFhptj04040000InvestorDailyByMarketResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new MarketIndustryContextDto.MarketInvestorFlowDto(
                        output.stckBsopDate(),
                        "DAILY",
                        number(output.frgnNtbyQty()),
                        decimal(output.frgnNtbyTrPbmn()),
                        number(output.prsnNtbyQty()),
                        decimal(output.prsnNtbyTrPbmn()),
                        number(output.orgnNtbyQty()),
                        decimal(output.orgnNtbyTrPbmn())
                ))
                .toList();
    }

    private MarketIndustryContextDto.RelativeStrengthDto relativeStrength(
            KisFhpup02100000IndexPriceResponse.Output price,
            List<MarketIndustryContextDto.OhlcvDto> dailyBars
    ) {
        BigDecimal latestDailyChangeRate = dailyBars.isEmpty() ? null : dailyBars.getFirst().changeRate();
        return new MarketIndustryContextDto.RelativeStrengthDto(
                decimal(price.bstpNmixPrdyCtrt()),
                latestDailyChangeRate,
                "개별 종목 수익률이 없으므로 이 DTO는 시장/업종 자체 흐름만 제공합니다."
        );
    }

    private MarketIndustryContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new MarketIndustryContextDto.KisApiCallSummary(
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

    private String marketName(String industryCode) {
        return switch (industryCode) {
            case "0001" -> "KOSPI";
            case "1001" -> "KOSDAQ";
            default -> industryCode;
        };
    }

    private String investorMarketCode(String industryCode) {
        return "1001".equals(industryCode) ? "KSQ" : "KSP";
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

    private String normalizeHour(String value) {
        if (isBlank(value)) {
            return DEFAULT_INPUT_HOUR;
        }
        return value.trim().replace(":", "");
    }

    private String normalizeStartDate(String value) {
        if (isBlank(value)) {
            return LocalDate.now(SEOUL).minusDays(10).format(DATE_FORMAT);
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
