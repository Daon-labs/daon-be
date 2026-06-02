package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010200TodayMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010200TodayMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010230DailyMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhkst03010230DailyMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhpst01060000TimeItemConclusionRequest;
import daon.be.agent.data.source.kis.quotation.intraday.KisFhpst01060000TimeItemConclusionResponse;
import daon.be.agent.tool.stock.dto.IntradayMoveContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisIntradayMoveContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisIntradayMoveContextService service = new KisIntradayMoveContextService(quotationApiClient);

    @Test
    void getIntradayMoveContextForTodayCombinesMinuteBarsAndExecutionSummaries() throws Exception {
        when(quotationApiClient.inquireTodayMinuteChart(
                new KisFhkst03010200TodayMinuteChartRequest("J", "005930", "101500", "N", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "hts_kor_isnm": "삼성전자",
                    "stck_prpr": "70000",
                    "prdy_vrss": "-1200",
                    "prdy_vrss_sign": "5",
                    "prdy_ctrt": "-1.69",
                    "acml_vol": "12345678",
                    "acml_tr_pbmn": "987654321000"
                  },
                  "output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_cntg_hour": "101500",
                      "stck_prpr": "70000",
                      "stck_oprc": "70200",
                      "stck_hgpr": "70300",
                      "stck_lwpr": "69900",
                      "cntg_vol": "10000",
                      "acml_tr_pbmn": "987654321000"
                    },
                    {
                      "stck_bsop_date": "20260602",
                      "stck_cntg_hour": "101400",
                      "stck_prpr": "70500",
                      "stck_oprc": "70600",
                      "stck_hgpr": "70700",
                      "stck_lwpr": "70400",
                      "cntg_vol": "8000",
                      "acml_tr_pbmn": "986954321000"
                    }
                  ]
                }
                """, KisFhkst03010200TodayMinuteChartResponse.class));
        when(quotationApiClient.inquireTimeItemConclusion(
                new KisFhpst01060000TimeItemConclusionRequest("J", "005930", "101500")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "stck_prpr": "70000",
                    "prdy_vrss": "-1200",
                    "prdy_vrss_sign": "5",
                    "prdy_ctrt": "-1.69",
                    "acml_vol": "12345678",
                    "rprs_mrkt_kor_name": "KOSPI"
                  },
                  "output2": [
                    {
                      "stck_cntg_hour": "101500",
                      "stck_pbpr": "70000",
                      "askp": "70100",
                      "bidp": "70000",
                      "tday_rltv": "87.45",
                      "acml_vol": "12345678",
                      "cnqn": "1200"
                    }
                  ]
                }
                """, KisFhpst01060000TimeItemConclusionResponse.class));

        IntradayMoveContextDto result = service.getIntradayMoveContext("005930", "", "101500");

        assertThat(result.meta().toolName()).isEqualTo("GET_INTRADAY_MOVE_CONTEXT");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.requestedRange().inputHour()).isEqualTo("101500");
        assertThat(result.currentPrice().price()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.minuteBars()).hasSize(2);
        assertThat(result.minuteBars().getFirst().time()).isEqualTo("101500");
        assertThat(result.turningPoints()).extracting(IntradayMoveContextDto.IntradayTurningPointDto::type)
                .containsExactly("HIGH", "LOW", "VOLUME_SPIKE");
        assertThat(result.executionSummaries()).hasSize(1);
        assertThat(result.executionSummaries().getFirst().tradeStrength()).isEqualByComparingTo(new BigDecimal("87.45"));
        assertThat(result.meta().kisApiCalls()).hasSize(2);
        assertThat(result.limitations()).contains("분봉은 KIS 응답 기준 최근 구간이며, 1회 조회 건수 제한을 받습니다.");

        verify(quotationApiClient).inquireTodayMinuteChart(
                new KisFhkst03010200TodayMinuteChartRequest("J", "005930", "101500", "N", "")
        );
        verify(quotationApiClient).inquireTimeItemConclusion(
                new KisFhpst01060000TimeItemConclusionRequest("J", "005930", "101500")
        );
    }

    @Test
    void getIntradayMoveContextForHistoricalDateUsesDailyMinuteChart() throws Exception {
        when(quotationApiClient.inquireDailyMinuteChart(
                new KisFhkst03010230DailyMinuteChartRequest("J", "005930", "153000", "20260520", "N", "N")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "hts_kor_isnm": "삼성전자",
                    "stck_prpr": "70500",
                    "prdy_vrss": "500",
                    "prdy_vrss_sign": "2",
                    "prdy_ctrt": "0.71",
                    "acml_vol": "10000000",
                    "acml_tr_pbmn": "700000000000"
                  },
                  "output2": [
                    {
                      "stck_bsop_date": "20260520",
                      "stck_cntg_hour": "153000",
                      "stck_prpr": "70500",
                      "stck_oprc": "70400",
                      "stck_hgpr": "70600",
                      "stck_lwpr": "70300",
                      "cntg_vol": "50000",
                      "acml_tr_pbmn": "700000000000"
                    }
                  ]
                }
                """, KisFhkst03010230DailyMinuteChartResponse.class));

        IntradayMoveContextDto result = service.getIntradayMoveContext("005930", "2026-05-20", "153000");

        assertThat(result.requestedRange().tradingDate()).isEqualTo("20260520");
        assertThat(result.minuteBars()).hasSize(1);
        assertThat(result.executionSummaries()).isEmpty();
        assertThat(result.meta().kisApiCalls()).hasSize(1);
        assertThat(result.limitations()).contains("과거 거래일 조회에서는 당일시간대별체결 API를 호출하지 않습니다.");

        verify(quotationApiClient).inquireDailyMinuteChart(
                new KisFhkst03010230DailyMinuteChartRequest("J", "005930", "153000", "20260520", "N", "N")
        );
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
