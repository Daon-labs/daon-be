package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record StockNowContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        PricePointDto currentPrice,
        DailyPriceRangeDto dailyRange,
        OrderbookSnapshotDto orderbook,
        TradingStatusDto tradingStatus,
        ValuationSnapshotDto valuationFromQuote,
        List<String> interpretationHints
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
        UNAVAILABLE
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName,
            String market,
            String exchange,
            String industryCode,
            String industryName,
            Boolean etf,
            Boolean etn,
            Boolean managementIssue,
            Boolean tradingSuspended,
            Boolean nxtAvailable
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

    public record DailyPriceRangeDto(
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal upperLimit,
            BigDecimal lowerLimit
    ) {
    }

    public record OrderbookSnapshotDto(
            String acceptedTime,
            List<OrderbookLevelDto> levels,
            Long totalAskQuantity,
            Long totalBidQuantity,
            Long netBidAskQuantity,
            String marketOperationCode
    ) {
    }

    public record OrderbookLevelDto(
            int level,
            BigDecimal askPrice,
            Long askQuantity,
            BigDecimal bidPrice,
            Long bidQuantity
    ) {
    }

    public record TradingStatusDto(
            Boolean tradingSuspended,
            Boolean temporarySuspended,
            Boolean investmentCaution,
            Boolean managementIssue,
            String viCode,
            String marketWarningCode
    ) {
    }

    public record ValuationSnapshotDto(
            BigDecimal per,
            BigDecimal pbr,
            BigDecimal eps,
            BigDecimal bps
    ) {
    }
}
