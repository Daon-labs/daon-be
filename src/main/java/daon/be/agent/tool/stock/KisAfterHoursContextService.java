package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02340000OvertimeFluctuationRequest;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02340000OvertimeFluctuationResponse;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02350000OvertimeVolumeRequest;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02350000OvertimeVolumeResponse;
import daon.be.agent.tool.stock.dto.AfterHoursContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KisAfterHoursContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_AFTER_HOURS_CONTEXT";
    private static final String DEFAULT_MARKET = "J";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";
    private static final String DEFAULT_INPUT_HOUR = "153000";
    private static final String OVERTIME_VOLUME_SCREEN_CODE = "20235";
    private static final String OVERTIME_FLUCTUATION_SCREEN_CODE = "20234";
    private static final String OVERTIME_FLUCTUATION_SORT_CODE = "2";

    private final KisRankingApiClient rankingApiClient;
    private final KisQuotationApiClient quotationApiClient;

    public AfterHoursContextDto getAfterHoursContext(String tradingDate, String market, String inputHour) {
        String normalizedDate = normalizeDate(tradingDate);
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        String normalizedHour = normalizeHour(inputHour);
        OffsetDateTime requestedAt = now();
        List<AfterHoursContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime volumeCalledAt = now();
        KisFhpst02350000OvertimeVolumeResponse volumeResponse = rankingApiClient.inquireOvertimeVolume(
                new KisFhpst02350000OvertimeVolumeRequest(
                        normalizedMarket,
                        OVERTIME_VOLUME_SCREEN_CODE,
                        ALL_MARKET_CODE,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST02350000_OVERTIME_VOLUME, volumeCalledAt, volumeResponse.rtCd(), volumeResponse.msg1()));

        OffsetDateTime fluctuationCalledAt = now();
        KisFhpst02340000OvertimeFluctuationResponse fluctuationResponse = rankingApiClient.inquireOvertimeFluctuation(
                new KisFhpst02340000OvertimeFluctuationRequest(
                        normalizedMarket,
                        "",
                        OVERTIME_FLUCTUATION_SCREEN_CODE,
                        ALL_MARKET_CODE,
                        OVERTIME_FLUCTUATION_SORT_CODE,
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST02340000_OVERTIME_FLUCTUATION, fluctuationCalledAt, fluctuationResponse.rtCd(), fluctuationResponse.msg1()));

        OffsetDateTime newsCalledAt = now();
        KisFhkst01011800NewsTitleResponse newsResponse = quotationApiClient.inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", "", "", "", newsDate(normalizedDate), newsHour(normalizedHour), "", "")
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST01011800_NEWS_TITLE, newsCalledAt, newsResponse.rtCd(), newsResponse.msg1()));

        validateSuccess(KisEndpoint.FHPST02350000_OVERTIME_VOLUME, volumeResponse.rtCd(), volumeResponse.msg1());
        validateSuccess(KisEndpoint.FHPST02340000_OVERTIME_FLUCTUATION, fluctuationResponse.rtCd(), fluctuationResponse.msg1());
        validateSuccess(KisEndpoint.FHKST01011800_NEWS_TITLE, newsResponse.rtCd(), newsResponse.msg1());

        List<AfterHoursContextDto.RankedStockDto> volumeCandidates = volumeCandidates(volumeResponse.output2());
        List<AfterHoursContextDto.RankedStockDto> fluctuationCandidates = fluctuationCandidates(fluctuationResponse.output2());
        List<AfterHoursContextDto.NewsTitleEventDto> newsEvents = newsEvents(newsResponse.output());
        List<String> cautions = List.of(
                "시간외 가격은 거래량이 얇으면 왜곡될 수 있으므로 단독 근거로 사용하지 않습니다.",
                "시간외잔량 순위와 시간외예상체결등락률은 허수·취소 주문과 미확정 체결 영향을 줄이기 위해 제외합니다.",
                "뉴스/공시 제목은 장후 이벤트 후보이며 원인 확정 근거가 아닙니다."
        );

        return new AfterHoursContextDto(
                new AfterHoursContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        AfterHoursContextDto.DataFreshness.AFTER_HOURS_RANKING_SNAPSHOT,
                        cautions
                ),
                LocalDate.parse(normalizedDate, DATE_FORMAT),
                normalizedMarket,
                marketSummary(volumeResponse.output1(), fluctuationResponse.output1()),
                volumeCandidates,
                fluctuationCandidates,
                newsEvents,
                mergedCandidates(volumeCandidates, fluctuationCandidates, newsEvents),
                cautions
        );
    }

    private AfterHoursContextDto.AfterHoursMarketSummaryDto marketSummary(
            KisFhpst02350000OvertimeVolumeResponse.Output1 volumeSummary,
            KisFhpst02340000OvertimeFluctuationResponse.Output1 fluctuationSummary
    ) {
        return new AfterHoursContextDto.AfterHoursMarketSummaryDto(
                volumeSummary == null ? null : number(volumeSummary.ovtmUntpExchVol()),
                volumeSummary == null ? null : decimal(volumeSummary.ovtmUntpExchTrPbmn()),
                volumeSummary == null ? null : number(volumeSummary.ovtmUntpKosdaqVol()),
                volumeSummary == null ? null : decimal(volumeSummary.ovtmUntpKosdaqTrPbmn()),
                fluctuationSummary == null ? null : number(fluctuationSummary.ovtmUntpAcmlVol()),
                fluctuationSummary == null ? null : decimal(fluctuationSummary.ovtmUntpAcmlTrPbmn()),
                fluctuationSummary == null ? null : integer(fluctuationSummary.ovtmUntpUplmIssuCnt()),
                fluctuationSummary == null ? null : integer(fluctuationSummary.ovtmUntpAscnIssuCnt()),
                fluctuationSummary == null ? null : integer(fluctuationSummary.ovtmUntpStnrIssuCnt()),
                fluctuationSummary == null ? null : integer(fluctuationSummary.ovtmUntpLslmIssuCnt()),
                fluctuationSummary == null ? null : integer(fluctuationSummary.ovtmUntpDownIssuCnt())
        );
    }

    private List<AfterHoursContextDto.RankedStockDto> volumeCandidates(
            List<KisFhpst02350000OvertimeVolumeResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        List<AfterHoursContextDto.RankedStockDto> candidates = new ArrayList<>();
        int rank = 1;
        for (KisFhpst02350000OvertimeVolumeResponse.Output2 output : outputs) {
            candidates.add(new AfterHoursContextDto.RankedStockDto(
                    AfterHoursContextDto.AfterHoursCategory.OVERTIME_VOLUME,
                    rank++,
                    output.stckShrnIscd(),
                    output.htsKorIsnm(),
                    decimal(output.ovtmUntpPrpr()),
                    decimal(output.ovtmUntpPrdyVrss()),
                    output.ovtmUntpPrdyVrssSign(),
                    decimal(output.ovtmUntpPrdyCtrt()),
                    number(output.ovtmUntpVol()),
                    decimal(output.ovtmVrssAcmlVolRlim()),
                    decimal(output.stckPrpr()),
                    number(output.acmlVol()),
                    "OVERTIME_VOLUME"
            ));
        }
        return List.copyOf(candidates);
    }

    private List<AfterHoursContextDto.RankedStockDto> fluctuationCandidates(
            List<KisFhpst02340000OvertimeFluctuationResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        List<AfterHoursContextDto.RankedStockDto> candidates = new ArrayList<>();
        int rank = 1;
        for (KisFhpst02340000OvertimeFluctuationResponse.Output2 output : outputs) {
            candidates.add(new AfterHoursContextDto.RankedStockDto(
                    AfterHoursContextDto.AfterHoursCategory.OVERTIME_FLUCTUATION,
                    rank++,
                    output.mkscShrnIscd(),
                    output.htsKorIsnm(),
                    decimal(output.ovtmUntpPrpr()),
                    decimal(output.ovtmUntpPrdyVrss()),
                    output.ovtmUntpPrdyVrssSign(),
                    decimal(output.ovtmUntpPrdyCtrt()),
                    number(output.ovtmUntpVol()),
                    decimal(output.ovtmVrssAcmlVolRlim()),
                    decimal(output.stckPrpr()),
                    number(output.acmlVol()),
                    "OVERTIME_FLUCTUATION"
            ));
        }
        return List.copyOf(candidates);
    }

    private List<AfterHoursContextDto.NewsTitleEventDto> newsEvents(
            List<KisFhkst01011800NewsTitleResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new AfterHoursContextDto.NewsTitleEventDto(
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

    private List<AfterHoursContextDto.AfterHoursCandidateSummaryDto> mergedCandidates(
            List<AfterHoursContextDto.RankedStockDto> volumeCandidates,
            List<AfterHoursContextDto.RankedStockDto> fluctuationCandidates,
            List<AfterHoursContextDto.NewsTitleEventDto> newsEvents
    ) {
        Map<String, CandidateAccumulator> accumulators = new LinkedHashMap<>();
        merge(accumulators, volumeCandidates);
        merge(accumulators, fluctuationCandidates);
        for (AfterHoursContextDto.NewsTitleEventDto newsEvent : newsEvents) {
            for (String stockCode : newsEvent.stockCodes()) {
                CandidateAccumulator accumulator = accumulators.get(stockCode);
                if (accumulator != null) {
                    accumulator.relatedNewsCount++;
                }
            }
        }
        return accumulators.values().stream()
                .map(CandidateAccumulator::toDto)
                .sorted((first, second) -> Integer.compare(second.signalCount(), first.signalCount()))
                .toList();
    }

    private void merge(Map<String, CandidateAccumulator> accumulators, List<AfterHoursContextDto.RankedStockDto> candidates) {
        for (AfterHoursContextDto.RankedStockDto candidate : candidates) {
            if (isBlank(candidate.stockCode())) {
                continue;
            }
            accumulators.computeIfAbsent(candidate.stockCode(), CandidateAccumulator::new).merge(candidate);
        }
    }

    private AfterHoursContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new AfterHoursContextDto.KisApiCallSummary(
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

    private Integer integer(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Integer.parseInt(value.trim().replace(",", ""));
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

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
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

    private static class CandidateAccumulator {
        private final String stockCode;
        private String stockName;
        private BigDecimal overtimePrice;
        private BigDecimal changeRate;
        private Long overtimeVolume;
        private int relatedNewsCount;
        private final List<AfterHoursContextDto.AfterHoursCategory> sourceCategories = new ArrayList<>();

        private CandidateAccumulator(String stockCode) {
            this.stockCode = stockCode;
        }

        private void merge(AfterHoursContextDto.RankedStockDto candidate) {
            if (stockName == null) {
                stockName = candidate.stockName();
            }
            if (overtimePrice == null) {
                overtimePrice = candidate.overtimePrice();
            }
            if (changeRate == null) {
                changeRate = candidate.changeRate();
            }
            if (overtimeVolume == null) {
                overtimeVolume = candidate.overtimeVolume();
            }
            if (!sourceCategories.contains(candidate.category())) {
                sourceCategories.add(candidate.category());
            }
        }

        private AfterHoursContextDto.AfterHoursCandidateSummaryDto toDto() {
            return new AfterHoursContextDto.AfterHoursCandidateSummaryDto(
                    stockCode,
                    stockName,
                    overtimePrice,
                    changeRate,
                    overtimeVolume,
                    sourceCategories.size(),
                    relatedNewsCount,
                    List.copyOf(sourceCategories)
            );
        }
    }
}
