package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst01011800NewsTitleResponse;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhkst130000C0CaptureUpLowPriceResponse;
import daon.be.agent.data.source.kis.quotation.event.KisFhpst01390000ViStatusRequest;
import daon.be.agent.data.source.kis.quotation.event.KisFhpst01390000ViStatusResponse;
import daon.be.agent.tool.stock.dto.EventTimelineContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisEventTimelineContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisEventTimelineContextService service = new KisEventTimelineContextService(quotationApiClient);

    @Test
    void getEventTimelineContextCombinesViNewsAndLimitPriceCaptures() throws Exception {
        when(quotationApiClient.inquireViStatus(
                new KisFhpst01390000ViStatusRequest("0", "20139", "0", "005930", "0", "20260602", "", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "hts_kor_isnm": "삼성전자",
                      "mksc_shrn_iscd": "005930",
                      "vi_cls_code": "Y",
                      "bsop_date": "20260602",
                      "cntg_vi_hour": "101500",
                      "vi_cncl_hour": "101700",
                      "vi_kind_code": "2",
                      "vi_prc": "70000",
                      "vi_stnd_prc": "0",
                      "vi_dprt": "0.00",
                      "vi_dmc_stnd_prc": "68200",
                      "vi_dmc_dprt": "2.64",
                      "vi_count": "1"
                    },
                    {
                      "hts_kor_isnm": "다른종목",
                      "mksc_shrn_iscd": "000000",
                      "vi_cls_code": "N",
                      "bsop_date": "20260602",
                      "cntg_vi_hour": "101000",
                      "vi_cncl_hour": "101200",
                      "vi_kind_code": "2",
                      "vi_prc": "1000",
                      "vi_stnd_prc": "0",
                      "vi_dprt": "0.00",
                      "vi_dmc_stnd_prc": "950",
                      "vi_dmc_dprt": "5.26",
                      "vi_count": "1"
                    }
                  ]
                }
                """, KisFhpst01390000ViStatusResponse.class));
        when(quotationApiClient.inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", "", "005930", "", "0020260602", "0000101500", "", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "cntt_usiq_srno": "202606021014000001",
                      "news_ofer_entp_code": "7",
                      "data_dt": "20260602",
                      "data_tm": "101400",
                      "hts_pbnt_titl_cntt": "삼성전자, 장중 변동성 확대",
                      "news_lrdv_code": "02",
                      "dorg": "인포스탁",
                      "iscd1": "005930",
                      "iscd2": "",
                      "iscd3": "",
                      "iscd4": "",
                      "iscd5": ""
                    }
                  ]
                }
                """, KisFhkst01011800NewsTitleResponse.class));
        when(quotationApiClient.captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest("J", "11300", "0", "0", "0000", "", "", "", "", "")
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
                      "prdy_vrss_sign": "2",
                      "prdy_vrss": "1500",
                      "prdy_ctrt": "2.19",
                      "acml_vol": "12345678",
                      "total_askp_rsqn": "56000",
                      "total_bidp_rsqn": "73000",
                      "askp_rsqn1": "1200",
                      "bidp_rsqn1": "1800",
                      "prdy_vol": "9000000",
                      "seln_cnqn": "100",
                      "shnu_cnqn": "250",
                      "stck_llam": "49900",
                      "stck_mxpr": "92500",
                      "prdy_vrss_vol_rate": "137.17"
                    }
                  ]
                }
                """, KisFhkst130000C0CaptureUpLowPriceResponse.class));
        when(quotationApiClient.captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest("J", "11300", "1", "0", "0000", "", "", "", "", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": []
                }
                """, KisFhkst130000C0CaptureUpLowPriceResponse.class));

        EventTimelineContextDto result = service.getEventTimelineContext("005930", "2026-06-02", "101500");

        assertThat(result.meta().toolName()).isEqualTo("GET_EVENT_TIMELINE_CONTEXT");
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.requestedRange().tradingDate()).isEqualTo("20260602");
        assertThat(result.requestedRange().inputHour()).isEqualTo("101500");
        assertThat(result.viEvents()).hasSize(1);
        assertThat(result.viEvents().getFirst().triggerPrice()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.newsTitleEvents()).hasSize(1);
        assertThat(result.newsTitleEvents().getFirst().title()).isEqualTo("삼성전자, 장중 변동성 확대");
        assertThat(result.limitPriceCaptures()).hasSize(1);
        assertThat(result.limitPriceCaptures().getFirst().captureType()).isEqualTo("UPPER_LIMIT");
        assertThat(result.correlationHints()).extracting(EventTimelineContextDto.EventCorrelationHintDto::eventType)
                .containsExactly("VI_AND_NEWS");
        assertThat(result.cautions()).contains("뉴스/공시 제목은 원인 확정 근거가 아니라 동시간대 이벤트 후보입니다.");
        assertThat(result.meta().kisApiCalls()).hasSize(4);

        verify(quotationApiClient).inquireViStatus(
                new KisFhpst01390000ViStatusRequest("0", "20139", "0", "005930", "0", "20260602", "", "")
        );
        verify(quotationApiClient).inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", "", "005930", "", "0020260602", "0000101500", "", "")
        );
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
