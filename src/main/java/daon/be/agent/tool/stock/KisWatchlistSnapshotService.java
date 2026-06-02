package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst11300006IntstockMultpriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst11300006IntstockMultpriceResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankRequest;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankResponse;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankRequest;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankResponse;
import daon.be.agent.tool.stock.dto.WatchlistSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KisWatchlistSnapshotService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_WATCHLIST_SNAPSHOT";
    private static final String DEFAULT_WATCHLIST_ID = "request";
    private static final String DEFAULT_MARKET = "J";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";
    private static final int MAX_MULTPRICE_STOCK_COUNT = 30;

    private final KisQuotationApiClient quotationApiClient;
    private final KisRankingApiClient rankingApiClient;

    public WatchlistSnapshotDto getWatchlistSnapshot(String watchlistId, String stockCodes, String market) {
        String normalizedWatchlistId = defaultIfBlank(watchlistId, DEFAULT_WATCHLIST_ID);
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        List<String> normalizedStockCodes = parseStockCodes(stockCodes);
        OffsetDateTime requestedAt = now();
        List<WatchlistSnapshotDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime multpriceCalledAt = now();
        KisFhkst11300006IntstockMultpriceResponse multpriceResponse = quotationApiClient.inquireIntstockMultprice(
                multpriceRequest(normalizedMarket, normalizedStockCodes)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST11300006_INTSTOCK_MULTPRICE, multpriceCalledAt, multpriceResponse.rtCd(), multpriceResponse.msg1()));

        OffsetDateTime volumeRankCalledAt = now();
        KisFhpst01710000VolumeRankResponse volumeRankResponse = rankingApiClient.inquireVolumeRank(
                new KisFhpst01710000VolumeRankRequest(normalizedMarket, "20171", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01710000_VOLUME_RANK, volumeRankCalledAt, volumeRankResponse.rtCd(), volumeRankResponse.msg1()));

        OffsetDateTime fluctuationRankCalledAt = now();
        KisFhpst01700000FluctuationRankResponse fluctuationRankResponse = rankingApiClient.inquireFluctuationRank(
                new KisFhpst01700000FluctuationRankRequest(ZERO, normalizedMarket, "20170", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01700000_FLUCTUATION_RANK, fluctuationRankCalledAt, fluctuationRankResponse.rtCd(), fluctuationRankResponse.msg1()));

        validateSuccess(KisEndpoint.FHKST11300006_INTSTOCK_MULTPRICE, multpriceResponse.rtCd(), multpriceResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01710000_VOLUME_RANK, volumeRankResponse.rtCd(), volumeRankResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01700000_FLUCTUATION_RANK, fluctuationRankResponse.rtCd(), fluctuationRankResponse.msg1());

        Map<String, List<WatchlistSnapshotDto.RankSignalDto>> rankSignals = rankSignals(volumeRankResponse.output(), fluctuationRankResponse.output());
        List<WatchlistSnapshotDto.WatchlistStockSnapshotDto> stocks = stockSnapshots(multpriceResponse.output(), rankSignals);
        List<String> limitations = List.of(
                "초기 구현은 DB 관심그룹이 아니라 요청 파라미터의 종목코드 목록을 사용합니다.",
                "멀티시세 API는 1회 최대 30개 종목까지만 조회합니다.",
                "거래량/등락률 순위 매칭은 이상 흐름 후보 표시용이며 상세 원인 분석은 개별 domain tool로 보강해야 합니다."
        );

        return new WatchlistSnapshotDto(
                new WatchlistSnapshotDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        WatchlistSnapshotDto.DataFreshness.INTRADAY_WATCHLIST_SNAPSHOT,
                        limitations
                ),
                normalizedWatchlistId,
                stocks,
                alertCandidates(stocks, rankSignals),
                limitations
        );
    }

    private List<String> parseStockCodes(String stockCodes) {
        if (isBlank(stockCodes)) {
            throw new IllegalArgumentException("관심종목 종목코드는 1개 이상 필요합니다.");
        }
        Set<String> parsed = new LinkedHashSet<>();
        for (String stockCode : stockCodes.split("[,\\s]+")) {
            if (!isBlank(stockCode)) {
                parsed.add(stockCode.trim());
            }
        }
        if (parsed.isEmpty()) {
            throw new IllegalArgumentException("관심종목 종목코드는 1개 이상 필요합니다.");
        }
        if (parsed.size() > MAX_MULTPRICE_STOCK_COUNT) {
            throw new IllegalArgumentException("관심종목 멀티시세는 1회 최대 30개 종목까지만 조회합니다.");
        }
        return List.copyOf(parsed);
    }

    private KisFhkst11300006IntstockMultpriceRequest multpriceRequest(String market, List<String> stockCodes) {
        String[] markets = new String[MAX_MULTPRICE_STOCK_COUNT];
        String[] codes = new String[MAX_MULTPRICE_STOCK_COUNT];
        for (int index = 0; index < stockCodes.size(); index++) {
            markets[index] = market;
            codes[index] = stockCodes.get(index);
        }
        return new KisFhkst11300006IntstockMultpriceRequest(
                markets[0], codes[0],
                markets[1], codes[1],
                markets[2], codes[2],
                markets[3], codes[3],
                markets[4], codes[4],
                markets[5], codes[5],
                markets[6], codes[6],
                markets[7], codes[7],
                markets[8], codes[8],
                markets[9], codes[9],
                markets[10], codes[10],
                markets[11], codes[11],
                markets[12], codes[12],
                markets[13], codes[13],
                markets[14], codes[14],
                markets[15], codes[15],
                markets[16], codes[16],
                markets[17], codes[17],
                markets[18], codes[18],
                markets[19], codes[19],
                markets[20], codes[20],
                markets[21], codes[21],
                markets[22], codes[22],
                markets[23], codes[23],
                markets[24], codes[24],
                markets[25], codes[25],
                markets[26], codes[26],
                markets[27], codes[27],
                markets[28], codes[28],
                markets[29], codes[29]
        );
    }

    private Map<String, List<WatchlistSnapshotDto.RankSignalDto>> rankSignals(
            List<KisFhpst01710000VolumeRankResponse.Output> volumeRankOutputs,
            List<KisFhpst01700000FluctuationRankResponse.Output> fluctuationRankOutputs
    ) {
        Map<String, List<WatchlistSnapshotDto.RankSignalDto>> signals = new LinkedHashMap<>();
        if (volumeRankOutputs != null) {
            for (KisFhpst01710000VolumeRankResponse.Output output : volumeRankOutputs) {
                addSignal(
                        signals,
                        output.mkscShrnIscd(),
                        new WatchlistSnapshotDto.RankSignalDto(
                                WatchlistSnapshotDto.WatchlistSignal.VOLUME_RANK,
                                integer(output.dataRank()),
                                decimal(output.volInrt()),
                                "거래량증가율"
                        )
                );
            }
        }
        if (fluctuationRankOutputs != null) {
            for (KisFhpst01700000FluctuationRankResponse.Output output : fluctuationRankOutputs) {
                addSignal(
                        signals,
                        output.stckShrnIscd(),
                        new WatchlistSnapshotDto.RankSignalDto(
                                WatchlistSnapshotDto.WatchlistSignal.FLUCTUATION_RANK,
                                integer(output.dataRank()),
                                decimal(output.prdyCtrt()),
                                "전일대비율"
                        )
                );
            }
        }
        return signals;
    }

    private void addSignal(
            Map<String, List<WatchlistSnapshotDto.RankSignalDto>> signals,
            String stockCode,
            WatchlistSnapshotDto.RankSignalDto signal
    ) {
        if (isBlank(stockCode)) {
            return;
        }
        signals.computeIfAbsent(stockCode, ignored -> new ArrayList<>()).add(signal);
    }

    private List<WatchlistSnapshotDto.WatchlistStockSnapshotDto> stockSnapshots(
            List<KisFhkst11300006IntstockMultpriceResponse.Output> outputs,
            Map<String, List<WatchlistSnapshotDto.RankSignalDto>> rankSignals
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new WatchlistSnapshotDto.WatchlistStockSnapshotDto(
                        output.interShrnIscd(),
                        output.interKorIsnm(),
                        output.kospiKosdaqClsName(),
                        output.mrktTrtmClsName(),
                        output.hourClsCode(),
                        decimal(output.inter2Prpr()),
                        decimal(output.inter2PrdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        decimal(output.acmlTrPbmn()),
                        decimal(output.inter2Oprc()),
                        decimal(output.inter2Hgpr()),
                        decimal(output.inter2Lwpr()),
                        decimal(output.inter2Askp()),
                        decimal(output.inter2Bidp()),
                        number(output.totalAskpRsqn()),
                        number(output.totalBidpRsqn()),
                        signalsOnly(rankSignals.get(output.interShrnIscd()))
                ))
                .toList();
    }

    private List<WatchlistSnapshotDto.WatchlistAlertCandidateDto> alertCandidates(
            List<WatchlistSnapshotDto.WatchlistStockSnapshotDto> stocks,
            Map<String, List<WatchlistSnapshotDto.RankSignalDto>> rankSignals
    ) {
        return stocks.stream()
                .filter(stock -> rankSignals.containsKey(stock.stockCode()))
                .map(stock -> {
                    List<WatchlistSnapshotDto.RankSignalDto> signals = rankSignals.get(stock.stockCode());
                    return new WatchlistSnapshotDto.WatchlistAlertCandidateDto(
                            stock.stockCode(),
                            stock.stockName(),
                            stock.currentPrice(),
                            stock.changeRate(),
                            stock.accumulatedVolume(),
                            stock.tradingValue(),
                            signals.size(),
                            signalsOnly(signals),
                            List.copyOf(signals)
                    );
                })
                .sorted((first, second) -> Integer.compare(second.signalCount(), first.signalCount()))
                .toList();
    }

    private List<WatchlistSnapshotDto.WatchlistSignal> signalsOnly(List<WatchlistSnapshotDto.RankSignalDto> signals) {
        if (signals == null) {
            return List.of();
        }
        return signals.stream()
                .map(WatchlistSnapshotDto.RankSignalDto::signal)
                .toList();
    }

    private WatchlistSnapshotDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new WatchlistSnapshotDto.KisApiCallSummary(
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

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
