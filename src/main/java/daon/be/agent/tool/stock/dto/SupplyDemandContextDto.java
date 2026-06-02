package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record SupplyDemandContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        IntradayEstimatedInvestorFlowDto intradayEstimate,
        List<DailyInvestorFlowDto> dailyInvestorFlows,
        List<InstitutionForeignRankDto> marketInstitutionForeignRanks,
        MarketInvestorFlowDto marketFlow,
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
        MIXED_INTRADAY_AND_DAILY
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName,
            String market
    ) {
    }

    public record IntradayEstimatedInvestorFlowDto(
            String time,
            Long foreignNetBuyQuantity,
            Long institutionNetBuyQuantity,
            Long totalNetBuyQuantity,
            boolean estimated
    ) {
    }

    public record DailyInvestorFlowDto(
            String date,
            BigDecimal close,
            Long foreignNetBuyQuantity,
            BigDecimal foreignNetBuyAmount,
            Long individualNetBuyQuantity,
            BigDecimal individualNetBuyAmount,
            Long institutionNetBuyQuantity,
            BigDecimal institutionNetBuyAmount
    ) {
    }

    public record InstitutionForeignRankDto(
            String stockCode,
            String stockName,
            Long netBuyQuantity,
            BigDecimal currentPrice,
            Long foreignNetBuyQuantity,
            BigDecimal foreignNetBuyAmount,
            Long institutionNetBuyQuantity,
            BigDecimal institutionNetBuyAmount
    ) {
    }

    public record MarketInvestorFlowDto(
            String date,
            Long foreignNetBuyQuantity,
            BigDecimal foreignNetBuyAmount,
            Long individualNetBuyQuantity,
            BigDecimal individualNetBuyAmount,
            Long institutionNetBuyQuantity,
            BigDecimal institutionNetBuyAmount
    ) {
    }
}
