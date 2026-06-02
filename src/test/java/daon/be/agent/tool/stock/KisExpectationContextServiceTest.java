package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.finance.KisFinanceApiClient;
import daon.be.agent.data.source.kis.finance.expectation.KisFhkst663300C0InvestOpinionRequest;
import daon.be.agent.data.source.kis.finance.expectation.KisFhkst663300C0InvestOpinionResponse;
import daon.be.agent.data.source.kis.finance.expectation.KisHhkst668300C0EstimatePerformRequest;
import daon.be.agent.data.source.kis.finance.expectation.KisHhkst668300C0EstimatePerformResponse;
import daon.be.agent.tool.stock.dto.ExpectationContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisExpectationContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisFinanceApiClient financeApiClient = mock(KisFinanceApiClient.class);
    private final KisExpectationContextService service = new KisExpectationContextService(financeApiClient);

    @Test
    void getExpectationContextCombinesOpinionsAndEstimatedPerformance() throws Exception {
        when(financeApiClient.inquireInvestOpinion(
                new KisFhkst663300C0InvestOpinionRequest("J", "16633", "005930", "20260101", "20260602")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_bsop_date": "20260602",
                      "invt_opnn": "매수",
                      "invt_opnn_cls_code": "2",
                      "rgbf_invt_opnn": "중립",
                      "rgbf_invt_opnn_cls_code": "3",
                      "mbcr_name": "한국투자증권",
                      "hts_goal_prc": "90000",
                      "stck_prdy_clpr": "70000",
                      "stck_nday_esdg": "20000",
                      "nday_dprt": "28.57",
                      "stft_esdg": "0",
                      "dprt": "28.57"
                    }
                  ]
                }
                """, KisFhkst663300C0InvestOpinionResponse.class));
        when(financeApiClient.inquireEstimatePerform(
                new KisHhkst668300C0EstimatePerformRequest("005930")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "sht_cd": "A005930",
                    "item_kor_nm": "삼성전자",
                    "name1": "김한국",
                    "estdate": "20260601",
                    "rcmd_name": "매수",
                    "capital": "8975.0",
                    "forn_item_lmtrt": "0.00"
                  },
                  "output2": [
                    { "data1": "3048945.0", "data2": "3295675.0" },
                    { "data1": "181.0", "data2": "81.0" },
                    { "data1": "330172.0", "data2": "555410.0" },
                    { "data1": "4048.0", "data2": "682.0" },
                    { "data1": "253332.0", "data2": "422055.0" },
                    { "data1": "1387.0", "data2": "666.0" }
                  ],
                  "output3": [
                    { "data1": "792602.0", "data2": "1043367.0" },
                    { "data1": "36983.0", "data2": "61483.0" },
                    { "data1": "1369.0", "data2": "662.0" },
                    { "data1": "207.0", "data2": "124.0" },
                    { "data1": "53.0", "data2": "39.0" },
                    { "data1": "70.0", "data2": "109.0" },
                    { "data1": "226.0", "data2": "163.0" },
                    { "data1": "232.0", "data2": "655.0" }
                  ],
                  "output4": [
                    { "dt": "2024.12E" },
                    { "dt": "2025.12E" }
                  ]
                }
                """, KisHhkst668300C0EstimatePerformResponse.class));

        ExpectationContextDto result = service.getExpectationContext("005930", "2026-01-01", "2026-06-02", "J");

        assertThat(result.meta().toolName()).isEqualTo("GET_EXPECTATION_CONTEXT");
        assertThat(result.meta().kisApiCalls()).hasSize(2);
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.identity().market()).isEqualTo("J");
        assertThat(result.investmentOpinions()).hasSize(1);
        assertThat(result.investmentOpinions().getFirst().date()).isEqualTo("20260602");
        assertThat(result.investmentOpinions().getFirst().opinion()).isEqualTo("매수");
        assertThat(result.investmentOpinions().getFirst().previousOpinion()).isEqualTo("중립");
        assertThat(result.investmentOpinions().getFirst().targetPrice()).isEqualByComparingTo(new BigDecimal("90000"));
        assertThat(result.investmentOpinions().getFirst().targetPriceGapRate()).isEqualByComparingTo(new BigDecimal("28.57"));
        assertThat(result.estimatedPerformance().incomeStatementEstimates()).hasSize(2);
        assertThat(result.estimatedPerformance().incomeStatementEstimates().get(1).period()).isEqualTo("2025.12E");
        assertThat(result.estimatedPerformance().incomeStatementEstimates().get(1).estimatedSales()).isEqualByComparingTo(new BigDecimal("3295675.0"));
        assertThat(result.estimatedPerformance().incomeStatementEstimates().get(1).estimatedOperatingProfit()).isEqualByComparingTo(new BigDecimal("555410.0"));
        assertThat(result.estimatedPerformance().incomeStatementEstimates().get(1).estimatedNetIncome()).isEqualByComparingTo(new BigDecimal("422055.0"));
        assertThat(result.estimatedPerformance().investmentIndicatorEstimates().get(1).estimatedEps()).isEqualByComparingTo(new BigDecimal("61483.0"));
        assertThat(result.estimatedPerformance().investmentIndicatorEstimates().get(1).estimatedPer()).isEqualByComparingTo(new BigDecimal("124.0"));
        assertThat(result.estimatedPerformance().investmentIndicatorEstimates().get(1).estimatedRoe()).isEqualByComparingTo(new BigDecimal("109.0"));
        assertThat(result.estimatedPerformance().summary().latestPeriod()).isEqualTo("2025.12E");
        assertThat(result.estimatedPerformance().summary().latestEstimatedSales()).isEqualByComparingTo(new BigDecimal("3295675.0"));
        assertThat(result.cautions()).contains(
                "투자의견과 추정실적은 가격 변동의 직접 원인으로 단정하지 않습니다.",
                "종목추정실적은 리서치본부 추정 대상 일부 종목으로 제한됩니다."
        );

        verify(financeApiClient).inquireInvestOpinion(new KisFhkst663300C0InvestOpinionRequest("J", "16633", "005930", "20260101", "20260602"));
        verify(financeApiClient).inquireEstimatePerform(new KisHhkst668300C0EstimatePerformRequest("005930"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
