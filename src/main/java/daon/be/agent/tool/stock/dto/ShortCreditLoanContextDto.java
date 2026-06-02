package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ShortCreditLoanContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        List<ShortSaleDailyDto> shortSaleTrend,
        List<CreditBalanceDailyDto> creditBalanceTrend,
        List<LoanTransactionDailyDto> loanTransactionTrend,
        List<RankedStockDto> shortSaleRankCandidates,
        List<RankedStockDto> creditBalanceRankCandidates,
        RiskSummaryDto riskSummary,
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
        DAILY_RISK_AND_RANKING
    }

    public record StockIdentityDto(
            String stockCode,
            String market
    ) {
    }

    public record ShortSaleDailyDto(
            String date,
            BigDecimal close,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            Long shortSaleQuantity,
            BigDecimal shortSaleVolumeRatio,
            BigDecimal shortSaleAmount,
            BigDecimal shortSaleAmountRatio
    ) {
    }

    public record CreditBalanceDailyDto(
            String date,
            String settlementDate,
            BigDecimal currentPrice,
            Long loanNewQuantity,
            Long loanRedemptionQuantity,
            Long loanBalanceQuantity,
            BigDecimal loanBalanceAmount,
            BigDecimal loanBalanceRate,
            Long stockLoanBalanceQuantity,
            BigDecimal stockLoanBalanceAmount,
            BigDecimal stockLoanBalanceRate
    ) {
    }

    public record LoanTransactionDailyDto(
            String date,
            BigDecimal close,
            Long newLoanQuantity,
            Long redemptionQuantity,
            Long loanBalanceChange,
            Long loanBalanceQuantity,
            BigDecimal loanBalanceAmount
    ) {
    }

    public record RankedStockDto(
            Integer rank,
            String stockCode,
            String stockName,
            BigDecimal currentPrice,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long volume,
            BigDecimal signalValue,
            String signal
    ) {
    }

    public record RiskSummaryDto(
            Long latestShortSaleQuantity,
            BigDecimal latestShortSaleVolumeRatio,
            Long latestCreditLoanBalanceQuantity,
            BigDecimal latestCreditLoanBalanceRate,
            Long latestLoanTransactionBalanceQuantity,
            Long latestLoanTransactionBalanceChange
    ) {
    }
}
