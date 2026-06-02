package daon.be.agent.tool.stock.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ProgramTradingContextDto(
        ToolEvidenceMeta meta,
        StockIdentityDto identity,
        List<StockProgramTradePointDto> stockIntradayProgramTrades,
        List<StockProgramTradeDailyDto> stockDailyProgramTrades,
        List<MarketProgramTradePointDto> marketIntradayProgramTrades,
        List<MarketProgramTradeDailyDto> marketDailyProgramTrades,
        ProgramTradeInvestorBreakdownDto investorBreakdown,
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
        MIXED_INTRADAY_AND_DAILY
    }

    public record StockIdentityDto(
            String stockCode,
            String market
    ) {
    }

    public record StockProgramTradePointDto(
            String time,
            BigDecimal currentPrice,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            Long sellVolume,
            Long buyVolume,
            Long netBuyQuantity,
            BigDecimal sellAmount,
            BigDecimal buyAmount,
            BigDecimal netBuyAmount
    ) {
    }

    public record StockProgramTradeDailyDto(
            String date,
            BigDecimal close,
            BigDecimal change,
            String changeSign,
            BigDecimal changeRate,
            Long accumulatedVolume,
            BigDecimal accumulatedTradingValue,
            Long sellVolume,
            Long buyVolume,
            Long netBuyQuantity,
            BigDecimal sellAmount,
            BigDecimal buyAmount,
            BigDecimal netBuyAmount
    ) {
    }

    public record MarketProgramTradePointDto(
            String time,
            BigDecimal arbitrageNetBuyAmount,
            BigDecimal nonArbitrageNetBuyAmount,
            BigDecimal wholeNetBuyAmount,
            BigDecimal indexPrice,
            BigDecimal indexChange,
            String indexChangeSign
    ) {
    }

    public record MarketProgramTradeDailyDto(
            String date,
            Long arbitrageNetBuyQuantity,
            Long nonArbitrageNetBuyQuantity,
            Long wholeNetBuyQuantity,
            BigDecimal arbitrageNetBuyAmount,
            BigDecimal nonArbitrageNetBuyAmount,
            BigDecimal wholeNetBuyAmount
    ) {
    }

    public record ProgramTradeInvestorBreakdownDto(
            List<InvestorProgramTradeDto> investors
    ) {
    }

    public record InvestorProgramTradeDto(
            String investorCode,
            String investorName,
            Long wholeSellQuantity,
            Long wholeBuyQuantity,
            Long wholeNetBuyQuantity,
            BigDecimal wholeSellAmount,
            BigDecimal wholeBuyAmount,
            BigDecimal wholeNetBuyAmount,
            BigDecimal arbitrageNetBuyAmount,
            BigDecimal nonArbitrageNetBuyAmount
    ) {
    }
}
