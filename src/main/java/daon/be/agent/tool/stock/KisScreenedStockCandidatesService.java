package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.marketcap.KisFhpst01740000MarketCapRequest;
import daon.be.agent.data.source.kis.ranking.marketcap.KisFhpst01740000MarketCapResponse;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01750000FinanceRatioRankRequest;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01750000FinanceRatioRankResponse;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01790000MarketValueRankRequest;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01790000MarketValueRankResponse;
import daon.be.agent.tool.stock.dto.ScreenedStockCandidatesDto;
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
public class KisScreenedStockCandidatesService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_SCREENED_STOCK_CANDIDATES";
    private static final String DEFAULT_MARKET = "J";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";
    private static final String MARKET_CAP_SCREEN_CODE = "20174";
    private static final String FINANCE_RATIO_SCREEN_CODE = "20175";
    private static final String MARKET_VALUE_SCREEN_CODE = "20179";

    private final KisRankingApiClient rankingApiClient;

    public ScreenedStockCandidatesDto getScreenedStockCandidates(String market) {
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        OffsetDateTime requestedAt = now();
        List<ScreenedStockCandidatesDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime financeRatioCalledAt = now();
        KisFhpst01750000FinanceRatioRankResponse financeRatioResponse = rankingApiClient.inquireFinanceRatioRank(
                new KisFhpst01750000FinanceRatioRankRequest(
                        ZERO,
                        normalizedMarket,
                        FINANCE_RATIO_SCREEN_CODE,
                        ALL_MARKET_CODE,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01750000_FINANCE_RATIO_RANK, financeRatioCalledAt, financeRatioResponse.rtCd(), financeRatioResponse.msg1()));

        OffsetDateTime marketValueCalledAt = now();
        KisFhpst01790000MarketValueRankResponse marketValueResponse = rankingApiClient.inquireMarketValueRank(
                new KisFhpst01790000MarketValueRankRequest(
                        ZERO,
                        normalizedMarket,
                        MARKET_VALUE_SCREEN_CODE,
                        ALL_MARKET_CODE,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01790000_MARKET_VALUE_RANK, marketValueCalledAt, marketValueResponse.rtCd(), marketValueResponse.msg1()));

        OffsetDateTime marketCapCalledAt = now();
        KisFhpst01740000MarketCapResponse marketCapResponse = rankingApiClient.inquireMarketCap(
                new KisFhpst01740000MarketCapRequest(
                        ZERO,
                        normalizedMarket,
                        MARKET_CAP_SCREEN_CODE,
                        ZERO,
                        ALL_MARKET_CODE,
                        ZERO,
                        ZERO,
                        ZERO,
                        ZERO
                )
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST01740000_MARKET_CAP, marketCapCalledAt, marketCapResponse.rtCd(), marketCapResponse.msg1()));

        validateSuccess(KisEndpoint.FHPST01750000_FINANCE_RATIO_RANK, financeRatioResponse.rtCd(), financeRatioResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01790000_MARKET_VALUE_RANK, marketValueResponse.rtCd(), marketValueResponse.msg1());
        validateSuccess(KisEndpoint.FHPST01740000_MARKET_CAP, marketCapResponse.rtCd(), marketCapResponse.msg1());

        List<ScreenedStockCandidatesDto.RankedStockDto> financialRatioCandidates = financialRatioCandidates(financeRatioResponse.output());
        List<ScreenedStockCandidatesDto.RankedStockDto> marketValueCandidates = marketValueCandidates(marketValueResponse.output());
        List<ScreenedStockCandidatesDto.RankedStockDto> marketCapCandidates = marketCapCandidates(marketCapResponse.output());
        List<String> filteringRules = List.of(
                "API 순위를 그대로 추천하지 않고 관리/주의 종목 제외, 거래대금, 최근 급등락, 재무 데이터 품질 확인을 후속 검증합니다.",
                "재무비율/시장가치 순위는 최대 30건 제한과 극단값/0값 품질 이슈가 있어 후보 생성 용도로만 사용합니다.",
                "시가총액 상위는 대형주 universe 기준선이며 저평가 또는 매수 추천 근거가 아닙니다."
        );

        return new ScreenedStockCandidatesDto(
                new ScreenedStockCandidatesDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        ScreenedStockCandidatesDto.DataFreshness.DAILY_RANKING_SNAPSHOT,
                        filteringRules
                ),
                normalizedMarket,
                financialRatioCandidates,
                marketValueCandidates,
                marketCapCandidates,
                mergedCandidates(financialRatioCandidates, marketValueCandidates, marketCapCandidates),
                filteringRules
        );
    }

    private List<ScreenedStockCandidatesDto.RankedStockDto> financialRatioCandidates(
            List<KisFhpst01750000FinanceRatioRankResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ScreenedStockCandidatesDto.RankedStockDto(
                        ScreenedStockCandidatesDto.ScreeningCategory.FINANCIAL_RATIO,
                        integer(output.dataRank()),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        "총자본순이익율",
                        decimal(output.cptlNtinRate()),
                        decimal(output.cptlNtinRate()),
                        decimal(output.bis()),
                        decimal(output.lbltRate()),
                        decimal(output.grs()),
                        decimal(output.bsopPrfiInrt()),
                        decimal(output.ntinInrt()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ))
                .toList();
    }

    private List<ScreenedStockCandidatesDto.RankedStockDto> marketValueCandidates(
            List<KisFhpst01790000MarketValueRankResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ScreenedStockCandidatesDto.RankedStockDto(
                        ScreenedStockCandidatesDto.ScreeningCategory.MARKET_VALUE,
                        integer(output.dataRank()),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        "PER",
                        decimal(output.per()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        decimal(output.per()),
                        decimal(output.pbr()),
                        decimal(output.eps()),
                        decimal(output.ebitda()),
                        null,
                        null,
                        null
                ))
                .toList();
    }

    private List<ScreenedStockCandidatesDto.RankedStockDto> marketCapCandidates(
            List<KisFhpst01740000MarketCapResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ScreenedStockCandidatesDto.RankedStockDto(
                        ScreenedStockCandidatesDto.ScreeningCategory.MARKET_CAP,
                        integer(output.dataRank()),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        "시가총액",
                        decimal(output.stckAvls()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        number(output.lstnStcn()),
                        decimal(output.stckAvls()),
                        decimal(output.mrktWholAvlsRlim())
                ))
                .toList();
    }

    @SafeVarargs
    private List<ScreenedStockCandidatesDto.ScreenedStockCandidateDto> mergedCandidates(
            List<ScreenedStockCandidatesDto.RankedStockDto>... candidateGroups
    ) {
        Map<String, CandidateAccumulator> accumulators = new LinkedHashMap<>();
        for (List<ScreenedStockCandidatesDto.RankedStockDto> candidates : candidateGroups) {
            for (ScreenedStockCandidatesDto.RankedStockDto candidate : candidates) {
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

    private ScreenedStockCandidatesDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new ScreenedStockCandidatesDto.KisApiCallSummary(
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

    private static class CandidateAccumulator {
        private final String stockCode;
        private String stockName;
        private BigDecimal currentPrice;
        private BigDecimal changeRate;
        private Long accumulatedVolume;
        private BigDecimal marketCap;
        private final List<ScreenedStockCandidatesDto.ScreeningCategory> sourceCategories = new ArrayList<>();

        private CandidateAccumulator(String stockCode) {
            this.stockCode = stockCode;
        }

        private void merge(ScreenedStockCandidatesDto.RankedStockDto candidate) {
            if (stockName == null) {
                stockName = candidate.stockName();
            }
            if (currentPrice == null) {
                currentPrice = candidate.currentPrice();
            }
            if (changeRate == null) {
                changeRate = candidate.changeRate();
            }
            if (accumulatedVolume == null) {
                accumulatedVolume = candidate.accumulatedVolume();
            }
            if (marketCap == null) {
                marketCap = candidate.marketCap();
            }
            if (!sourceCategories.contains(candidate.category())) {
                sourceCategories.add(candidate.category());
            }
        }

        private ScreenedStockCandidatesDto.ScreenedStockCandidateDto toDto() {
            return new ScreenedStockCandidatesDto.ScreenedStockCandidateDto(
                    stockCode,
                    stockName,
                    currentPrice,
                    changeRate,
                    accumulatedVolume,
                    marketCap,
                    sourceCategories.size(),
                    List.copyOf(sourceCategories)
            );
        }
    }
}
