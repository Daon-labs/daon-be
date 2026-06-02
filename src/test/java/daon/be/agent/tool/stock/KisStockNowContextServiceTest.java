package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010100InquirePriceResponse;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010200AskingPriceRequest;
import daon.be.agent.data.source.kis.quotation.price.KisFhkst01010200AskingPriceResponse;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.StockNowContextDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisStockNowContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisStockNowContextService service = new KisStockNowContextService(quotationApiClient);

    @Test
    void getStockNowContextCombinesCurrentPriceAskingPriceAndStockInfo() throws Exception {
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
                            "prdy_vrss": "-1200",
                            "prdy_vrss_sign": "5",
                            "prdy_ctrt": "-1.69",
                            "acml_vol": "12345678",
                            "acml_tr_pbmn": "987654321000",
                            "stck_oprc": "71200",
                            "stck_hgpr": "71500",
                            "stck_lwpr": "69800",
                            "stck_mxpr": "92500",
                            "stck_llam": "49900",
                            "per": "15.25",
                            "pbr": "1.21",
                            "eps": "4587",
                            "bps": "57840",
                            "vi_cls_code": "0",
                            "temp_stop_yn": "N",
                            "invt_caful_yn": "N",
                            "mang_issu_cls_code": "N"
                          }
                        }
                        """, KisFhkst01010100InquirePriceResponse.class));
        when(quotationApiClient.inquireAskingPrice(new KisFhkst01010200AskingPriceRequest("J", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output1": {
                            "aspr_acpt_hour": "101530",
                            "askp1": "70100",
                            "askp_rsqn1": "1200",
                            "bidp1": "70000",
                            "bidp_rsqn1": "1800",
                            "total_askp_rsqn": "56000",
                            "total_bidp_rsqn": "73000",
                            "ntby_aspr_rsqn": "17000",
                            "new_mkop_cls_code": "20"
                          },
                          "output2": {
                            "stck_prpr": "70000",
                            "vi_cls_code": "0"
                          }
                        }
                        """, KisFhkst01010200AskingPriceResponse.class));
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
                            "excg_dvsn_cd": "KRX",
                            "idx_bztp_scls_cd": "013",
                            "idx_bztp_scls_cd_name": "전기전자",
                            "etf_dvsn_cd": "0",
                            "tr_stop_yn": "N",
                            "admn_item_yn": "N",
                            "cptt_trad_tr_psbl_yn": "Y"
                          }
                        }
                        """, KisCtpf1002rSearchStockInfoResponse.class));

        StockNowContextDto result = service.getStockNowContext("005930");

        assertThat(result.meta().toolName()).isEqualTo("GET_STOCK_NOW_CONTEXT");
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.identity().industryName()).isEqualTo("전기전자");
        assertThat(result.currentPrice().price()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(result.currentPrice().changeRate()).isEqualByComparingTo(new BigDecimal("-1.69"));
        assertThat(result.dailyRange().low()).isEqualByComparingTo(new BigDecimal("69800"));
        assertThat(result.orderbook().levels()).hasSize(1);
        assertThat(result.orderbook().levels().getFirst().askPrice()).isEqualByComparingTo(new BigDecimal("70100"));
        assertThat(result.orderbook().totalBidQuantity()).isEqualTo(73000L);
        assertThat(result.tradingStatus().tradingSuspended()).isFalse();
        assertThat(result.valuationFromQuote().per()).isEqualByComparingTo(new BigDecimal("15.25"));
        assertThat(result.meta().kisApiCalls()).hasSize(3);
        assertThat(result.meta().limitations()).contains("국내주식 장운영정보 WebSocket 스냅샷은 아직 결합하지 않았습니다.");

        ArgumentCaptor<KisFhkst01010100InquirePriceRequest> priceRequest =
                ArgumentCaptor.forClass(KisFhkst01010100InquirePriceRequest.class);
        verify(quotationApiClient).inquirePrice(priceRequest.capture());
        assertThat(priceRequest.getValue().fidCondMrktDivCode()).isEqualTo("J");
        assertThat(priceRequest.getValue().fidInputIscd()).isEqualTo("005930");
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
