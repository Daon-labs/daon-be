package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record PriceTrendContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        String periodType,
        List<OhlcvDto> bars,
        TrendSummaryDto trendSummary,
        VolatilitySummaryDto volatility,
        BreakoutContextDto breakoutContext,
        List<String> limitations
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
        HISTORICAL_CONFIRMED
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName,
            String market,
            String industryCode,
            String industryName
    ) {
    }

    public record OhlcvDto(
            String date,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal close,
            Long volume,
            BigDecimal tradingValue,
            BigDecimal change,
            String changeSign
    ) {
    }

    public record TrendSummaryDto(
            String startDate,
            String latestDate,
            BigDecimal startClose,
            BigDecimal latestClose,
            BigDecimal periodChange,
            BigDecimal periodReturnRate
    ) {
    }

    public record VolatilitySummaryDto(
            BigDecimal highestPrice,
            String highestPriceDate,
            BigDecimal lowestPrice,
            String lowestPriceDate,
            BigDecimal highLowRangeRate,
            Long averageVolume
    ) {
    }

    public record BreakoutContextDto(
            BigDecimal currentPrice,
            BigDecimal week52High,
            String week52HighDate,
            BigDecimal distanceFromWeek52HighRate,
            BigDecimal week52Low,
            String week52LowDate,
            BigDecimal distanceFromWeek52LowRate
    ) {
    }
}
