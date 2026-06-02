package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.marketcap.KisFhpst01740000MarketCapRequest;
import daon.be.agent.data.source.kis.ranking.marketcap.KisFhpst01740000MarketCapResponse;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01750000FinanceRatioRankRequest;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01750000FinanceRatioRankResponse;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01790000MarketValueRankRequest;
import daon.be.agent.data.source.kis.ranking.valuation.KisFhpst01790000MarketValueRankResponse;
import daon.be.agent.tool.stock.dto.ScreenedStockCandidatesDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisScreenedStockCandidatesServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisRankingApiClient rankingApiClient = mock(KisRankingApiClient.class);
    private final KisScreenedStockCandidatesService service = new KisScreenedStockCandidatesService(rankingApiClient);

    @Test
    void getScreenedStockCandidatesCombinesFinancialValueAndMarketCapRanks() throws Exception {
        when(rankingApiClient.inquireFinanceRatioRank(
                new KisFhpst01750000FinanceRatioRankRequest("0", "J", "20175", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "data_rank": "1",
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.45",
                      "acml_vol": "12345678",
                      "cptl_ntin_rate": "8.5",
                      "bis": "78.1",
                      "lblt_rate": "25.2",
                      "grs": "10.4",
                      "bsop_prfi_inrt": "12.0",
                      "ntin_inrt": "9.3"
                    }
                  ]
                }
                """, KisFhpst01750000FinanceRatioRankResponse.class));
        when(rankingApiClient.inquireMarketValueRank(
                new KisFhpst01790000MarketValueRankRequest("0", "J", "20179", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "data_rank": "1",
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.45",
                      "acml_vol": "12345678",
                      "per": "12.4",
                      "pbr": "1.3",
                      "eps": "5645",
                      "ebitda": "850000"
                    },
                    {
                      "data_rank": "2",
                      "mksc_shrn_iscd": "000660",
                      "hts_kor_isnm": "SK하이닉스",
                      "stck_prpr": "208000",
                      "prdy_vrss": "-1000",
                      "prdy_vrss_sign": "5",
                      "prdy_ctrt": "-0.48",
                      "acml_vol": "4567890",
                      "per": "15.2",
                      "pbr": "1.8",
                      "eps": "13684"
                    }
                  ]
                }
                """, KisFhpst01790000MarketValueRankResponse.class));
        when(rankingApiClient.inquireMarketCap(
                new KisFhpst01740000MarketCapRequest("0", "J", "20174", "0", "0000", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "data_rank": "1",
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.45",
                      "acml_vol": "12345678",
                      "lstn_stcn": "5969782550",
                      "stck_avls": "417884778500000",
                      "mrkt_whol_avls_rlim": "18.5"
                    }
                  ]
                }
                """, KisFhpst01740000MarketCapResponse.class));

        ScreenedStockCandidatesDto result = service.getScreenedStockCandidates("J");

        assertThat(result.meta().toolName()).isEqualTo("GET_SCREENED_STOCK_CANDIDATES");
        assertThat(result.meta().kisApiCalls()).hasSize(3);
        assertThat(result.market()).isEqualTo("J");
        assertThat(result.financialRatioRankCandidates()).hasSize(1);
        assertThat(result.financialRatioRankCandidates().getFirst().category()).isEqualTo(ScreenedStockCandidatesDto.ScreeningCategory.FINANCIAL_RATIO);
        assertThat(result.financialRatioRankCandidates().getFirst().primaryMetricName()).isEqualTo("총자본순이익율");
        assertThat(result.financialRatioRankCandidates().getFirst().primaryMetricValue()).isEqualByComparingTo(new BigDecimal("8.5"));
        assertThat(result.marketValueRankCandidates()).hasSize(2);
        assertThat(result.marketValueRankCandidates().getFirst().per()).isEqualByComparingTo(new BigDecimal("12.4"));
        assertThat(result.marketCapRankCandidates().getFirst().marketCap()).isEqualByComparingTo(new BigDecimal("417884778500000"));
        assertThat(result.mergedCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.mergedCandidates().getFirst().signalCount()).isEqualTo(3);
        assertThat(result.mergedCandidates().getFirst().sourceCategories()).containsExactly(
                ScreenedStockCandidatesDto.ScreeningCategory.FINANCIAL_RATIO,
                ScreenedStockCandidatesDto.ScreeningCategory.MARKET_VALUE,
                ScreenedStockCandidatesDto.ScreeningCategory.MARKET_CAP
        );
        assertThat(result.filteringRules()).contains(
                "API 순위를 그대로 추천하지 않고 관리/주의 종목 제외, 거래대금, 최근 급등락, 재무 데이터 품질 확인을 후속 검증합니다."
        );

        verify(rankingApiClient).inquireFinanceRatioRank(new KisFhpst01750000FinanceRatioRankRequest("0", "J", "20175", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0"));
        verify(rankingApiClient).inquireMarketValueRank(new KisFhpst01790000MarketValueRankRequest("0", "J", "20179", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0"));
        verify(rankingApiClient).inquireMarketCap(new KisFhpst01740000MarketCapRequest("0", "J", "20174", "0", "0000", "0", "0", "0", "0"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
