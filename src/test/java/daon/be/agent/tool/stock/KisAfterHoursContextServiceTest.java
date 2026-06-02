package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02340000OvertimeFluctuationRequest;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02340000OvertimeFluctuationResponse;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02350000OvertimeVolumeRequest;
import daon.be.agent.data.source.kis.ranking.afterhours.KisFhpst02350000OvertimeVolumeResponse;
import daon.be.agent.tool.stock.dto.AfterHoursContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisAfterHoursContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisRankingApiClient rankingApiClient = mock(KisRankingApiClient.class);
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisAfterHoursContextService service = new KisAfterHoursContextService(rankingApiClient, quotationApiClient);

    @Test
    void getAfterHoursContextCombinesOvertimeRanksAndNewsTitleEvents() throws Exception {
        when(rankingApiClient.inquireOvertimeVolume(
                new KisFhpst02350000OvertimeVolumeRequest("J", "20235", "0000", "0", "0", "0", "0", "0", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "ovtm_untp_exch_vol": "1200000",
                    "ovtm_untp_exch_tr_pbmn": "85000000000",
                    "ovtm_untp_kosdaq_vol": "900000",
                    "ovtm_untp_kosdaq_tr_pbmn": "42000000000"
                  },
                  "output2": [
                    {
                      "stck_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "ovtm_untp_prpr": "70500",
                      "ovtm_untp_prdy_vrss": "500",
                      "ovtm_untp_prdy_vrss_sign": "2",
                      "ovtm_untp_prdy_ctrt": "0.71",
                      "ovtm_untp_vol": "150000",
                      "ovtm_vrss_acml_vol_rlim": "1.20",
                      "stck_prpr": "70000",
                      "acml_vol": "12345678"
                    }
                  ]
                }
                """, KisFhpst02350000OvertimeVolumeResponse.class));
        when(rankingApiClient.inquireOvertimeFluctuation(
                new KisFhpst02340000OvertimeFluctuationRequest("J", "", "20234", "0000", "2", "", "", "", "", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "ovtm_untp_uplm_issu_cnt": "3",
                    "ovtm_untp_ascn_issu_cnt": "120",
                    "ovtm_untp_stnr_issu_cnt": "200",
                    "ovtm_untp_lslm_issu_cnt": "1",
                    "ovtm_untp_down_issu_cnt": "90",
                    "ovtm_untp_acml_vol": "2100000",
                    "ovtm_untp_acml_tr_pbmn": "127000000000"
                  },
                  "output2": [
                    {
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "ovtm_untp_prpr": "70500",
                      "ovtm_untp_prdy_vrss": "500",
                      "ovtm_untp_prdy_vrss_sign": "2",
                      "ovtm_untp_prdy_ctrt": "0.71",
                      "ovtm_untp_vol": "150000",
                      "ovtm_vrss_acml_vol_rlim": "1.20",
                      "stck_prpr": "70000",
                      "acml_vol": "12345678"
                    },
                    {
                      "mksc_shrn_iscd": "000660",
                      "hts_kor_isnm": "SK하이닉스",
                      "ovtm_untp_prpr": "215000",
                      "ovtm_untp_prdy_vrss": "7000",
                      "ovtm_untp_prdy_vrss_sign": "2",
                      "ovtm_untp_prdy_ctrt": "3.37",
                      "ovtm_untp_vol": "80000",
                      "ovtm_vrss_acml_vol_rlim": "0.95",
                      "stck_prpr": "208000",
                      "acml_vol": "4567890"
                    }
                  ]
                }
                """, KisFhpst02340000OvertimeFluctuationResponse.class));
        when(quotationApiClient.inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", "", "", "", "0020260602", "0000153000", "", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "cntt_usiq_srno": "N001",
                      "news_ofer_entp_code": "01",
                      "data_dt": "20260602",
                      "data_tm": "163000",
                      "hts_pbnt_titl_cntt": "삼성전자 장후 공급계약 공시",
                      "news_lrdv_code": "02",
                      "dorg": "거래소",
                      "iscd1": "005930"
                    }
                  ]
                }
                """, KisFhkst01011800NewsTitleResponse.class));

        AfterHoursContextDto result = service.getAfterHoursContext("2026-06-02", "J", "153000");

        assertThat(result.meta().toolName()).isEqualTo("GET_AFTER_HOURS_CONTEXT");
        assertThat(result.meta().kisApiCalls()).hasSize(3);
        assertThat(result.tradingDate()).isEqualTo(LocalDate.of(2026, 6, 2));
        assertThat(result.market()).isEqualTo("J");
        assertThat(result.marketSummary().exchangeVolume()).isEqualTo(1200000L);
        assertThat(result.marketSummary().totalOvertimeTradingValue()).isEqualByComparingTo(new BigDecimal("127000000000"));
        assertThat(result.afterHoursVolumeCandidates()).hasSize(1);
        assertThat(result.afterHoursVolumeCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.afterHoursVolumeCandidates().getFirst().signal()).isEqualTo("OVERTIME_VOLUME");
        assertThat(result.afterHoursFluctuationCandidates()).hasSize(2);
        assertThat(result.afterHoursFluctuationCandidates().getFirst().changeRate()).isEqualByComparingTo(new BigDecimal("0.71"));
        assertThat(result.afterHoursNewsTitleEvents().getFirst().title()).isEqualTo("삼성전자 장후 공급계약 공시");
        assertThat(result.afterHoursNewsTitleEvents().getFirst().stockCodes()).containsExactly("005930");
        assertThat(result.mergedCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.mergedCandidates().getFirst().signalCount()).isEqualTo(2);
        assertThat(result.mergedCandidates().getFirst().relatedNewsCount()).isEqualTo(1);
        assertThat(result.cautions()).contains("시간외 가격은 거래량이 얇으면 왜곡될 수 있으므로 단독 근거로 사용하지 않습니다.");

        verify(rankingApiClient).inquireOvertimeVolume(new KisFhpst02350000OvertimeVolumeRequest("J", "20235", "0000", "0", "0", "0", "0", "0", "0"));
        verify(rankingApiClient).inquireOvertimeFluctuation(new KisFhpst02340000OvertimeFluctuationRequest("J", "", "20234", "0000", "2", "", "", "", "", ""));
        verify(quotationApiClient).inquireNewsTitle(new KisFhkst01011800NewsTitleRequest("", "", "", "", "0020260602", "0000153000", "", ""));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
