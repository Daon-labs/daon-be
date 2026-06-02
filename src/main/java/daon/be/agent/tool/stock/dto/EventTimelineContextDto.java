package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record EventTimelineContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        TimeRangeDto requestedRange,
        List<ViEventDto> viEvents,
        List<NewsTitleEventDto> newsTitleEvents,
        List<LimitPriceCaptureDto> limitPriceCaptures,
        List<EventCorrelationHintDto> correlationHints,
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
        INTRADAY_SNAPSHOT
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName
    ) {
    }

    public record TimeRangeDto(
            String tradingDate,
            String inputHour
    ) {
    }

    public record ViEventDto(
            String stockCode,
            String stockName,
            String date,
            String triggerTime,
            String cancelTime,
            String statusCode,
            String kindCode,
            BigDecimal triggerPrice,
            BigDecimal staticTriggerPrice,
            BigDecimal staticDeviationRate,
            BigDecimal dynamicTriggerPrice,
            BigDecimal dynamicDeviationRate,
            Long triggerCount
    ) {
    }

    public record NewsTitleEventDto(
            String serialNumber,
            String providerCode,
            String providerName,
            String date,
            String time,
            String title,
            String categoryCode,
            List<String> stockCodes
    ) {
    }

    public record LimitPriceCaptureDto(
            String captureType,
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal change,
            BigDecimal changeRate,
            String changeSign,
            Long accumulatedVolume,
            Long totalAskQuantity,
            Long totalBidQuantity,
            BigDecimal lowerLimit,
            BigDecimal upperLimit,
            BigDecimal volumeRate
    ) {
    }

    public record EventCorrelationHintDto(
            String eventType,
            String date,
            String time,
            String description
    ) {
    }
}
