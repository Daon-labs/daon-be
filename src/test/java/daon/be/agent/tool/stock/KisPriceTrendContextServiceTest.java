package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.daily.KisFhkst03010100DailyItemChartPriceRequest;
import daon.be.agent.data.source.kis.quotation.daily.KisFhkst03010100DailyItemChartPriceResponse;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceResponse;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.PriceTrendContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisPriceTrendContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisPriceTrendContextService service = new KisPriceTrendContextService(quotationApiClient);

    @Test
    void getPriceTrendContextCombinesDailyBarsCurrentPriceAndStockInfo() throws Exception {
        when(quotationApiClient.inquireDailyItemChartPrice(
                new KisFhkst03010100DailyItemChartPriceRequest("J", "005930", "20260501", "20260602", "D", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "hts_kor_isnm": "삼성전자",
                    "stck_shrn_iscd": "005930",
                    "stck_prpr": "70000",
                    "prdy_vrss": "1200",
                    "prdy_vrss_sign": "2",
                    "prdy_ctrt": "1.74",
                    "acml_vol": "12345678",
                    "acml_tr_pbmn": "987654321000"
                  },
                  "output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_oprc": "69200",
                      "stck_hgpr": "70500",
                      "stck_lwpr": "69000",
                      "stck_clpr": "70000",
                      "acml_vol": "12345678",
                      "acml_tr_pbmn": "987654321000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2"
                    },
                    {
                      "stck_bsop_date": "20260501",
                      "stck_oprc": "67500",
                      "stck_hgpr": "68200",
                      "stck_lwpr": "67000",
                      "stck_clpr": "68000",
                      "acml_vol": "9000000",
                      "acml_tr_pbmn": "612000000000",
                      "prdy_vrss": "-500",
                      "prdy_vrss_sign": "5"
                    }
                  ]
                }
                """, KisFhkst03010100DailyItemChartPriceResponse.class));
        when(quotationApiClient.inquirePrice(new KisFhkst01010100InquirePriceRequest("J", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": {
                            "stck_shrn_iscd": "005930",
                            "rprs_mrkt_kor_name": "KOSPI",
                            "bstp_kor_isnm": "전기전자",
                            "stck_prpr": "70000",
                            "w52_hgpr": "85000",
                            "w52_hgpr_date": "20260110",
                            "w52_hgpr_vrss_prpr_ctrt": "-17.65",
                            "w52_lwpr": "60000",
                            "w52_lwpr_date": "20251220",
                            "w52_lwpr_vrss_prpr_ctrt": "16.67"
                          }
                        }
                        """, KisFhkst01010100InquirePriceResponse.class));
        when(quotationApiClient.searchStockInfo(new KisCtpf1002rSearchStockInfoRequest("300", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": {
                            "pdno": "005930",
                            "prdt_name": "삼성전자",
                            "mket_id_cd": "STK",
                            "idx_bztp_scls_cd": "013",
                            "idx_bztp_scls_cd_name": "전기전자"
                          }
                        }
                        """, KisCtpf1002rSearchStockInfoResponse.class));

        PriceTrendContextDto result = service.getPriceTrendContext("005930", "2026-05-01", "2026-06-02", "D");

        assertThat(result.meta().toolName()).isEqualTo("GET_PRICE_TREND_CONTEXT");
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.identity().market()).isEqualTo("KOSPI");
        assertThat(result.periodType()).isEqualTo("D");
        assertThat(result.bars()).hasSize(2);
        assertThat(result.trendSummary().startClose()).isEqualByComparingTo(new BigDecimal("68000"));
        assertThat(result.trendSummary().latestClose()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.trendSummary().periodReturnRate()).isEqualByComparingTo(new BigDecimal("2.94"));
        assertThat(result.volatility().highestPrice()).isEqualByComparingTo(new BigDecimal("70500"));
        assertThat(result.volatility().lowestPrice()).isEqualByComparingTo(new BigDecimal("67000"));
        assertThat(result.breakoutContext().week52High()).isEqualByComparingTo(new BigDecimal("85000"));
        assertThat(result.breakoutContext().distanceFromWeek52HighRate()).isEqualByComparingTo(new BigDecimal("-17.65"));
        assertThat(result.limitations()).contains("기간별시세는 요청 기간과 KIS 응답 건수 제한을 받습니다.");
        assertThat(result.meta().kisApiCalls()).hasSize(3);

        verify(quotationApiClient).inquireDailyItemChartPrice(
                new KisFhkst03010100DailyItemChartPriceRequest("J", "005930", "20260501", "20260602", "D", "0")
        );
        verify(quotationApiClient).inquirePrice(new KisFhkst01010100InquirePriceRequest("J", "005930"));
        verify(quotationApiClient).searchStockInfo(new KisCtpf1002rSearchStockInfoRequest("300", "005930"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
