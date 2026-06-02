package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record MarketMoverCandidatesDto(
        ToolEvidenceMeta meta,
        String market,
        List<RankedStockDto> volumeRankCandidates,
        List<RankedStockDto> fluctuationRankCandidates,
        List<RankedStockDto> volumePowerCandidates,
        List<RankedStockDto> bulkTradeCandidates,
        List<RankedStockDto> htsTopViewedCandidates,
        List<RankedStockDto> nearHighLowCandidates,
        List<RankedStockDto> limitPriceCandidates,
        List<MarketMoverCandidateSummaryDto> mergedCandidates,
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
        INTRADAY_RANKING_SNAPSHOT
    }

    public enum MoverCategory {
        VOLUME_RANK,
        FLUCTUATION_RANK,
        VOLUME_POWER,
        BULK_TRADE,
        HTS_TOP_VIEW,
        NEAR_HIGH_LOW,
        LIMIT_PRICE
    }

    public record RankedStockDto(
            MoverCategory category,
            Integer rank,
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal change,
            BigDecimal changeRate,
            String changeSign,
            Long volume,
            BigDecimal tradingValue,
            BigDecimal signalValue,
            String signalLabel
    ) {
    }

    public record MarketMoverCandidateSummaryDto(
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal changeRate,
            Long volume,
            int signalCount,
            List<MoverCategory> sourceCategories
    ) {
    }
}
