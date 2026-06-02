package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleResponse;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceResponse;
import daon.be.agent.data.source.kis.quotation.event.KisFhpst01390000ViStatusRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhpst01390000ViStatusResponse;
import daon.be.agent.tool.stock.dto.EventTimelineContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisEventTimelineContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String TOOL_NAME = "GET_EVENT_TIMELINE_CONTEXT";
    private static final String DEFAULT_INPUT_HOUR = "153000";

    private final KisQuotationApiClient quotationApiClient;

    public EventTimelineContextDto getEventTimelineContext(String stockCode, String tradingDate, String inputHour) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedDate = normalizeDate(tradingDate);
        String normalizedHour = normalizeHour(inputHour);
        OffsetDateTime requestedAt = now();
        List<EventTimelineContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime viCalledAt = now();
        KisFhpst01390000ViStatusResponse viResponse = quotationApiClient.inquireViStatus(
                new KisFhpst01390000ViStatusRequest("0", "20139", "0", normalizedStockCode, "0", normalizedDate, "", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01390000_VI_STATUS, viCalledAt, viResponse.rtCd(), viResponse.msg1()));

        OffsetDateTime newsCalledAt = now();
        KisFhkst01011800NewsTitleResponse newsResponse = quotationApiClient.inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", "", normalizedStockCode, "", newsDate(normalizedDate), newsHour(normalizedHour), "", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST01011800_NEWS_TITLE, newsCalledAt, newsResponse.rtCd(), newsResponse.msg1()));

        OffsetDateTime upperLimitCalledAt = now();
        KisFhkst130000C0CaptureUpLowPriceResponse upperLimitResponse = quotationApiClient.captureUpLowPrice(
                captureRequest("0")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, upperLimitCalledAt, upperLimitResponse.rtCd(), upperLimitResponse.msg1()));

        OffsetDateTime lowerLimitCalledAt = now();
        KisFhkst130000C0CaptureUpLowPriceResponse lowerLimitResponse = quotationApiClient.captureUpLowPrice(
                captureRequest("1")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, lowerLimitCalledAt, lowerLimitResponse.rtCd(), lowerLimitResponse.msg1()));

        validateSuccess(KisEndpoint.FHPST01390000_VI_STATUS, viResponse.rtCd(), viResponse.msg1());
        validateSuccess(KisEndpoint.FHKST01011800_NEWS_TITLE, newsResponse.rtCd(), newsResponse.msg1());
        validateSuccess(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, upperLimitResponse.rtCd(), upperLimitResponse.msg1());
        validateSuccess(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, lowerLimitResponse.rtCd(), lowerLimitResponse.msg1());

        List<EventTimelineContextDto.ViEventDto> viEvents = viEvents(normalizedStockCode, viResponse.output());
        List<EventTimelineContextDto.NewsTitleEventDto> newsTitleEvents = newsEvents(normalizedStockCode, newsResponse.output());
        List<EventTimelineContextDto.LimitPriceCaptureDto> limitPriceCaptures = new ArrayList<>();
        limitPriceCaptures.addAll(limitPriceCaptures(normalizedStockCode, "UPPER_LIMIT", upperLimitResponse.output()));
        limitPriceCaptures.addAll(limitPriceCaptures(normalizedStockCode, "LOWER_LIMIT", lowerLimitResponse.output()));

        List<String> cautions = List.of(
                "뉴스/공시 제목은 원인 확정 근거가 아니라 동시간대 이벤트 후보입니다.",
                "상하한가 포착은 시장 전체 순위 API 결과 중 요청 종목이 포함된 경우만 반환합니다."
        );

        return new EventTimelineContextDto(
                new EventTimelineContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        EventTimelineContextDto.DataFreshness.INTRADAY_SNAPSHOT,
                        cautions
                ),
                new EventTimelineContextDto.StockIdentityDto(normalizedStockCode, stockName(viEvents, limitPriceCaptures)),
                new EventTimelineContextDto.TimeRangeDto(normalizedDate, normalizedHour),
                viEvents,
                newsTitleEvents,
                List.copyOf(limitPriceCaptures),
                correlationHints(viEvents, newsTitleEvents),
                cautions
        );
    }

    private KisFhkst130000C0CaptureUpLowPriceRequest captureRequest(String priceClassCode) {
        return new KisFhkst130000C0CaptureUpLowPriceRequest("J", "11300", priceClassCode, "0", "0000", "", "", "", "", "");
    }

    private List<EventTimelineContextDto.ViEventDto> viEvents(
            String stockCode,
            List<KisFhpst01390000ViStatusResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .filter(output -> stockCode.equals(output.mkscShrnIscd()))
                .map(output -> new EventTimelineContextDto.ViEventDto(
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        output.bsopDate(),
                        output.cntgViHour(),
                        output.viCnclHour(),
                        output.viClsCode(),
                        output.viKindCode(),
                        decimal(output.viPrc()),
                        decimal(output.viStndPrc()),
                        decimal(output.viDprt()),
                        decimal(output.viDmcStndPrc()),
                        decimal(output.viDmcDprt()),
                        number(output.viCount())
                ))
                .toList();
    }

    private List<EventTimelineContextDto.NewsTitleEventDto> newsEvents(
            String stockCode,
            List<KisFhkst01011800NewsTitleResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .filter(output -> newsStockCodes(output).contains(stockCode))
                .map(output -> new EventTimelineContextDto.NewsTitleEventDto(
                        output.cnttUsiqSrno(),
                        output.newsOferEntpCode(),
                        output.dorg(),
                        output.dataDt(),
                        output.dataTm(),
                        output.htsPbntTitlCntt(),
                        output.newsLrdvCode(),
                        newsStockCodes(output)
                ))
                .toList();
    }

    private List<String> newsStockCodes(KisFhkst01011800NewsTitleResponse.Output output) {
        List<String> stockCodes = new ArrayList<>();
        addIfNotBlank(stockCodes, output.iscd1());
        addIfNotBlank(stockCodes, output.iscd2());
        addIfNotBlank(stockCodes, output.iscd3());
        addIfNotBlank(stockCodes, output.iscd4());
        addIfNotBlank(stockCodes, output.iscd5());
        return List.copyOf(stockCodes);
    }

    private List<EventTimelineContextDto.LimitPriceCaptureDto> limitPriceCaptures(
            String stockCode,
            String captureType,
            List<KisFhkst130000C0CaptureUpLowPriceResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .filter(output -> stockCode.equals(output.mkscShrnIscd()))
                .map(output -> new EventTimelineContextDto.LimitPriceCaptureDto(
                        captureType,
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        decimal(output.prdyCtrt()),
                        output.prdyVrssSign(),
                        number(output.acmlVol()),
                        number(output.totalAskpRsqn()),
                        number(output.totalBidpRsqn()),
                        decimal(output.stckLlam()),
                        decimal(output.stckMxpr()),
                        decimal(output.prdyVrssVolRate())
                ))
                .toList();
    }

    private List<EventTimelineContextDto.EventCorrelationHintDto> correlationHints(
            List<EventTimelineContextDto.ViEventDto> viEvents,
            List<EventTimelineContextDto.NewsTitleEventDto> newsEvents
    ) {
        List<EventTimelineContextDto.EventCorrelationHintDto> hints = new ArrayList<>();
        if (!viEvents.isEmpty() && !newsEvents.isEmpty()) {
            EventTimelineContextDto.ViEventDto viEvent = viEvents.getFirst();
            hints.add(new EventTimelineContextDto.EventCorrelationHintDto(
                    "VI_AND_NEWS",
                    viEvent.date(),
                    viEvent.triggerTime(),
                    "VI 발동과 종목 관련 뉴스/공시 제목이 함께 조회되었습니다."
            ));
        }
        return List.copyOf(hints);
    }

    private String stockName(
            List<EventTimelineContextDto.ViEventDto> viEvents,
            List<EventTimelineContextDto.LimitPriceCaptureDto> limitPriceCaptures
    ) {
        if (!viEvents.isEmpty()) {
            return viEvents.getFirst().stockName();
        }
        if (!limitPriceCaptures.isEmpty()) {
            return limitPriceCaptures.getFirst().stockName();
        }
        return null;
    }

    private EventTimelineContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new EventTimelineContextDto.KisApiCallSummary(
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
            return now().format(DATE_FORMAT);
        }
        return value.trim().replace("-", "");
    }

    private String normalizeHour(String value) {
        if (isBlank(value)) {
            return DEFAULT_INPUT_HOUR;
        }
        return value.trim().replace(":", "");
    }

    private String newsDate(String date) {
        return "00" + date;
    }

    private String newsHour(String hour) {
        return "0000" + hour;
    }

    private void addIfNotBlank(List<String> values, String value) {
        if (!isBlank(value)) {
            values.add(value);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
