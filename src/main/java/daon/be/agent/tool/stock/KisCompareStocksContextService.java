package daon.be.agent.tool.stock;

import daon.be.agent.tool.stock.dto.CompareStocksContextDto;
import daon.be.agent.tool.stock.dto.FundamentalContextDto;
import daon.be.agent.tool.stock.dto.PriceTrendContextDto;
import daon.be.agent.tool.stock.dto.StockNowContextDto;
import daon.be.agent.tool.stock.dto.SupplyDemandContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class KisCompareStocksContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "COMPARE_STOCKS_CONTEXT";
    private static final String DEFAULT_FOCUS = "PRICE_FUNDAMENTAL";
    private static final String DEFAULT_PERIOD_TYPE = "D";

    private final KisStockNowContextService stockNowContextService;
    private final KisPriceTrendContextService priceTrendContextService;
    private final KisSupplyDemandContextService supplyDemandContextService;
    private final KisFundamentalContextService fundamentalContextService;

    public CompareStocksContextDto getCompareStocksContext(
            String stockCodes,
            String startDate,
            String endDate,
            String focus
    ) {
        List<String> normalizedStockCodes = normalizeStockCodes(stockCodes);
        String normalizedFocus = normalizeFocus(focus);
        String normalizedStartDate = defaultIfBlank(startDate, LocalDate.now(SEOUL).minusMonths(1).format(DATE_FORMAT));
        String normalizedEndDate = defaultIfBlank(endDate, LocalDate.now(SEOUL).format(DATE_FORMAT));
        List<CompareStocksContextDto.ComparisonDimension> dimensions = dimensions(normalizedFocus);
        OffsetDateTime requestedAt = now();
        List<CompareStocksContextDto.SourceToolCallSummary> sourceToolCalls = new ArrayList<>();
        List<String> missingDataNotes = new ArrayList<>();
        List<SnapshotAccumulator> snapshots = new ArrayList<>();

        for (String stockCode : normalizedStockCodes) {
            SnapshotAccumulator snapshot = new SnapshotAccumulator(stockCode);
            collectStockNow(snapshot, sourceToolCalls, missingDataNotes);
            if (dimensions.contains(CompareStocksContextDto.ComparisonDimension.PRICE_TREND)) {
                collectPriceTrend(snapshot, normalizedStartDate, normalizedEndDate, sourceToolCalls, missingDataNotes);
            }
            if (dimensions.contains(CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL)) {
                collectFundamental(snapshot, sourceToolCalls, missingDataNotes);
            }
            if (dimensions.contains(CompareStocksContextDto.ComparisonDimension.SUPPLY_DEMAND)) {
                collectSupplyDemand(snapshot, normalizedStartDate, sourceToolCalls, missingDataNotes);
            }
            snapshots.add(snapshot);
        }

        List<CompareStocksContextDto.ComparableStockSnapshotDto> stocks = snapshots.stream()
                .map(SnapshotAccumulator::toDto)
                .toList();

        return new CompareStocksContextDto(
                new CompareStocksContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(sourceToolCalls),
                        CompareStocksContextDto.DataFreshness.MIXED_DOMAIN_CONTEXT,
                        List.of("비교 tool은 선택된 하위 domain tool만 fan-out 하며, 미구현 expectation 컨텍스트는 아직 포함하지 않습니다.")
                ),
                normalizedStockCodes,
                new CompareStocksContextDto.ComparisonScopeDto(
                        normalizedFocus,
                        normalizedStartDate,
                        normalizedEndDate,
                        DEFAULT_PERIOD_TYPE,
                        dimensions
                ),
                stocks,
                metrics(stocks),
                List.copyOf(missingDataNotes),
                List.of("비교 결과는 선택된 하위 도메인 툴의 정규화 DTO를 병합한 값이며, 원인 단정이 아니라 상대 비교 근거입니다.")
        );
    }

    private void collectStockNow(
            SnapshotAccumulator snapshot,
            List<CompareStocksContextDto.SourceToolCallSummary> sourceToolCalls,
            List<String> missingDataNotes
    ) {
        try {
            StockNowContextDto context = stockNowContextService.getStockNowContext(snapshot.stockCode);
            sourceToolCalls.add(sourceToolCall("GET_STOCK_NOW_CONTEXT", snapshot.stockCode, true, null));
            snapshot.merge(context);
        } catch (RuntimeException exception) {
            sourceToolCalls.add(sourceToolCall("GET_STOCK_NOW_CONTEXT", snapshot.stockCode, false, exception.getMessage()));
            snapshot.missingDimensions.add(CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE);
            missingDataNotes.add(snapshot.stockCode + " 현재가 컨텍스트 조회 실패: " + exception.getMessage());
        }
    }

    private void collectPriceTrend(
            SnapshotAccumulator snapshot,
            String startDate,
            String endDate,
            List<CompareStocksContextDto.SourceToolCallSummary> sourceToolCalls,
            List<String> missingDataNotes
    ) {
        try {
            PriceTrendContextDto context = priceTrendContextService.getPriceTrendContext(snapshot.stockCode, startDate, endDate, DEFAULT_PERIOD_TYPE);
            sourceToolCalls.add(sourceToolCall("GET_PRICE_TREND_CONTEXT", snapshot.stockCode, true, null));
            snapshot.merge(context);
        } catch (RuntimeException exception) {
            sourceToolCalls.add(sourceToolCall("GET_PRICE_TREND_CONTEXT", snapshot.stockCode, false, exception.getMessage()));
            snapshot.missingDimensions.add(CompareStocksContextDto.ComparisonDimension.PRICE_TREND);
            missingDataNotes.add(snapshot.stockCode + " 가격 추세 컨텍스트 조회 실패: " + exception.getMessage());
        }
    }

    private void collectFundamental(
            SnapshotAccumulator snapshot,
            List<CompareStocksContextDto.SourceToolCallSummary> sourceToolCalls,
            List<String> missingDataNotes
    ) {
        try {
            FundamentalContextDto context = fundamentalContextService.getFundamentalContext(snapshot.stockCode);
            sourceToolCalls.add(sourceToolCall("GET_FUNDAMENTAL_CONTEXT", snapshot.stockCode, true, null));
            snapshot.merge(context);
        } catch (RuntimeException exception) {
            sourceToolCalls.add(sourceToolCall("GET_FUNDAMENTAL_CONTEXT", snapshot.stockCode, false, exception.getMessage()));
            snapshot.missingDimensions.add(CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL);
            missingDataNotes.add(snapshot.stockCode + " 펀더멘털 컨텍스트 조회 실패: " + exception.getMessage());
        }
    }

    private void collectSupplyDemand(
            SnapshotAccumulator snapshot,
            String startDate,
            List<CompareStocksContextDto.SourceToolCallSummary> sourceToolCalls,
            List<String> missingDataNotes
    ) {
        try {
            SupplyDemandContextDto context = supplyDemandContextService.getSupplyDemandContext(snapshot.stockCode, startDate, null);
            sourceToolCalls.add(sourceToolCall("GET_SUPPLY_DEMAND_CONTEXT", snapshot.stockCode, true, null));
            snapshot.merge(context);
        } catch (RuntimeException exception) {
            sourceToolCalls.add(sourceToolCall("GET_SUPPLY_DEMAND_CONTEXT", snapshot.stockCode, false, exception.getMessage()));
            snapshot.missingDimensions.add(CompareStocksContextDto.ComparisonDimension.SUPPLY_DEMAND);
            missingDataNotes.add(snapshot.stockCode + " 수급 컨텍스트 조회 실패: " + exception.getMessage());
        }
    }

    private List<CompareStocksContextDto.ComparisonMetricDto> metrics(
            List<CompareStocksContextDto.ComparableStockSnapshotDto> stocks
    ) {
        List<CompareStocksContextDto.ComparisonMetricDto> metrics = new ArrayList<>();
        addMetric(metrics, stocks, "현재 등락률", CompareStocksContextDto.MetricDirection.HIGHER_IS_BETTER, "%", CompareStocksContextDto.ComparableStockSnapshotDto::currentChangeRate);
        addMetric(metrics, stocks, "기간 수익률", CompareStocksContextDto.MetricDirection.HIGHER_IS_BETTER, "%", CompareStocksContextDto.ComparableStockSnapshotDto::periodReturnRate);
        addMetric(metrics, stocks, "ROE", CompareStocksContextDto.MetricDirection.HIGHER_IS_BETTER, "%", CompareStocksContextDto.ComparableStockSnapshotDto::roe);
        addMetric(metrics, stocks, "부채비율", CompareStocksContextDto.MetricDirection.LOWER_IS_BETTER, "%", CompareStocksContextDto.ComparableStockSnapshotDto::debtRatio);
        return List.copyOf(metrics);
    }

    private void addMetric(
            List<CompareStocksContextDto.ComparisonMetricDto> metrics,
            List<CompareStocksContextDto.ComparableStockSnapshotDto> stocks,
            String metricName,
            CompareStocksContextDto.MetricDirection direction,
            String unit,
            Function<CompareStocksContextDto.ComparableStockSnapshotDto, BigDecimal> extractor
    ) {
        List<CompareStocksContextDto.MetricValueDto> values = stocks.stream()
                .map(stock -> new CompareStocksContextDto.MetricValueDto(stock.stockCode(), extractor.apply(stock)))
                .filter(value -> value.value() != null)
                .toList();
        if (values.isEmpty()) {
            return;
        }
        metrics.add(new CompareStocksContextDto.ComparisonMetricDto(
                metricName,
                direction,
                leader(values, direction),
                values,
                unit
        ));
    }

    private String leader(
            List<CompareStocksContextDto.MetricValueDto> values,
            CompareStocksContextDto.MetricDirection direction
    ) {
        CompareStocksContextDto.MetricValueDto leader = values.getFirst();
        for (CompareStocksContextDto.MetricValueDto value : values) {
            int compare = value.value().compareTo(leader.value());
            if (direction == CompareStocksContextDto.MetricDirection.HIGHER_IS_BETTER && compare > 0) {
                leader = value;
            }
            if (direction == CompareStocksContextDto.MetricDirection.LOWER_IS_BETTER && compare < 0) {
                leader = value;
            }
        }
        return leader.stockCode();
    }

    private List<CompareStocksContextDto.ComparisonDimension> dimensions(String focus) {
        return switch (focus) {
            case "PRICE" -> List.of(
                    CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                    CompareStocksContextDto.ComparisonDimension.PRICE_TREND
            );
            case "FUNDAMENTAL" -> List.of(
                    CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                    CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL
            );
            case "SUPPLY_DEMAND" -> List.of(
                    CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                    CompareStocksContextDto.ComparisonDimension.SUPPLY_DEMAND
            );
            case "ALL" -> List.of(
                    CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                    CompareStocksContextDto.ComparisonDimension.PRICE_TREND,
                    CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL,
                    CompareStocksContextDto.ComparisonDimension.SUPPLY_DEMAND
            );
            default -> List.of(
                    CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE,
                    CompareStocksContextDto.ComparisonDimension.PRICE_TREND,
                    CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL
            );
        };
    }

    private CompareStocksContextDto.SourceToolCallSummary sourceToolCall(
            String toolName,
            String stockCode,
            boolean success,
            String failureReason
    ) {
        return new CompareStocksContextDto.SourceToolCallSummary(toolName, stockCode, success, failureReason);
    }

    private List<String> normalizeStockCodes(String stockCodes) {
        if (isBlank(stockCodes)) {
            throw new IllegalArgumentException("stockCodes는 필수입니다.");
        }
        Set<String> normalized = new LinkedHashSet<>();
        for (String stockCode : stockCodes.split(",")) {
            if (!isBlank(stockCode)) {
                normalized.add(stockCode.trim());
            }
        }
        if (normalized.size() < 2) {
            throw new IllegalArgumentException("비교할 종목코드는 2개 이상이어야 합니다.");
        }
        return List.copyOf(normalized);
    }

    private String normalizeFocus(String focus) {
        if (isBlank(focus)) {
            return DEFAULT_FOCUS;
        }
        String normalized = focus.trim().toUpperCase();
        return switch (normalized) {
            case "PRICE", "FUNDAMENTAL", "SUPPLY_DEMAND", "PRICE_FUNDAMENTAL", "ALL" -> normalized;
            default -> DEFAULT_FOCUS;
        };
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }

    private static class SnapshotAccumulator {
        private final String stockCode;
        private String stockName;
        private String market;
        private String industryName;
        private BigDecimal currentPrice;
        private BigDecimal currentChangeRate;
        private BigDecimal tradingValue;
        private BigDecimal periodReturnRate;
        private Long averageVolume;
        private BigDecimal roe;
        private BigDecimal debtRatio;
        private BigDecimal eps;
        private Long foreignNetBuyQuantity;
        private Long institutionNetBuyQuantity;
        private final List<CompareStocksContextDto.ComparisonDimension> missingDimensions = new ArrayList<>();

        private SnapshotAccumulator(String stockCode) {
            this.stockCode = stockCode;
        }

        private void merge(StockNowContextDto context) {
            if (context == null) {
                missingDimensions.add(CompareStocksContextDto.ComparisonDimension.CURRENT_PRICE);
                return;
            }
            if (context.identity() != null) {
                stockName = firstNonBlank(stockName, context.identity().stockName());
                market = firstNonBlank(market, context.identity().market());
                industryName = firstNonBlank(industryName, context.identity().industryName());
            }
            if (context.currentPrice() != null) {
                currentPrice = context.currentPrice().price();
                currentChangeRate = context.currentPrice().changeRate();
                tradingValue = context.currentPrice().tradingValue();
            }
        }

        private void merge(PriceTrendContextDto context) {
            if (context == null) {
                missingDimensions.add(CompareStocksContextDto.ComparisonDimension.PRICE_TREND);
                return;
            }
            if (context.identity() != null) {
                stockName = firstNonBlank(stockName, context.identity().stockName());
                market = firstNonBlank(market, context.identity().market());
                industryName = firstNonBlank(industryName, context.identity().industryName());
            }
            if (context.trendSummary() != null) {
                periodReturnRate = context.trendSummary().periodReturnRate();
            }
            if (context.volatility() != null) {
                averageVolume = context.volatility().averageVolume();
            }
        }

        private void merge(FundamentalContextDto context) {
            if (context == null) {
                missingDimensions.add(CompareStocksContextDto.ComparisonDimension.FUNDAMENTAL);
                return;
            }
            if (context.identity() != null) {
                stockName = firstNonBlank(stockName, context.identity().stockName());
                market = firstNonBlank(market, context.identity().market());
                industryName = firstNonBlank(industryName, context.identity().industryName());
            }
            if (context.summary() != null) {
                roe = context.summary().roe();
                debtRatio = context.summary().debtRatio();
                eps = context.summary().eps();
            }
        }

        private void merge(SupplyDemandContextDto context) {
            if (context == null) {
                missingDimensions.add(CompareStocksContextDto.ComparisonDimension.SUPPLY_DEMAND);
                return;
            }
            if (context.identity() != null) {
                stockName = firstNonBlank(stockName, context.identity().stockName());
                market = firstNonBlank(market, context.identity().market());
            }
            if (context.dailyInvestorFlows() != null && !context.dailyInvestorFlows().isEmpty()) {
                SupplyDemandContextDto.DailyInvestorFlowDto latest = context.dailyInvestorFlows().getFirst();
                foreignNetBuyQuantity = latest.foreignNetBuyQuantity();
                institutionNetBuyQuantity = latest.institutionNetBuyQuantity();
            }
        }

        private CompareStocksContextDto.ComparableStockSnapshotDto toDto() {
            return new CompareStocksContextDto.ComparableStockSnapshotDto(
                    stockCode,
                    stockName,
                    market,
                    industryName,
                    currentPrice,
                    currentChangeRate,
                    tradingValue,
                    periodReturnRate,
                    averageVolume,
                    roe,
                    debtRatio,
                    eps,
                    foreignNetBuyQuantity,
                    institutionNetBuyQuantity,
                    List.copyOf(missingDimensions)
            );
        }

        private static String firstNonBlank(String first, String second) {
            if (first != null && !first.isBlank()) {
                return first;
            }
            if (second != null && !second.isBlank()) {
                return second;
            }
            return null;
        }
    }
}
