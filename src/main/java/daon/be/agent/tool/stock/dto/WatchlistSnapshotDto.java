package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record WatchlistSnapshotDto(
        ToolEvidenceMeta meta,
        String watchlistId,
        List<WatchlistStockSnapshotDto> stocks,
        List<WatchlistAlertCandidateDto> alertCandidates,
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
        INTRADAY_WATCHLIST_SNAPSHOT
    }

    public enum WatchlistSignal {
        VOLUME_RANK,
        FLUCTUATION_RANK
    }

    public record WatchlistStockSnapshotDto(
            String stockCode,
            String stockName,
            String marketName,
            String marketTreatmentName,
            String hourClassCode,
            BigDecimal currentPrice,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            BigDecimal tradingValue,
            BigDecimal openPrice,
            BigDecimal highPrice,
            BigDecimal lowPrice,
            BigDecimal askPrice,
            BigDecimal bidPrice,
            Long totalAskQuantity,
            Long totalBidQuantity,
            List<WatchlistSignal> matchedSignals
    ) {
    }

    public record WatchlistAlertCandidateDto(
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal changeRate,
            Long accumulatedVolume,
            BigDecimal tradingValue,
            int signalCount,
            List<WatchlistSignal> signals,
            List<RankSignalDto> rankSignals
    ) {
    }

    public record RankSignalDto(
            WatchlistSignal signal,
            Integer rank,
            BigDecimal value,
            String label
    ) {
    }
}
