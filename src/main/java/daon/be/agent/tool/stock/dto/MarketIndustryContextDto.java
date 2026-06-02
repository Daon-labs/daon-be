package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record MarketIndustryContextDto(
        ToolEvidenceMeta meta,
        String market,
        IndustryIndexSnapshotDto industrySnapshot,
        List<OhlcvDto> industryMinuteBars,
        List<OhlcvDto> industryDailyBars,
        MarketInvestorFlowDto intradayMarketInvestorFlow,
        List<MarketInvestorFlowDto> dailyMarketInvestorFlows,
        RelativeStrengthDto relativeStrength,
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
        INTRADAY_SNAPSHOT
    }

    public record IndustryIndexSnapshotDto(
            String industryCode,
            String industryName,
            BigDecimal currentIndex,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            BigDecimal accumulatedTradingValue,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            Long risingIssueCount,
            Long fallingIssueCount,
            Long unchangedIssueCount
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
            BigDecimal changeRate
    ) {
    }

    public record MarketInvestorFlowDto(
            String date,
            String scope,
            Long foreignNetBuyQuantity,
            BigDecimal foreignNetBuyAmount,
            Long individualNetBuyQuantity,
            BigDecimal individualNetBuyAmount,
            Long institutionNetBuyQuantity,
            BigDecimal institutionNetBuyAmount
    ) {
    }

    public record RelativeStrengthDto(
            BigDecimal industryChangeRate,
            BigDecimal latestDailyChangeRate,
            String description
    ) {
    }
}
