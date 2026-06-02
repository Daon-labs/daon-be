package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ExpectationContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        List<InvestmentOpinionDto> investmentOpinions,
        EstimatedPerformanceDto estimatedPerformance,
        List<String> cautions
) {

    public record ToolEvidenceMeta(
            String toolName,
            OffsetDateTime requestedAt,
            OffsetDateTime dataTimestamp,
            List<KisApiCallSummary> kisApiCalls,
            DataFreshness freshness,
            List<String> limitations
    ) {
    }

    public record KisApiCallSummary(
            String apiName,
            String trId,
            String url,
            OffsetDateTime calledAt,
            boolean success,
            String failureReason
    ) {
    }

    public enum DataFreshness {
        DAILY_OR_MONTHLY_EXPECTATION
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName,
            String market
    ) {
    }

    public record InvestmentOpinionDto(
            String date,
            String memberName,
            String opinion,
            String opinionCode,
            String previousOpinion,
            String previousOpinionCode,
            BigDecimal targetPrice,
            BigDecimal previousClose,
            BigDecimal targetPriceGap,
            BigDecimal targetPriceGapRate,
            BigDecimal futuresGap,
            BigDecimal futuresGapRate
    ) {
    }

    public record EstimatedPerformanceDto(
            EstimateIdentityDto identity,
            List<IncomeStatementEstimateDto> incomeStatementEstimates,
            List<InvestmentIndicatorEstimateDto> investmentIndicatorEstimates,
            EstimateSummaryDto summary,
            List<String> rowMappingNotes
    ) {
    }

    public record EstimateIdentityDto(
            String stockCode,
            String stockName,
            String analystName,
            String estimateDate,
            String recommendation,
            BigDecimal capital,
            BigDecimal foreignLimitRate
    ) {
    }

    public record IncomeStatementEstimateDto(
            String period,
            BigDecimal estimatedSales,
            BigDecimal salesGrowthRate,
            BigDecimal estimatedOperatingProfit,
            BigDecimal operatingProfitGrowthRate,
            BigDecimal estimatedNetIncome,
            BigDecimal netIncomeGrowthRate
    ) {
    }

    public record InvestmentIndicatorEstimateDto(
            String period,
            BigDecimal estimatedEbitda,
            BigDecimal estimatedEps,
            BigDecimal epsGrowthRate,
            BigDecimal estimatedPer,
            BigDecimal estimatedEvEbitda,
            BigDecimal estimatedRoe,
            BigDecimal debtRatio,
            BigDecimal interestCoverageRatio
    ) {
    }

    public record EstimateSummaryDto(
            String latestPeriod,
            BigDecimal latestEstimatedSales,
            BigDecimal latestEstimatedOperatingProfit,
            BigDecimal latestEstimatedNetIncome,
            BigDecimal latestEstimatedEps,
            BigDecimal latestEstimatedPer,
            BigDecimal latestEstimatedRoe
    ) {
    }
}
