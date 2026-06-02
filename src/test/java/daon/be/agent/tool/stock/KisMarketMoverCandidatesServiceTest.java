package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisMarketMoverCandidatesServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisRankingApiClient rankingApiClient = mock(KisRankingApiClient.class);
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisMarketMoverCandidatesService service = new KisMarketMoverCandidatesService(rankingApiClient, quotationApiClient);

    @Test
    void getMarketMoverCandidatesMergesDuplicatedRankSignalsAsCandidatesNotCauses() throws Exception {
        when(rankingApiClient.inquireVolumeRank(
                new KisFhpst01710000VolumeRankRequest("J", "20171", "0000", "0", "0", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "Output": [
                    {
                      "hts_kor_isnm": "삼성전자",
                      "mksc_shrn_iscd": "005930",
                      "data_rank": "1",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678",
                      "acml_tr_pbmn": "987654321000",
                      "vol_inrt": "250.50"
                    }
                  ]
                }
                """, KisFhpst01710000VolumeRankResponse.class));
        when(rankingApiClient.inquireFluctuationRank(
                new KisFhpst01700000FluctuationRankRequest("0", "J", "20170", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_shrn_iscd": "005930",
                      "data_rank": "2",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678"
                    }
                  ]
                }
                """, KisFhpst01700000FluctuationRankResponse.class));
        when(rankingApiClient.inquireVolumePower(
                new KisFhpst01680000VolumePowerRequest("0", "J", "20168", "0000", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_shrn_iscd": "005930",
                      "data_rank": "3",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678",
                      "tday_rltv": "180.25"
                    }
                  ]
                }
                """, KisFhpst01680000VolumePowerResponse.class));
        when(rankingApiClient.inquireBulkTransNum(
                new KisFhkst190900C0BulkTransNumRequest("0", "J", "11909", "0000", "0", "0", "0", "0", "0000", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "mksc_shrn_iscd": "000660",
                      "data_rank": "1",
                      "hts_kor_isnm": "SK하이닉스",
                      "stck_prpr": "220000",
                      "prdy_vrss": "6000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "2.80",
                      "acml_vol": "3000000",
                      "shnu_cntg_csnu": "120",
                      "seln_cntg_csnu": "70",
                      "ntby_cnqn": "50000"
                    }
                  ]
                }
                """, KisFhkst190900C0BulkTransNumResponse.class));
        when(rankingApiClient.inquireHtsTopView(new KisHhmcm000100C0HtsTopViewRequest()))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output1": [
                            {
                              "mrkt_div_cls_code": "J",
                              "mksc_shrn_iscd": "005930"
                            }
                          ]
                        }
                        """, KisHhmcm000100C0HtsTopViewResponse.class));
        when(rankingApiClient.inquireNearNewHighLow(
                new KisFhpst01870000NearNewHighLowRequest("0", "J", "20187", "0", "0", "0", "0", "0000", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "hts_kor_isnm": "SK하이닉스",
                      "mksc_shrn_iscd": "000660",
                      "stck_prpr": "220000",
                      "prdy_vrss": "6000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "2.80",
                      "acml_vol": "3000000",
                      "new_hgpr": "225000",
                      "hprc_near_rate": "2.27"
                    }
                  ]
                }
                """, KisFhpst01870000NearNewHighLowResponse.class));
        when(quotationApiClient.captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest("J", "11300", "0", "0", "0000", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678",
                      "prdy_vrss_vol_rate": "250.50"
                    }
                  ]
                }
                """, KisFhkst130000C0CaptureUpLowPriceResponse.class));

        MarketMoverCandidatesDto result = service.getMarketMoverCandidates("J");

        assertThat(result.meta().toolName()).isEqualTo("GET_MARKET_MOVER_CANDIDATES");
        assertThat(result.meta().kisApiCalls()).hasSize(7);
        assertThat(result.market()).isEqualTo("J");
        assertThat(result.volumeRankCandidates()).hasSize(1);
        assertThat(result.volumeRankCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.volumeRankCandidates().getFirst().signalValue()).isEqualByComparingTo(new BigDecimal("250.50"));
        assertThat(result.fluctuationRankCandidates().getFirst().rank()).isEqualTo(2);
        assertThat(result.volumePowerCandidates().getFirst().signalValue()).isEqualByComparingTo(new BigDecimal("180.25"));
        assertThat(result.bulkTradeCandidates().getFirst().stockCode()).isEqualTo("000660");
        assertThat(result.htsTopViewedCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.nearHighLowCandidates().getFirst().stockCode()).isEqualTo("000660");
        assertThat(result.limitPriceCandidates().getFirst().category()).isEqualTo(MarketMoverCandidatesDto.MoverCategory.LIMIT_PRICE);
        assertThat(result.mergedCandidates()).hasSize(2);
        assertThat(result.mergedCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.mergedCandidates().getFirst().signalCount()).isEqualTo(5);
        assertThat(result.mergedCandidates().getFirst().sourceCategories()).contains(
                MarketMoverCandidatesDto.MoverCategory.VOLUME_RANK,
                MarketMoverCandidatesDto.MoverCategory.FLUCTUATION_RANK,
                MarketMoverCandidatesDto.MoverCategory.VOLUME_POWER,
                MarketMoverCandidatesDto.MoverCategory.HTS_TOP_VIEW,
                MarketMoverCandidatesDto.MoverCategory.LIMIT_PRICE
        );
        assertThat(result.cautions()).contains("순위 API는 원인 확정 근거가 아니라 상세 분석 대상 후보를 고르는 스캐너입니다.");

        verify(rankingApiClient).inquireVolumeRank(
                new KisFhpst01710000VolumeRankRequest("J", "20171", "0000", "0", "0", "0", "0", "0", "0", "0")
        );
        verify(rankingApiClient).inquireFluctuationRank(
                new KisFhpst01700000FluctuationRankRequest("0", "J", "20170", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
        );
        verify(rankingApiClient).inquireVolumePower(
                new KisFhpst01680000VolumePowerRequest("0", "J", "20168", "0000", "0", "0", "0", "0", "0")
        );
        verify(rankingApiClient).inquireBulkTransNum(
                new KisFhkst190900C0BulkTransNumRequest("0", "J", "11909", "0000", "0", "0", "0", "0", "0000", "0", "0", "0")
        );
        verify(rankingApiClient).inquireHtsTopView(new KisHhmcm000100C0HtsTopViewRequest());
        verify(rankingApiClient).inquireNearNewHighLow(
                new KisFhpst01870000NearNewHighLowRequest("0", "J", "20187", "0", "0", "0", "0", "0000", "0", "0", "0", "0")
        );
        verify(quotationApiClient).captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest("J", "11300", "0", "0", "0000", "0", "0", "0", "0", "0")
        );
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
