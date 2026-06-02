package daon.be.agent.tool.stock;

import daon.be.agent.tool.stock.dto.CompareStocksContextDto;
import daon.be.agent.tool.stock.dto.FundamentalContextDto;
import daon.be.agent.tool.stock.dto.PriceTrendContextDto;
import daon.be.agent.tool.stock.dto.StockNowContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class KisCompareStocksContextServiceTest {

    private final KisStockNowContextService stockNowContextService = mock(KisStockNowContextService.class);
    private final KisPriceTrendContextService priceTrendContextService = mock(KisPriceTrendContextService.class);
    private final KisSupplyDemandContextService supplyDemandContextService = mock(KisSupplyDemandContextService.class);
    private final KisFundamentalContextService fundamentalContextService = mock(KisFundamentalContextService.class);
    private final KisCompareStocksContextService service = new KisCompareStocksContextService(
            stockNowContextService,
            priceTrendContextService,
            supplyDemandContextService,
            fundamentalContextService
    );

    @Test
    void getCompareStocksContextFansOutSelectedToolsAndBuildsComparableMetrics() {
        when(stockNowContextService.getStockNowContext("005930"))
                .thenReturn(stockNow("005930", "삼성전자", "KOSPI", "전기전자", "70000", "1.74", "987654321000"));
        when(stockNowContextService.getStockNowContext("000660"))
                .thenReturn(stockNow("000660", "SK하이닉스", "KOSPI", "반도체", "220000", "3.10", "765432100000"));
        when(priceTrendContextService.getPriceTrendContext("005930", "2026-05-01", "2026-06-02", "D"))
                .thenReturn(priceTrend("005930", "삼성전자", "20260501", "20260602", "68000", "70000", "2.94", 9000000L));
        when(priceTrendContextService.getPriceTrendContext("000660", "2026-05-01", "2026-06-02", "D"))
                .thenReturn(priceTrend("000660", "SK하이닉스", "20260501", "20260602", "200000", "220000", "10.00", 7000000L));
        when(fundamentalContextService.getFundamentalContext("005930"))
                .thenReturn(fundamental("005930", "삼성전자", "8.20", "32.10", "5200"));
        when(fundamentalContextService.getFundamentalContext("000660"))
                .thenReturn(fundamental("000660", "SK하이닉스", "12.40", "40.00", "15000"));

        CompareStocksContextDto result = service.getCompareStocksContext(
                "005930, 000660",
                "2026-05-01",
                "2026-06-02",
                "PRICE_FUNDAMENTAL"
        );

        assertThat(result.meta().toolName()).isEqualTo("COMPARE_STOCKS_CONTEXT");
        assertThat(result.stockCodes()).containsExactly("005930", "000660");
        assertThat(result.scope().dimensions()).containsExactly(
                CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                CompareStocksContextDto.ComparisonDimension.PRICE_TREND,
                CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL
        );
        assertThat(result.stocks()).hasSize(2);
        assertThat(result.stocks().getFirst().stockName()).isEqualTo("삼성전자");
        assertThat(result.stocks().getFirst().currentPrice()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.stocks().getFirst().periodReturnRate()).isEqualByComparingTo(new BigDecimal("2.94"));
        assertThat(result.stocks().getFirst().roe()).isEqualByComparingTo(new BigDecimal("8.20"));
        assertThat(result.stocks().get(1).stockCode()).isEqualTo("000660");
        assertThat(result.stocks().get(1).periodReturnRate()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(result.metrics()).extracting(CompareStocksContextDto.ComparisonMetricDto::metricName)
                .contains("현재 등락률", "기간 수익률", "ROE", "부채비율");
        assertThat(metric(result, "기간 수익률").leaderStockCode()).isEqualTo("000660");
        assertThat(metric(result, "ROE").leaderStockCode()).isEqualTo("000660");
        assertThat(metric(result, "부채비율").leaderStockCode()).isEqualTo("005930");
        assertThat(result.missingDataNotes()).isEmpty();
        assertThat(result.cautions()).contains("비교 결과는 선택된 하위 도메인 툴의 정규화 DTO를 병합한 값이며, 원인 단정이 아니라 상대 비교 근거입니다.");

        verify(stockNowContextService).getStockNowContext("005930");
        verify(stockNowContextService).getStockNowContext("000660");
        verify(priceTrendContextService).getPriceTrendContext("005930", "2026-05-01", "2026-06-02", "D");
        verify(priceTrendContextService).getPriceTrendContext("000660", "2026-05-01", "2026-06-02", "D");
        verify(fundamentalContextService).getFundamentalContext("005930");
        verify(fundamentalContextService).getFundamentalContext("000660");
        verifyNoInteractions(supplyDemandContextService);
    }

    private CompareStocksContextDto.ComparisonMetricDto metric(CompareStocksContextDto result, String metricName) {
        return result.metrics().stream()
                .filter(metric -> metric.metricName().equals(metricName))
                .findFirst()
                .orElseThrow();
    }

    private StockNowContextDto stockNow(
            String stockCode,
            String stockName,
            String market,
            String industryName,
            String price,
            String changeRate,
            String tradingValue
    ) {
        return new StockNowContextDto(
                stockNowMeta(),
                new StockNowContextDto.StockIdentityDto(stockCode, stockName, market, "KRX", "013", industryName, false, false, false, false, true),
                new StockNowContextDto.PricePointDto(decimal(price), BigDecimal.ZERO, decimal(changeRate), "2", 1000000L, decimal(tradingValue), OffsetDateTime.now()),
                new StockNowContextDto.DailyPriceRangeDto(null, null, null, null, null),
                null,
                null,
                new StockNowContextDto.ValuationSnapshotDto(null, null, null, null),
                List.of()
        );
    }

    private StockNowContextDto.ToolEvidenceMeta stockNowMeta() {
        return new StockNowContextDto.ToolEvidenceMeta(
                "GET_STOCK_NOW_CONTEXT",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                List.of(),
                StockNowContextDto.DataFreshness.INTRADAY_SNAPSHOT,
                List.of()
        );
    }

    private PriceTrendContextDto priceTrend(
            String stockCode,
            String stockName,
            String startDate,
            String latestDate,
            String startClose,
            String latestClose,
            String periodReturnRate,
            Long averageVolume
    ) {
        return new PriceTrendContextDto(
                priceTrendMeta(),
                new PriceTrendContextDto.StockIdentityDto(stockCode, stockName, "KOSPI", "013", null),
                "D",
                List.of(),
                new PriceTrendContextDto.TrendSummaryDto(startDate, latestDate, decimal(startClose), decimal(latestClose), decimal(latestClose).subtract(decimal(startClose)), decimal(periodReturnRate)),
                new PriceTrendContextDto.VolatilitySummaryDto(null, null, null, null, null, averageVolume),
                null,
                List.of()
        );
    }

    private PriceTrendContextDto.ToolEvidenceMeta priceTrendMeta() {
        return new PriceTrendContextDto.ToolEvidenceMeta(
                "GET_PRICE_TREND_CONTEXT",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                List.of(),
                PriceTrendContextDto.DataFreshness.HISTORICAL_CONFIRMED,
                List.of()
        );
    }

    private FundamentalContextDto fundamental(String stockCode, String stockName, String roe, String debtRatio, String eps) {
        return new FundamentalContextDto(
                fundamentalMeta(),
                new FundamentalContextDto.StockIdentityDto(stockCode, stockName, "STK", "KRX", "013", null, null, null, null, null, false, false, true),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                new FundamentalContextDto.FundamentalSummaryDto("202512", decimal(eps), null, decimal(roe), decimal(debtRatio), null, null, null, null),
                List.of()
        );
    }

    private FundamentalContextDto.ToolEvidenceMeta fundamentalMeta() {
        return new FundamentalContextDto.ToolEvidenceMeta(
                "GET_FUNDAMENTAL_CONTEXT",
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                List.of(),
                FundamentalContextDto.DataFreshness.DAILY_OR_PERIODIC_FINANCIAL,
                List.of()
        );
    }

    private BigDecimal decimal(String value) {
        return new BigDecimal(value);
    }
}
