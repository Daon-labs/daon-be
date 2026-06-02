package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010200TodayMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010200TodayMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010230DailyMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010230DailyMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhpst01060000TimeItemConclusionRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhpst01060000TimeItemConclusionResponse;
import daon.be.agent.tool.stock.dto.IntradayMoveContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisIntradayMoveContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_INTRADAY_MOVE_CONTEXT";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String DEFAULT_INPUT_HOUR = "153000";

    private final KisQuotationApiClient quotationApiClient;

    public IntradayMoveContextDto getIntradayMoveContext(String stockCode, String tradingDate, String inputHour) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedDate = normalizeDate(tradingDate);
        String normalizedHour = normalizeHour(inputHour);
        OffsetDateTime requestedAt = now();

        if (isBlank(normalizedDate)) {
            return todayContext(normalizedStockCode, normalizedHour, requestedAt);
        }
        return historicalContext(normalizedStockCode, normalizedDate, normalizedHour, requestedAt);
    }

    private IntradayMoveContextDto todayContext(String stockCode, String inputHour, OffsetDateTime requestedAt) {
        List<IntradayMoveContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime minuteCalledAt = now();
        KisFhkst03010200TodayMinuteChartResponse minuteResponse = quotationApiClient.inquireTodayMinuteChart(
                new KisFhkst03010200TodayMinuteChartRequest(STOCK_MARKET_CODE, stockCode, inputHour, "N", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST03010200_TODAY_MINUTE_CHART, minuteCalledAt, minuteResponse.rtCd(), minuteResponse.msg1()));

        OffsetDateTime conclusionCalledAt = now();
        KisFhpst01060000TimeItemConclusionResponse conclusionResponse = quotationApiClient.inquireTimeItemConclusion(
                new KisFhpst01060000TimeItemConclusionRequest(STOCK_MARKET_CODE, stockCode, inputHour)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01060000_TIME_ITEM_CONCLUSION, conclusionCalledAt, conclusionResponse.rtCd(), conclusionResponse.msg1()));

        validateSuccess(KisEndpoint.FHKST03010200_TODAY_MINUTE_CHART, minuteResponse.rtCd(), minuteResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01060000_TIME_ITEM_CONCLUSION, conclusionResponse.rtCd(), conclusionResponse.msg1());

        List<IntradayMoveContextDto.OhlcvDto> bars = todayBars(minuteResponse.output2());
        String tradingDate = bars.isEmpty() ? null : bars.getFirst().date();
        List<String> limitations = List.of("분봉은 KIS 응답 기준 최근 구간이며, 1회 조회 건수 제한을 받습니다.");

        return new IntradayMoveContextDto(
                meta(requestedAt, apiCalls, IntradayMoveContextDto.DataFreshness.INTRADAY_SNAPSHOT, limitations),
                new IntradayMoveContextDto.StockIdentityDto(stockCode, minuteResponse.output1().htsKorIsnm()),
                new IntradayMoveContextDto.TimeRangeDto(tradingDate, inputHour, "TODAY_MINUTE_CHART"),
                currentPrice(minuteResponse.output1()),
                bars,
                turningPoints(bars),
                executionSummaries(conclusionResponse.output2()),
                limitations
        );
    }

    private IntradayMoveContextDto historicalContext(String stockCode, String tradingDate, String inputHour, OffsetDateTime requestedAt) {
        List<IntradayMoveContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime minuteCalledAt = now();
        KisFhkst03010230DailyMinuteChartResponse minuteResponse = quotationApiClient.inquireDailyMinuteChart(
                new KisFhkst03010230DailyMinuteChartRequest(STOCK_MARKET_CODE, stockCode, inputHour, tradingDate, "N", "N")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST03010230_DAILY_MINUTE_CHART, minuteCalledAt, minuteResponse.rtCd(), minuteResponse.msg1()));
        validateSuccess(KisEndpoint.FHKST03010230_DAILY_MINUTE_CHART, minuteResponse.rtCd(), minuteResponse.msg1());

        List<String> limitations = List.of(
                "분봉은 KIS 응답 기준 최근 구간이며, 1회 조회 건수 제한을 받습니다.",
                "과거 거래일 조회에서는 당일시간대별체결 API를 호출하지 않습니다."
        );
        List<IntradayMoveContextDto.OhlcvDto> bars = historicalBars(minuteResponse.output2());

        return new IntradayMoveContextDto(
                meta(requestedAt, apiCalls, IntradayMoveContextDto.DataFreshness.HISTORICAL_CONFIRMED, limitations),
                new IntradayMoveContextDto.StockIdentityDto(stockCode, minuteResponse.output1().htsKorIsnm()),
                new IntradayMoveContextDto.TimeRangeDto(tradingDate, inputHour, "DAILY_MINUTE_CHART"),
                currentPrice(minuteResponse.output1()),
                bars,
                turningPoints(bars),
                List.of(),
                limitations
        );
    }

    private IntradayMoveContextDto.ToolEvidenceMeta meta(
            OffsetDateTime requestedAt,
            List<IntradayMoveContextDto.KisApiCallSummary> apiCalls,
            IntradayMoveContextDto.DataFreshness freshness,
            List<String> limitations
    ) {
        return new IntradayMoveContextDto.ToolEvidenceMeta(
                TOOL_NAME,
                requestedAt,
                now(),
                List.copyOf(apiCalls),
                freshness,
                limitations
        );
    }

    private IntradayMoveContextDto.PricePointDto currentPrice(KisFhkst03010200TodayMinuteChartResponse.Output1 output) {
        return new IntradayMoveContextDto.PricePointDto(
                decimal(output.stckPrpr()),
                decimal(output.prdyVrss()),
                decimal(output.prdyCtrt()),
                output.prdyVrssSign(),
                number(output.acmlVol()),
                decimal(output.acmlTrPbmn()),
                now()
        );
    }

    private IntradayMoveContextDto.PricePointDto currentPrice(KisFhkst03010230DailyMinuteChartResponse.Output1 output) {
        return new IntradayMoveContextDto.PricePointDto(
                decimal(output.stckPrpr()),
                decimal(output.prdyVrss()),
                decimal(output.prdyCtrt()),
                output.prdyVrssSign(),
                number(output.acmlVol()),
                decimal(output.acmlTrPbmn()),
                now()
        );
    }

    private List<IntradayMoveContextDto.OhlcvDto> todayBars(List<KisFhkst03010200TodayMinuteChartResponse.Output2> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new IntradayMoveContextDto.OhlcvDto(
                        output.stckBsopDate(),
                        output.stckCntgHour(),
                        decimal(output.stckOprc()),
                        decimal(output.stckHgpr()),
                        decimal(output.stckLwpr()),
                        decimal(output.stckPrpr()),
                        number(output.cntgVol()),
                        decimal(output.acmlTrPbmn()),
                        true
                ))
                .toList();
    }

    private List<IntradayMoveContextDto.OhlcvDto> historicalBars(List<KisFhkst03010230DailyMinuteChartResponse.Output2> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new IntradayMoveContextDto.OhlcvDto(
                        output.stckBsopDate(),
                        output.stckCntgHour(),
                        decimal(output.stckOprc()),
                        decimal(output.stckHgpr()),
                        decimal(output.stckLwpr()),
                        decimal(output.stckPrpr()),
                        number(output.cntgVol()),
                        decimal(output.acmlTrPbmn()),
                        false
                ))
                .toList();
    }

    private List<IntradayMoveContextDto.IntradayTurningPointDto> turningPoints(List<IntradayMoveContextDto.OhlcvDto> bars) {
        if (bars.isEmpty()) {
            return List.of();
        }
        List<IntradayMoveContextDto.IntradayTurningPointDto> points = new ArrayList<>();
        bars.stream()
                .filter(bar -> bar.high() != null)
                .max(Comparator.comparing(IntradayMoveContextDto.OhlcvDto::high))
                .ifPresent(bar -> points.add(turningPoint("HIGH", bar, bar.high(), "조회 구간 내 최고가 분봉")));
        bars.stream()
                .filter(bar -> bar.low() != null)
                .min(Comparator.comparing(IntradayMoveContextDto.OhlcvDto::low))
                .ifPresent(bar -> points.add(turningPoint("LOW", bar, bar.low(), "조회 구간 내 최저가 분봉")));
        bars.stream()
                .filter(bar -> bar.volume() != null)
                .max(Comparator.comparing(IntradayMoveContextDto.OhlcvDto::volume))
                .ifPresent(bar -> points.add(turningPoint("VOLUME_SPIKE", bar, bar.close(), "조회 구간 내 체결 거래량 최대 분봉")));
        return List.copyOf(points);
    }

    private IntradayMoveContextDto.IntradayTurningPointDto turningPoint(
            String type,
            IntradayMoveContextDto.OhlcvDto bar,
            BigDecimal price,
            String reason
    ) {
        return new IntradayMoveContextDto.IntradayTurningPointDto(
                type,
                bar.date(),
                bar.time(),
                price,
                bar.volume(),
                reason
        );
    }

    private List<IntradayMoveContextDto.TimeAndSalesSummaryDto> executionSummaries(
            List<KisFhpst01060000TimeItemConclusionResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new IntradayMoveContextDto.TimeAndSalesSummaryDto(
                        output.stckCntgHour(),
                        decimal(output.stckPbpr()),
                        decimal(output.askp()),
                        decimal(output.bidp()),
                        decimal(output.tdayRltv()),
                        number(output.acmlVol()),
                        number(output.cnqn())
                ))
                .toList();
    }

    private IntradayMoveContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new IntradayMoveContextDto.KisApiCallSummary(
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

    private String normalizeDate(String value) {
        if (isBlank(value)) {
            return "";
        }
        return value.trim().replace("-", "");
    }

    private String normalizeHour(String value) {
        if (isBlank(value)) {
            return DEFAULT_INPUT_HOUR;
        }
        return value.trim().replace(":", "");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
