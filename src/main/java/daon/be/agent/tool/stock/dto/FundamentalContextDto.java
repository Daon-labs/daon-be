package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record FundamentalContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        List<FinancialRatioPeriodDto> financialRatios,
        List<IncomeStatementPeriodDto> incomeStatements,
        List<BalanceSheetPeriodDto> balanceSheets,
        List<StabilityRatioPeriodDto> stabilityRatios,
        FundamentalSummaryDto summary,
        List<String> dataQualityNotes
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
        DAILY_OR_PERIODIC_FINANCIAL
    }

    public record StockIdentityDto(
            String stockCode,
            String stockName,
            String market,
            String exchangeCode,
            String industryCode,
            String industryName,
            Long listedShares,
            BigDecimal listedCapitalAmount,
            BigDecimal parValue,
            String listingDate,
            boolean tradingHalted,
            boolean administrativeIssue,
            boolean nxtTradable
    ) {
    }

    public record FinancialRatioPeriodDto(
            String period,
            BigDecimal salesGrowthRate,
            BigDecimal operatingProfitGrowthRate,
            BigDecimal netIncomeGrowthRate,
            BigDecimal roe,
            BigDecimal eps,
            BigDecimal salesPerShare,
            BigDecimal bps,
            BigDecimal reserveRate,
            BigDecimal debtRatio
    ) {
    }

    public record IncomeStatementPeriodDto(
            String period,
            BigDecimal revenue,
            BigDecimal costOfSales,
            BigDecimal grossProfit,
            BigDecimal depreciationCost,
            BigDecimal sellingGeneralAdministrativeExpense,
            BigDecimal operatingProfit,
            BigDecimal nonOperatingIncome,
            BigDecimal nonOperatingExpense,
            BigDecimal ordinaryProfit,
            BigDecimal extraordinaryGain,
            BigDecimal extraordinaryLoss,
            BigDecimal netIncome
    ) {
    }

    public record BalanceSheetPeriodDto(
            String period,
            BigDecimal currentAssets,
            BigDecimal fixedAssets,
            BigDecimal totalAssets,
            BigDecimal currentLiabilities,
            BigDecimal fixedLiabilities,
            BigDecimal totalLiabilities,
            BigDecimal capitalStock,
            BigDecimal capitalSurplus,
            BigDecimal retainedEarnings,
            BigDecimal totalEquity
    ) {
    }

    public record StabilityRatioPeriodDto(
            String period,
            BigDecimal debtRatio,
            BigDecimal borrowingDependency,
            BigDecimal currentRatio,
            BigDecimal quickRatio
    ) {
    }

    public record FundamentalSummaryDto(
            String latestPeriod,
            BigDecimal eps,
            BigDecimal bps,
            BigDecimal roe,
            BigDecimal debtRatio,
            BigDecimal operatingProfit,
            BigDecimal netIncome,
            BigDecimal totalAssets,
            BigDecimal totalEquity
    ) {
    }
}
