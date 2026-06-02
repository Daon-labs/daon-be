package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst11300006IntstockMultpriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst11300006IntstockMultpriceResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankRequest;
import daon.be.agent.data.source.kis.ranking.fluctuation.KisFhpst01700000FluctuationRankResponse;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankRequest;
import daon.be.agent.data.source.kis.ranking.volume.KisFhpst01710000VolumeRankResponse;
import daon.be.agent.tool.stock.dto.WatchlistSnapshotDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisWatchlistSnapshotServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisRankingApiClient rankingApiClient = mock(KisRankingApiClient.class);
    private final KisWatchlistSnapshotService service = new KisWatchlistSnapshotService(quotationApiClient, rankingApiClient);

    @Test
    void getWatchlistSnapshotCombinesMultpriceWithVolumeAndFluctuationRankSignals() throws Exception {
        when(quotationApiClient.inquireIntstockMultprice(new KisFhkst11300006IntstockMultpriceRequest(
                "J", "005930",
                "J", "000660",
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null
        ))).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "kospi_kosdaq_cls_name": "KOSPI",
                      "mrkt_trtm_cls_name": "정상",
                      "hour_cls_code": "0",
                      "inter_shrn_iscd": "005930",
                      "inter_kor_isnm": "삼성전자",
                      "inter2_prpr": "70000",
                      "inter2_prdy_vrss": "1000",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.45",
                      "acml_vol": "12345678",
                      "inter2_oprc": "69000",
                      "inter2_hgpr": "70500",
                      "inter2_lwpr": "68800",
                      "inter2_askp": "70100",
                      "inter2_bidp": "70000",
                      "total_askp_rsqn": "100000",
                      "total_bidp_rsqn": "120000",
                      "acml_tr_pbmn": "850000000000"
                    },
                    {
                      "kospi_kosdaq_cls_name": "KOSPI",
                      "mrkt_trtm_cls_name": "정상",
                      "hour_cls_code": "0",
                      "inter_shrn_iscd": "000660",
                      "inter_kor_isnm": "SK하이닉스",
                      "inter2_prpr": "208000",
                      "inter2_prdy_vrss": "-1000",
                      "prdy_vrss_sign": "5",
                      "prdy_ctrt": "-0.48",
                      "acml_vol": "4567890",
                      "acml_tr_pbmn": "940000000000"
                    }
                  ]
                }
                """, KisFhkst11300006IntstockMultpriceResponse.class));
        when(rankingApiClient.inquireVolumeRank(new KisFhpst01710000VolumeRankRequest("J", "20171", "0000", "0", "0", "0", "0", "0", "0", "0")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "Output": [
                            {
                              "hts_kor_isnm": "삼성전자",
                              "mksc_shrn_iscd": "005930",
                              "data_rank": "3",
                              "stck_prpr": "70000",
                              "prdy_vrss_sign": "2",
                              "prdy_vrss": "1000",
                              "prdy_ctrt": "1.45",
                              "acml_vol": "12345678",
                              "vol_inrt": "180.5",
                              "acml_tr_pbmn": "850000000000"
                            }
                          ]
                        }
                        """, KisFhpst01710000VolumeRankResponse.class));
        when(rankingApiClient.inquireFluctuationRank(new KisFhpst01700000FluctuationRankRequest("0", "J", "20170", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": [
                            {
                              "stck_shrn_iscd": "005930",
                              "data_rank": "5",
                              "hts_kor_isnm": "삼성전자",
                              "stck_prpr": "70000",
                              "prdy_vrss": "1000",
                              "prdy_vrss_sign": "2",
                              "prdy_ctrt": "1.45",
                              "acml_vol": "12345678"
                            }
                          ]
                        }
                        """, KisFhpst01700000FluctuationRankResponse.class));

        WatchlistSnapshotDto result = service.getWatchlistSnapshot("main", "005930,000660", "J");

        assertThat(result.meta().toolName()).isEqualTo("GET_WATCHLIST_SNAPSHOT");
        assertThat(result.meta().kisApiCalls()).hasSize(3);
        assertThat(result.watchlistId()).isEqualTo("main");
        assertThat(result.stocks()).hasSize(2);
        assertThat(result.stocks().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.stocks().getFirst().currentPrice()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.alertCandidates()).hasSize(1);
        assertThat(result.alertCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.alertCandidates().getFirst().signalCount()).isEqualTo(2);
        assertThat(result.alertCandidates().getFirst().signals()).containsExactly(
                WatchlistSnapshotDto.WatchlistSignal.VOLUME_RANK,
                WatchlistSnapshotDto.WatchlistSignal.FLUCTUATION_RANK
        );
        assertThat(result.limitations()).contains("초기 구현은 DB 관심그룹이 아니라 요청 파라미터의 종목코드 목록을 사용합니다.");

        verify(quotationApiClient).inquireIntstockMultprice(new KisFhkst11300006IntstockMultpriceRequest(
                "J", "005930",
                "J", "000660",
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null
        ));
        verify(rankingApiClient).inquireVolumeRank(new KisFhpst01710000VolumeRankRequest("J", "20171", "0000", "0", "0", "0", "0", "0", "0", "0"));
        verify(rankingApiClient).inquireFluctuationRank(new KisFhpst01700000FluctuationRankRequest("0", "J", "20170", "0000", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
