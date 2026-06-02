package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record AfterHoursContextDto(
        ToolEvidenceMeta meta,
        LocalDate tradingDate,
        String market,
        AfterHoursMarketSummaryDto marketSummary,
        List<RankedStockDto> afterHoursVolumeCandidates,
        List<RankedStockDto> afterHoursFluctuationCandidates,
        List<NewsTitleEventDto> afterHoursNewsTitleEvents,
        List<AfterHoursCandidateSummaryDto> mergedCandidates,
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
        AFTER_HOURS_RANKING_SNAPSHOT
    }

    public enum AfterHoursCategory {
        OVERTIME_VOLUME,
        OVERTIME_FLUCTUATION
    }

    public record AfterHoursMarketSummaryDto(
            Long exchangeVolume,
            BigDecimal exchangeTradingValue,
            Long kosdaqVolume,
            BigDecimal kosdaqTradingValue,
            Long totalOvertimeVolume,
            BigDecimal totalOvertimeTradingValue,
            Integer upperLimitCount,
            Integer risingCount,
            Integer unchangedCount,
            Integer lowerLimitCount,
            Integer fallingCount
    ) {
    }

    public record RankedStockDto(
            AfterHoursCategory category,
            Integer rank,
            String stockCode,
            String stockName,
            BigDecimal overtimePrice,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long overtimeVolume,
            BigDecimal overtimeVolumeRatio,
            BigDecimal regularSessionPrice,
            Long regularSessionVolume,
            String signal
    ) {
    }

    public record NewsTitleEventDto(
            String serialNumber,
            String providerCode,
            String source,
            String date,
            String time,
            String title,
            String newsCategoryCode,
            List<String> stockCodes
    ) {
    }

    public record AfterHoursCandidateSummaryDto(
            String stockCode,
            String stockName,
            BigDecimal overtimePrice,
            BigDecimal changeRate,
            Long overtimeVolume,
            int signalCount,
            int relatedNewsCount,
            List<AfterHoursCategory> sourceCategories
    ) {
    }
}
