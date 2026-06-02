package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record IntradayMoveContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        TimeRangeDto requestedRange,
        PricePointDto currentPrice,
        List<OhlcvDto> minuteBars,
        List<IntradayTurningPointDto> turningPoints,
        List<TimeAndSalesSummaryDto> executionSummaries,
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
        INTRADAY_SNAPSHOT,
        HISTORICAL_CONFIRMED
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName
    ) {
    }

    public record TimeRangeDto(
            String tradingDate,
            String inputHour,
            String source
    ) {
    }

    public record PricePointDto(
            BigDecimal price,
            BigDecimal change,
            BigDecimal changeRate,
            String changeSign,
            Long volume,
            BigDecimal tradingValue,
            OffsetDateTime observedAt
    ) {
    }

    public record OhlcvDto(
            String date,
            String time,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal close,
            Long volume,
            BigDecimal tradingValue,
            boolean provisional
    ) {
    }

    public record IntradayTurningPointDto(
            String type,
            String date,
            String time,
            BigDecimal price,
            Long volume,
            String reason
    ) {
    }

    public record TimeAndSalesSummaryDto(
            String time,
            BigDecimal price,
            BigDecimal askPrice,
            BigDecimal bidPrice,
            BigDecimal tradeStrength,
            Long accumulatedVolume,
            Long executionVolume
    ) {
    }
}
