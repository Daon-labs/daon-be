package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ScreenedStockCandidatesDto(
        ToolEvidenceMeta meta,
        String market,
        List<RankedStockDto> financialRatioRankCandidates,
        List<RankedStockDto> marketValueRankCandidates,
        List<RankedStockDto> marketCapRankCandidates,
        List<ScreenedStockCandidateDto> mergedCandidates,
        List<String> filteringRules
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
        DAILY_RANKING_SNAPSHOT
    }

    public enum ScreeningCategory {
        FINANCIAL_RATIO,
        MARKET_VALUE,
        MARKET_CAP
    }

    public record RankedStockDto(
            ScreeningCategory category,
            Integer rank,
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            String primaryMetricName,
            BigDecimal primaryMetricValue,
            BigDecimal totalCapitalNetIncomeRate,
            BigDecimal bis,
            BigDecimal debtRatio,
            BigDecimal salesGrowthRate,
            BigDecimal operatingProfitGrowthRate,
            BigDecimal netIncomeGrowthRate,
            BigDecimal per,
            BigDecimal pbr,
            BigDecimal eps,
            BigDecimal ebitda,
            Long listedShareCount,
            BigDecimal marketCap,
            BigDecimal marketCapShareRatio
    ) {
    }

    public record ScreenedStockCandidateDto(
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal changeRate,
            Long accumulatedVolume,
            BigDecimal marketCap,
            int signalCount,
            List<ScreeningCategory> sourceCategories
    ) {
    }
}
