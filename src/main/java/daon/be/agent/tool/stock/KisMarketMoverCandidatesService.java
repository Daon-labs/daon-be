package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.attention.KisHhmcm000100C0HtsTopViewRequest;
import daon.be.agent.data.source.kis.ranking.attention.KisHhmcm000100C0HtsTopViewResponse;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankRequest;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankResponse;
import daon.be.agent.data.source.kis.ranking.tradepower.KisFhkst190900C0BulkTransNumRequest;
import daon.be.agent.data.source.kis.ranking.tradepower.KisFhkst190900C0BulkTransNumResponse;
import daon.be.agent.data.source.kis.ranking.tradepower.KisFhpst01680000VolumePowerRequest;
import daon.be.agent.data.source.kis.ranking.tradepower.KisFhpst01680000VolumePowerResponse;
import daon.be.agent.data.source.kis.ranking.trend.KisFhpst01870000NearNewHighLowRequest;
import daon.be.agent.data.source.kis.ranking.trend.KisFhpst01870000NearNewHighLowResponse;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankRequest;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankResponse;
import daon.be.agent.tool.stock.dto.MarketMoverCandidatesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KisMarketMoverCandidatesService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_MARKET_MOVER_CANDIDATES";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";

    private final KisRankingApiClient rankingApiClient;
    private final KisQuotationApiClient quotationApiClient;

    public MarketMoverCandidatesDto getMarketMoverCandidates(String market) {
        String normalizedMarket = defaultIfBlank(market, STOCK_MARKET_CODE);
        OffsetDateTime requestedAt = now();
        List<MarketMoverCandidatesDto.KisApiCallSummary> apiCalls = new ArrayList<>();

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

        OffsetDateTime volumePowerCalledAt = now();
        KisFhpst01680000VolumePowerResponse volumePowerResponse = rankingApiClient.inquireVolumePower(
                new KisFhpst01680000VolumePowerRequest(ZERO, normalizedMarket, "20168", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01680000_VOLUME_POWER, volumePowerCalledAt, volumePowerResponse.rtCd(), volumePowerResponse.msg1()));

        OffsetDateTime bulkTradeCalledAt = now();
        KisFhkst190900C0BulkTransNumResponse bulkTradeResponse = rankingApiClient.inquireBulkTransNum(
                new KisFhkst190900C0BulkTransNumRequest(ZERO, normalizedMarket, "11909", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST190900_C0_BULK_TRANS_NUM, bulkTradeCalledAt, bulkTradeResponse.rtCd(), bulkTradeResponse.msg1()));

        OffsetDateTime htsTopViewCalledAt = now();
        KisHhmcm000100C0HtsTopViewResponse htsTopViewResponse = rankingApiClient.inquireHtsTopView(new KisHhmcm000100C0HtsTopViewRequest());
        apiCalls.add(apiCall(KisEndpoint.HHMCM000100_C0_HTS_TOP_VIEW, htsTopViewCalledAt, htsTopViewResponse.rtCd(), htsTopViewResponse.msg1()));

        OffsetDateTime nearHighLowCalledAt = now();
        KisFhpst01870000NearNewHighLowResponse nearHighLowResponse = rankingApiClient.inquireNearNewHighLow(
                new KisFhpst01870000NearNewHighLowRequest(ZERO, normalizedMarket, "20187", ZERO, ZERO, ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01870000_NEAR_NEW_HIGH_LOW, nearHighLowCalledAt, nearHighLowResponse.rtCd(), nearHighLowResponse.msg1()));

        OffsetDateTime limitPriceCalledAt = now();
        KisFhkst130000C0CaptureUpLowPriceResponse limitPriceResponse = quotationApiClient.captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest(normalizedMarket, "11300", ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, limitPriceCalledAt, limitPriceResponse.rtCd(), limitPriceResponse.msg1()));

        validateSuccess(KisEndpoint.FHPST01710000_VOLUME_RANK, volumeRankResponse.rtCd(), volumeRankResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01700000_FLUCTUATION_RANK, fluctuationRankResponse.rtCd(), fluctuationRankResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01680000_VOLUME_POWER, volumePowerResponse.rtCd(), volumePowerResponse.msg1());
        validateSuccess(KisEndpoint.FHKST190900_C0_BULK_TRANS_NUM, bulkTradeResponse.rtCd(), bulkTradeResponse.msg1());
        validateSuccess(KisEndpoint.HHMCM000100_C0_HTS_TOP_VIEW, htsTopViewResponse.rtCd(), htsTopViewResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01870000_NEAR_NEW_HIGH_LOW, nearHighLowResponse.rtCd(), nearHighLowResponse.msg1());
        validateSuccess(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, limitPriceResponse.rtCd(), limitPriceResponse.msg1());

        List<MarketMoverCandidatesDto.RankedStockDto> volumeRankCandidates = volumeRankCandidates(volumeRankResponse.output());
        List<MarketMoverCandidatesDto.RankedStockDto> fluctuationRankCandidates = fluctuationRankCandidates(fluctuationRankResponse.output());
        List<MarketMoverCandidatesDto.RankedStockDto> volumePowerCandidates = volumePowerCandidates(volumePowerResponse.output());
        List<MarketMoverCandidatesDto.RankedStockDto> bulkTradeCandidates = bulkTradeCandidates(bulkTradeResponse.output());
        List<MarketMoverCandidatesDto.RankedStockDto> htsTopViewedCandidates = htsTopViewedCandidates(normalizedMarket, htsTopViewResponse.output1());
        List<MarketMoverCandidatesDto.RankedStockDto> nearHighLowCandidates = nearHighLowCandidates(nearHighLowResponse.output());
        List<MarketMoverCandidatesDto.RankedStockDto> limitPriceCandidates = limitPriceCandidates(limitPriceResponse.output());
        List<String> cautions = List.of("순위 API는 원인 확정 근거가 아니라 상세 분석 대상 후보를 고르는 스캐너입니다.");

        return new MarketMoverCandidatesDto(
                new MarketMoverCandidatesDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        MarketMoverCandidatesDto.DataFreshness.INTRADAY_RANKING_SNAPSHOT,
                        cautions
                ),
                normalizedMarket,
                volumeRankCandidates,
                fluctuationRankCandidates,
                volumePowerCandidates,
                bulkTradeCandidates,
                htsTopViewedCandidates,
                nearHighLowCandidates,
                limitPriceCandidates,
                mergedCandidates(
                        volumeRankCandidates,
                        fluctuationRankCandidates,
                        volumePowerCandidates,
                        bulkTradeCandidates,
                        htsTopViewedCandidates,
                        nearHighLowCandidates,
                        limitPriceCandidates
                ),
                cautions
        );
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> volumeRankCandidates(List<KisFhpst01710000VolumeRankResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> rankedStock(
                        MarketMoverCandidatesDto.MoverCategory.VOLUME_RANK,
                        output.dataRank(),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        output.stckPrpr(),
                        output.prdyVrss(),
                        output.prdyCtrt(),
                        output.prdyVrssSign(),
                        output.acmlVol(),
                        output.acmlTrPbmn(),
                        output.volInrt(),
                        "거래량증가율"
                ))
                .toList();
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> fluctuationRankCandidates(List<KisFhpst01700000FluctuationRankResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> rankedStock(
                        MarketMoverCandidatesDto.MoverCategory.FLUCTUATION_RANK,
                        output.dataRank(),
                        output.stckShrnIscd(),
                        output.htsKorIsnm(),
                        output.stckPrpr(),
                        output.prdyVrss(),
                        output.prdyCtrt(),
                        output.prdyVrssSign(),
                        output.acmlVol(),
                        null,
                        output.prdyCtrt(),
                        "전일대비율"
                ))
                .toList();
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> volumePowerCandidates(List<KisFhpst01680000VolumePowerResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> rankedStock(
                        MarketMoverCandidatesDto.MoverCategory.VOLUME_POWER,
                        output.dataRank(),
                        output.stckShrnIscd(),
                        output.htsKorIsnm(),
                        output.stckPrpr(),
                        output.prdyVrss(),
                        output.prdyCtrt(),
                        output.prdyVrssSign(),
                        output.acmlVol(),
                        null,
                        output.tdayRltv(),
                        "체결강도"
                ))
                .toList();
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> bulkTradeCandidates(List<KisFhkst190900C0BulkTransNumResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> rankedStock(
                        MarketMoverCandidatesDto.MoverCategory.BULK_TRADE,
                        output.dataRank(),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        output.stckPrpr(),
                        output.prdyVrss(),
                        output.prdyCtrt(),
                        output.prdyVrssSign(),
                        output.acmlVol(),
                        null,
                        output.ntbyCnqn(),
                        "순매수 체결량"
                ))
                .toList();
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> htsTopViewedCandidates(
            String market,
            List<KisHhmcm000100C0HtsTopViewResponse.Output1> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        List<MarketMoverCandidatesDto.RankedStockDto> candidates = new ArrayList<>();
        int rank = 1;
        for (KisHhmcm000100C0HtsTopViewResponse.Output1 output : outputs) {
            if (!market.equals(output.mrktDivClsCode())) {
                continue;
            }
            candidates.add(new MarketMoverCandidatesDto.RankedStockDto(
                    MarketMoverCandidatesDto.MoverCategory.HTS_TOP_VIEW,
                    rank++,
                    output.mkscShrnIscd(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "HTS조회상위"
            ));
        }
        return List.copyOf(candidates);
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> nearHighLowCandidates(List<KisFhpst01870000NearNewHighLowResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        List<MarketMoverCandidatesDto.RankedStockDto> candidates = new ArrayList<>();
        int rank = 1;
        for (KisFhpst01870000NearNewHighLowResponse.Output output : outputs) {
            candidates.add(rankedStock(
                    MarketMoverCandidatesDto.MoverCategory.NEAR_HIGH_LOW,
                    Integer.toString(rank++),
                    output.mkscShrnIscd(),
                    output.htsKorIsnm(),
                    output.stckPrpr(),
                    output.prdyVrss(),
                    output.prdyCtrt(),
                    output.prdyVrssSign(),
                    output.acmlVol(),
                    null,
                    firstNonBlank(output.hprcNearRate(), output.lwprNearRate()),
                    output.hprcNearRate() == null ? "저가 근접 비율" : "고가 근접 비율"
            ));
        }
        return List.copyOf(candidates);
    }

    private List<MarketMoverCandidatesDto.RankedStockDto> limitPriceCandidates(List<KisFhkst130000C0CaptureUpLowPriceResponse.Output> outputs) {
        if (outputs == null) {
            return List.of();
        }
        List<MarketMoverCandidatesDto.RankedStockDto> candidates = new ArrayList<>();
        int rank = 1;
        for (KisFhkst130000C0CaptureUpLowPriceResponse.Output output : outputs) {
            candidates.add(rankedStock(
                    MarketMoverCandidatesDto.MoverCategory.LIMIT_PRICE,
                    Integer.toString(rank++),
                    output.mkscShrnIscd(),
                    output.htsKorIsnm(),
                    output.stckPrpr(),
                    output.prdyVrss(),
                    output.prdyCtrt(),
                    output.prdyVrssSign(),
                    output.acmlVol(),
                    null,
                    output.prdyVrssVolRate(),
                    "전일대비거래량비율"
            ));
        }
        return List.copyOf(candidates);
    }

    @SafeVarargs
    private List<MarketMoverCandidatesDto.MarketMoverCandidateSummaryDto> mergedCandidates(
            List<MarketMoverCandidatesDto.RankedStockDto>... candidateGroups
    ) {
        Map<String, CandidateAccumulator> accumulators = new LinkedHashMap<>();
        for (List<MarketMoverCandidatesDto.RankedStockDto> candidates : candidateGroups) {
            for (MarketMoverCandidatesDto.RankedStockDto candidate : candidates) {
                if (isBlank(candidate.stockCode())) {
                    continue;
                }
                accumulators.computeIfAbsent(candidate.stockCode(), CandidateAccumulator::new).merge(candidate);
            }
        }
        return accumulators.values().stream()
                .map(CandidateAccumulator::toDto)
                .sorted((first, second) -> Integer.compare(second.signalCount(), first.signalCount()))
                .toList();
    }

    private MarketMoverCandidatesDto.RankedStockDto rankedStock(
            MarketMoverCandidatesDto.MoverCategory category,
            String rank,
            String stockCode,
            String stockName,
            String currentPrice,
            String change,
            String changeRate,
            String changeSign,
            String volume,
            String tradingValue,
            String signalValue,
            String signalLabel
    ) {
        return new MarketMoverCandidatesDto.RankedStockDto(
                category,
                integer(rank),
                stockCode,
                stockName,
                decimal(currentPrice),
                decimal(change),
                decimal(changeRate),
                changeSign,
                number(volume),
                decimal(tradingValue),
                decimal(signalValue),
                signalLabel
        );
    }

    private MarketMoverCandidatesDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new MarketMoverCandidatesDto.KisApiCallSummary(
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

    private String firstNonBlank(String first, String second) {
        if (!isBlank(first)) {
            return first;
        }
        return isBlank(second) ? null : second;
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

    private static class CandidateAccumulator {
        private final String stockCode;
        private String stockName;
        private BigDecimal currentPrice;
        private BigDecimal changeRate;
        private Long volume;
        private final List<MarketMoverCandidatesDto.MoverCategory> sourceCategories = new ArrayList<>();

        private CandidateAccumulator(String stockCode) {
            this.stockCode = stockCode;
        }

        private void merge(MarketMoverCandidatesDto.RankedStockDto candidate) {
            if (stockName == null) {
                stockName = candidate.stockName();
            }
            if (currentPrice == null) {
                currentPrice = candidate.currentPrice();
            }
            if (changeRate == null) {
                changeRate = candidate.changeRate();
            }
            if (volume == null) {
                volume = candidate.volume();
            }
            if (!sourceCategories.contains(candidate.category())) {
                sourceCategories.add(candidate.category());
            }
        }

        private MarketMoverCandidatesDto.MarketMoverCandidateSummaryDto toDto() {
            return new MarketMoverCandidatesDto.MarketMoverCandidateSummaryDto(
                    stockCode,
                    stockName,
                    currentPrice,
                    changeRate,
                    volume,
                    sourceCategories.size(),
                    List.copyOf(sourceCategories)
            );
        }
    }
}
