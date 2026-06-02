package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.finance.KisFinanceApiClient;
import daon.be.agent.data.source.kis.finance.expectation.KisFhkst663300C0InvestOpinionRequest;
import daon.be.agent.data.source.kis.finance.expectation.KisFhkst663300C0InvestOpinionResponse;
import daon.be.agent.data.source.kis.finance.expectation.KisHhkst668300C0EstimatePerformRequest;
import daon.be.agent.data.source.kis.finance.expectation.KisHhkst668300C0EstimatePerformResponse;
import daon.be.agent.tool.stock.dto.ExpectationContextDto;
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
public class KisExpectationContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_EXPECTATION_CONTEXT";
    private static final String DEFAULT_MARKET = "J";
    private static final String INVEST_OPINION_SCREEN_CODE = "16633";

    private final KisFinanceApiClient financeApiClient;

    public ExpectationContextDto getExpectationContext(String stockCode, String startDate, String endDate, String market) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        String normalizedStartDate = normalizeDate(startDate, LocalDate.now(SEOUL).minusYears(1).format(DATE_FORMAT));
        String normalizedEndDate = normalizeDate(endDate, LocalDate.now(SEOUL).format(DATE_FORMAT));
        OffsetDateTime requestedAt = now();
        List<ExpectationContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime opinionCalledAt = now();
        KisFhkst663300C0InvestOpinionResponse opinionResponse = financeApiClient.inquireInvestOpinion(
                new KisFhkst663300C0InvestOpinionRequest(
                        normalizedMarket,
                        INVEST_OPINION_SCREEN_CODE,
                        normalizedStockCode,
                        normalizedStartDate,
                        normalizedEndDate
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST663300_C0_INVEST_OPINION, opinionCalledAt, opinionResponse.rtCd(), opinionResponse.msg1()));

        OffsetDateTime estimateCalledAt = now();
        KisHhkst668300C0EstimatePerformResponse estimateResponse = financeApiClient.inquireEstimatePerform(
                new KisHhkst668300C0EstimatePerformRequest(normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.HHKST668300_C0_ESTIMATE_PERFORM, estimateCalledAt, estimateResponse.rtCd(), estimateResponse.msg1()));

        validateSuccess(KisEndpoint.FHKST663300_C0_INVEST_OPINION, opinionResponse.rtCd(), opinionResponse.msg1());
        validateSuccess(KisEndpoint.HHKST668300_C0_ESTIMATE_PERFORM, estimateResponse.rtCd(), estimateResponse.msg1());

        List<String> cautions = List.of(
                "투자의견과 추정실적은 가격 변동의 직접 원인으로 단정하지 않습니다.",
                "종목추정실적은 리서치본부 추정 대상 일부 종목으로 제한됩니다.",
                "종목추정실적은 당월 초 애널리스트 의견 기준이며 월중 변동 가능성이 있습니다.",
                "종목추정실적의 data1~data5는 output4 결산년월 순서에 맞춰 해석합니다."
        );
        ExpectationContextDto.EstimatedPerformanceDto estimatedPerformance = estimatedPerformance(normalizedStockCode, estimateResponse);

        return new ExpectationContextDto(
                new ExpectationContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        ExpectationContextDto.DataFreshness.DAILY_OR_MONTHLY_EXPECTATION,
                        cautions
                ),
                identity(normalizedStockCode, normalizedMarket, estimateResponse.output1()),
                investmentOpinions(opinionResponse.output()),
                estimatedPerformance,
                cautions
        );
    }

    private ExpectationContextDto.StockIdentityDto identity(
            String stockCode,
            String market,
            KisHhkst668300C0EstimatePerformResponse.Output1 estimateIdentity
    ) {
        return new ExpectationContextDto.StockIdentityDto(
                stripLeadingA(firstNonBlank(estimateIdentity == null ? null : estimateIdentity.shtCd(), stockCode)),
                estimateIdentity == null ? null : estimateIdentity.itemKorNm(),
                market
        );
    }

    private List<ExpectationContextDto.InvestmentOpinionDto> investmentOpinions(
            List<KisFhkst663300C0InvestOpinionResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ExpectationContextDto.InvestmentOpinionDto(
                        output.stckBsopDate(),
                        output.mbcrName(),
                        output.invtOpnn(),
                        output.invtOpnnClsCode(),
                        output.rgbfInvtOpnn(),
                        output.rgbfInvtOpnnClsCode(),
                        decimal(output.htsGoalPrc()),
                        decimal(output.stckPrdyClpr()),
                        decimal(output.stckNdayEsdg()),
                        decimal(output.ndayDprt()),
                        decimal(output.stftEsdg()),
                        decimal(output.dprt())
                ))
                .toList();
    }

    private ExpectationContextDto.EstimatedPerformanceDto estimatedPerformance(
            String stockCode,
            KisHhkst668300C0EstimatePerformResponse response
    ) {
        List<String> periods = periods(response.output4());
        List<ExpectationContextDto.IncomeStatementEstimateDto> incomeStatements = incomeStatementEstimates(periods, response.output2());
        List<ExpectationContextDto.InvestmentIndicatorEstimateDto> investmentIndicators = investmentIndicatorEstimates(periods, response.output3());
        return new ExpectationContextDto.EstimatedPerformanceDto(
                estimateIdentity(stockCode, response.output1()),
                incomeStatements,
                investmentIndicators,
                summary(incomeStatements, investmentIndicators),
                List.of(
                        "output2 행 순서: 매출액, 매출액증감율, 영업이익, 영업이익증감율, 순이익, 순이익증감율",
                        "output3 행 순서: EBITDA, EPS, EPS증감율, PER, EV/EBITDA, ROE, 부채비율, 이자보상배율"
                )
        );
    }

    private ExpectationContextDto.EstimateIdentityDto estimateIdentity(
            String stockCode,
            KisHhkst668300C0EstimatePerformResponse.Output1 output
    ) {
        return new ExpectationContextDto.EstimateIdentityDto(
                stripLeadingA(firstNonBlank(output == null ? null : output.shtCd(), stockCode)),
                output == null ? null : output.itemKorNm(),
                output == null ? null : output.name1(),
                output == null ? null : output.estdate(),
                output == null ? null : output.rcmdName(),
                output == null ? null : decimal(output.capital()),
                output == null ? null : decimal(output.fornItemLmtrt())
        );
    }

    private List<String> periods(List<KisHhkst668300C0EstimatePerformResponse.Output4> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(KisHhkst668300C0EstimatePerformResponse.Output4::dt)
                .toList();
    }

    private List<ExpectationContextDto.IncomeStatementEstimateDto> incomeStatementEstimates(
            List<String> periods,
            List<KisHhkst668300C0EstimatePerformResponse.Output2> outputs
    ) {
        if (periods.isEmpty()) {
            return List.of();
        }
        return indexes(periods.size()).stream()
                .map(index -> new ExpectationContextDto.IncomeStatementEstimateDto(
                        periods.get(index),
                        decimal(incomeValue(outputs, 0, index)),
                        decimal(incomeValue(outputs, 1, index)),
                        decimal(incomeValue(outputs, 2, index)),
                        decimal(incomeValue(outputs, 3, index)),
                        decimal(incomeValue(outputs, 4, index)),
                        decimal(incomeValue(outputs, 5, index))
                ))
                .toList();
    }

    private List<ExpectationContextDto.InvestmentIndicatorEstimateDto> investmentIndicatorEstimates(
            List<String> periods,
            List<KisHhkst668300C0EstimatePerformResponse.Output3> outputs
    ) {
        if (periods.isEmpty()) {
            return List.of();
        }
        return indexes(periods.size()).stream()
                .map(index -> new ExpectationContextDto.InvestmentIndicatorEstimateDto(
                        periods.get(index),
                        decimal(indicatorValue(outputs, 0, index)),
                        decimal(indicatorValue(outputs, 1, index)),
                        decimal(indicatorValue(outputs, 2, index)),
                        decimal(indicatorValue(outputs, 3, index)),
                        decimal(indicatorValue(outputs, 4, index)),
                        decimal(indicatorValue(outputs, 5, index)),
                        decimal(indicatorValue(outputs, 6, index)),
                        decimal(indicatorValue(outputs, 7, index))
                ))
                .toList();
    }

    private List<Integer> indexes(int size) {
        List<Integer> indexes = new ArrayList<>();
        for (int index = 0; index < Math.min(size, 5); index++) {
            indexes.add(index);
        }
        return indexes;
    }

    private String incomeValue(List<KisHhkst668300C0EstimatePerformResponse.Output2> outputs, int rowIndex, int dataIndex) {
        if (outputs == null || rowIndex >= outputs.size()) {
            return null;
        }
        KisHhkst668300C0EstimatePerformResponse.Output2 output = outputs.get(rowIndex);
        return switch (dataIndex) {
            case 0 -> output.data1();
            case 1 -> output.data2();
            case 2 -> output.data3();
            case 3 -> output.data4();
            case 4 -> output.data5();
            default -> null;
        };
    }

    private String indicatorValue(List<KisHhkst668300C0EstimatePerformResponse.Output3> outputs, int rowIndex, int dataIndex) {
        if (outputs == null || rowIndex >= outputs.size()) {
            return null;
        }
        KisHhkst668300C0EstimatePerformResponse.Output3 output = outputs.get(rowIndex);
        return switch (dataIndex) {
            case 0 -> output.data1();
            case 1 -> output.data2();
            case 2 -> output.data3();
            case 3 -> output.data4();
            case 4 -> output.data5();
            default -> null;
        };
    }

    private ExpectationContextDto.EstimateSummaryDto summary(
            List<ExpectationContextDto.IncomeStatementEstimateDto> incomeStatements,
            List<ExpectationContextDto.InvestmentIndicatorEstimateDto> investmentIndicators
    ) {
        if (incomeStatements.isEmpty()) {
            return null;
        }
        ExpectationContextDto.IncomeStatementEstimateDto incomeStatement = incomeStatements.getLast();
        ExpectationContextDto.InvestmentIndicatorEstimateDto investmentIndicator = investmentIndicators.isEmpty()
                ? null
                : investmentIndicators.getLast();
        return new ExpectationContextDto.EstimateSummaryDto(
                incomeStatement.period(),
                incomeStatement.estimatedSales(),
                incomeStatement.estimatedOperatingProfit(),
                incomeStatement.estimatedNetIncome(),
                investmentIndicator == null ? null : investmentIndicator.estimatedEps(),
                investmentIndicator == null ? null : investmentIndicator.estimatedPer(),
                investmentIndicator == null ? null : investmentIndicator.estimatedRoe()
        );
    }

    private ExpectationContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new ExpectationContextDto.KisApiCallSummary(
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

    private String stripLeadingA(String value) {
        if (value != null && value.length() == 7 && value.startsWith("A")) {
            return value.substring(1);
        }
        return value;
    }

    private String firstNonBlank(String first, String second) {
        return isBlank(first) ? second : first;
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
