package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.daily.KisFhkst03010100DailyItemChartPriceRequest;
import daon.be.agent.data.source.kis.quotation.daily.KisFhkst03010100DailyItemChartPriceResponse;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceResponse;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.PriceTrendContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisPriceTrendContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_PRICE_TREND_CONTEXT";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String STOCK_PRODUCT_TYPE_CODE = "300";
    private static final String DEFAULT_PERIOD_TYPE = "D";

    private final KisQuotationApiClient quotationApiClient;

    public PriceTrendContextDto getPriceTrendContext(String stockCode, String startDate, String endDate, String periodType) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedStartDate = normalizeDate(startDate, LocalDate.now(SEOUL).minusMonths(1));
        String normalizedEndDate = normalizeDate(endDate, LocalDate.now(SEOUL));
        String normalizedPeriodType = normalizePeriodType(periodType);
        OffsetDateTime requestedAt = now();
        List<PriceTrendContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime chartCalledAt = now();
        KisFhkst03010100DailyItemChartPriceResponse chartResponse = quotationApiClient.inquireDailyItemChartPrice(
                new KisFhkst03010100DailyItemChartPriceRequest(
                        STOCK_MARKET_CODE,
                        normalizedStockCode,
                        normalizedStartDate,
                        normalizedEndDate,
                        normalizedPeriodType,
                        "0"
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST03010100_DAILY_ITEM_CHART_PRICE, chartCalledAt, chartResponse.rtCd(), chartResponse.msg1()));

        OffsetDateTime priceCalledAt = now();
        KisFhkst01010100InquirePriceResponse priceResponse = quotationApiClient.inquirePrice(
                new KisFhkst01010100InquirePriceRequest(STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST01010100_INQUIRE_PRICE, priceCalledAt, priceResponse.rtCd(), priceResponse.msg1()));

        OffsetDateTime stockInfoCalledAt = now();
        KisCtpf1002rSearchStockInfoResponse stockInfoResponse = quotationApiClient.searchStockInfo(
                new KisCtpf1002rSearchStockInfoRequest(STOCK_PRODUCT_TYPE_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoCalledAt, stockInfoResponse.rtCd(), stockInfoResponse.msg1()));

        validateSuccess(KisEndpoint.FHKST03010100_DAILY_ITEM_CHART_PRICE, chartResponse.rtCd(), chartResponse.msg1());
        validateSuccess(KisEndpoint.FHKST01010100_INQUIRE_PRICE, priceResponse.rtCd(), priceResponse.msg1());
        validateSuccess(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoResponse.rtCd(), stockInfoResponse.msg1());

        List<PriceTrendContextDto.OhlcvDto> bars = bars(chartResponse.output2());
        List<String> limitations = List.of(
                "기간별시세는 요청 기간과 KIS 응답 건수 제한을 받습니다.",
                "장중 조회 시 최신 일봉은 확정 전 데이터일 수 있습니다."
        );

        return new PriceTrendContextDto(
                new PriceTrendContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        PriceTrendContextDto.DataFreshness.HISTORICAL_CONFIRMED,
                        limitations
                ),
                identity(normalizedStockCode, chartResponse.output1(), priceResponse.output(), stockInfoResponse.output()),
                normalizedPeriodType,
                bars,
                trendSummary(bars),
                volatilitySummary(bars),
                breakoutContext(priceResponse.output()),
                limitations
        );
    }

    private PriceTrendContextDto.StockIdentityDto identity(
            String stockCode,
            KisFhkst03010100DailyItemChartPriceResponse.Output1 chartSummary,
            KisFhkst01010100InquirePriceResponse.Output price,
            KisCtpf1002rSearchStockInfoResponse.Output stockInfo
    ) {
        return new PriceTrendContextDto.StockIdentityDto(
                firstNonBlank(price.stckShrnIscd(), chartSummary.stckShrnIscd(), stockInfo.pdno(), stockCode),
                firstNonBlank(stockInfo.prdtName(), stockInfo.prdtAbrvName(), chartSummary.htsKorIsnm()),
                firstNonBlank(price.rprsMrktKorName(), stockInfo.mketIdCd()),
                firstNonBlank(stockInfo.idxBztpSclsCd(), stockInfo.stdIdstClsfCd()),
                firstNonBlank(stockInfo.idxBztpSclsCdName(), stockInfo.stdIdstClsfCdName(), price.bstpKorIsnm())
        );
    }

    private List<PriceTrendContextDto.OhlcvDto> bars(List<KisFhkst03010100DailyItemChartPriceResponse.Output2> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new PriceTrendContextDto.OhlcvDto(
                        output.stckBsopDate(),
                        decimal(output.stckOprc()),
                        decimal(output.stckHgpr()),
                        decimal(output.stckLwpr()),
                        decimal(output.stckClpr()),
                        number(output.acmlVol()),
                        decimal(output.acmlTrPbmn()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign()
                ))
                .toList();
    }

    private PriceTrendContextDto.TrendSummaryDto trendSummary(List<PriceTrendContextDto.OhlcvDto> bars) {
        if (bars.isEmpty()) {
            return new PriceTrendContextDto.TrendSummaryDto(null, null, null, null, null, null);
        }
        PriceTrendContextDto.OhlcvDto start = bars.stream()
                .min(Comparator.comparing(PriceTrendContextDto.OhlcvDto::date))
                .orElseThrow();
        PriceTrendContextDto.OhlcvDto latest = bars.stream()
                .max(Comparator.comparing(PriceTrendContextDto.OhlcvDto::date))
                .orElseThrow();
        BigDecimal periodChange = subtract(latest.close(), start.close());
        return new PriceTrendContextDto.TrendSummaryDto(
                start.date(),
                latest.date(),
                start.close(),
                latest.close(),
                periodChange,
                rate(periodChange, start.close())
        );
    }

    private PriceTrendContextDto.VolatilitySummaryDto volatilitySummary(List<PriceTrendContextDto.OhlcvDto> bars) {
        if (bars.isEmpty()) {
            return new PriceTrendContextDto.VolatilitySummaryDto(null, null, null, null, null, null);
        }
        PriceTrendContextDto.OhlcvDto high = bars.stream()
                .filter(bar -> bar.high() != null)
                .max(Comparator.comparing(PriceTrendContextDto.OhlcvDto::high))
                .orElse(null);
        PriceTrendContextDto.OhlcvDto low = bars.stream()
                .filter(bar -> bar.low() != null)
                .min(Comparator.comparing(PriceTrendContextDto.OhlcvDto::low))
                .orElse(null);
        BigDecimal range = high == null || low == null ? null : subtract(high.high(), low.low());
        long volumeCount = bars.stream().filter(bar -> bar.volume() != null).count();
        Long averageVolume = volumeCount == 0
                ? null
                : bars.stream()
                .filter(bar -> bar.volume() != null)
                .mapToLong(PriceTrendContextDto.OhlcvDto::volume)
                .sum() / volumeCount;
        return new PriceTrendContextDto.VolatilitySummaryDto(
                high == null ? null : high.high(),
                high == null ? null : high.date(),
                low == null ? null : low.low(),
                low == null ? null : low.date(),
                low == null ? null : rate(range, low.low()),
                averageVolume
        );
    }

    private PriceTrendContextDto.BreakoutContextDto breakoutContext(KisFhkst01010100InquirePriceResponse.Output price) {
        return new PriceTrendContextDto.BreakoutContextDto(
                decimal(price.stckPrpr()),
                decimal(price.w52Hgpr()),
                price.w52HgprDate(),
                decimal(price.w52HgprVrssPrprCtrt()),
                decimal(price.w52Lwpr()),
                price.w52LwprDate(),
                decimal(price.w52LwprVrssPrprCtrt())
        );
    }

    private PriceTrendContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new PriceTrendContextDto.KisApiCallSummary(
                endpoint.apiKoreanName(),
                endpoint.trId(),
                endpoint.url(),
                calledAt,
                success,
                success ? null : msg1
        );
    }

    private void validateSuccess(KisEndpoint endpoint, String rtCd, String msg1) {
        if (!KisResponseValidator.isSuccess(rtCd)) {
            throw new IllegalStateException(endpoint.apiKoreanName() + " KIS 응답 실패: " + msg1);
        }
    }

    private BigDecimal subtract(BigDecimal left, BigDecimal right) {
        if (left == null || right == null) {
            return null;
        }
        return left.subtract(right);
    }

    private BigDecimal rate(BigDecimal numerator, BigDecimal denominator) {
        if (numerator == null || denominator == null || BigDecimal.ZERO.compareTo(denominator) == 0) {
            return null;
        }
        return numerator.multiply(BigDecimal.valueOf(100)).divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal decimal(String value) {
        if (isBlank(value)) {
            return null;
        }
        return new BigDecimal(value.trim().replace(",", ""));
    }

    private Long number(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Long.parseLong(value.trim().replace(",", ""));
    }

    private String normalizeRequired(String value, String name) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + "는 필수입니다.");
        }
        return value.trim();
    }

    private String normalizeDate(String value, LocalDate defaultDate) {
        if (isBlank(value)) {
            return defaultDate.format(DATE_FORMAT);
        }
        return value.trim().replace("-", "");
    }

    private String normalizePeriodType(String value) {
        if (isBlank(value)) {
            return DEFAULT_PERIOD_TYPE;
        }
        return value.trim().toUpperCase();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
