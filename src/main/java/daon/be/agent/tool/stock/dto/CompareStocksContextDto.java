package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CompareStocksContextDto(
        ToolEvidenceMeta meta,
        List<String> stockCodes,
        ComparisonScopeDto scope,
        List<ComparableStockSnapshotDto> stocks,
        List<ComparisonMetricDto> metrics,
        List<String> missingDataNotes,
        List<String> cautions
) {

    public record ToolEvidenceMeta(
            String toolName,
            OffsetDateTime requestedAt,
            OffsetDateTime dataTimestamp,
            List<SourceToolCallSummary> sourceToolCalls,
            DataFreshness freshness,
            List<String> limitations
    ) {
    }

    public record SourceToolCallSummary(
            String toolName,
            String stockCode,
            boolean success,
            String failureReason
    ) {
    }

    public enum DataFreshness {
        MIXED_DOMAIN_CONTEXT
    }

    public enum ComparisonDimension {
        CURRENT_PRICE,
        PRICE_TREND,
        FUNDAMENTAL,
        SUPPLY_DEMAND
    }

    public record ComparisonScopeDto(
            String focus,
            String startDate,
            String endDate,
            String periodType,
            List<ComparisonDimension> dimensions
    ) {
    }

    public record ComparableStockSnapshotDto(
            String stockCode,
            String stockName,
            String market,
            String industryName,
            BigDecimal currentPrice,
            BigDecimal currentChangeRate,
            BigDecimal tradingValue,
            BigDecimal periodReturnRate,
            Long averageVolume,
            BigDecimal roe,
            BigDecimal debtRatio,
            BigDecimal eps,
            Long foreignNetBuyQuantity,
            Long institutionNetBuyQuantity,
            List<ComparisonDimension> missingDimensions
    ) {
    }

    public enum MetricDirection {
        HIGHER_IS_BETTER,
        LOWER_IS_BETTER,
        DESCRIPTIVE
    }

    public record ComparisonMetricDto(
            String metricName,
            MetricDirection direction,
            String leaderStockCode,
            List<MetricValueDto> values,
            String unit
    ) {
    }

    public record MetricValueDto(
            String stockCode,
            BigDecimal value
    ) {
    }
}
