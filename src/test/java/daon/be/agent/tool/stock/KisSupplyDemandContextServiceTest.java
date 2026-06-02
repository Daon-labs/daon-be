package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04160001InvestorTradeByStockDailyRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04160001InvestorTradeByStockDailyResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04400000ForeignInstitutionTotalRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04400000ForeignInstitutionTotalResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisHhptj04160200InvestorTrendEstimateRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisHhptj04160200InvestorTrendEstimateResponse;
import daon.be.agent.tool.stock.dto.SupplyDemandContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisSupplyDemandContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisSupplyDemandContextService service = new KisSupplyDemandContextService(quotationApiClient);

    @Test
    void getSupplyDemandContextSeparatesEstimatedAndConfirmedInvestorFlows() throws Exception {
        when(quotationApiClient.inquireInvestorTradeByStockDaily(
                new KisFhptj04160001InvestorTradeByStockDailyRequest("J", "005930", "20260520", "0", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "stck_prpr": "70000",
                    "rprs_mrkt_kor_name": "KOSPI"
                  },
                  "output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_clpr": "70000",
                      "frgn_ntby_qty": "-120000",
                      "frgn_ntby_tr_pbmn": "-35000000000",
                      "prsn_ntby_qty": "150000",
                      "prsn_ntby_tr_pbmn": "42000000000",
                      "orgn_ntby_qty": "-30000",
                      "orgn_ntby_tr_pbmn": "-7000000000"
                    }
                  ]
                }
                """, KisFhptj04160001InvestorTradeByStockDailyResponse.class));
        when(quotationApiClient.inquireInvestorTrendEstimate(
                new KisHhptj04160200InvestorTrendEstimateRequest("005930")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output2": [
                    {
                      "bsop_hour_gb": "153000",
                      "frgn_fake_ntby_qty": "-100000",
                      "orgn_fake_ntby_qty": "-25000",
                      "sum_fake_ntby_qty": "-125000"
                    }
                  ]
                }
                """, KisHhptj04160200InvestorTrendEstimateResponse.class));
        when(quotationApiClient.inquireForeignInstitutionTotal(
                new KisFhptj04400000ForeignInstitutionTotalRequest("J", "16449", "0000", "0", "0", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "Output": {
                    "hts_kor_isnm": "삼성전자",
                    "mksc_shrn_iscd": "005930",
                    "ntby_qty": "-125000",
                    "stck_prpr": "70000",
                    "frgn_ntby_qty": "-100000",
                    "orgn_ntby_qty": "-25000",
                    "frgn_ntby_tr_pbmn": "-30000000000",
                    "orgn_ntby_tr_pbmn": "-5000000000"
                  }
                }
                """, KisFhptj04400000ForeignInstitutionTotalResponse.class));
        when(quotationApiClient.inquireInvestorDailyByMarket(
                new KisFhptj04040000InvestorDailyByMarketRequest("U", "0001", "20260520", "KSP", "20260520", "0001")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_bsop_date": "20260602",
                      "frgn_ntby_qty": "-500000",
                      "frgn_ntby_tr_pbmn": "-120000000000",
                      "prsn_ntby_qty": "650000",
                      "prsn_ntby_tr_pbmn": "150000000000",
                      "orgn_ntby_qty": "-150000",
                      "orgn_ntby_tr_pbmn": "-30000000000"
                    }
                  ]
                }
                """, KisFhptj04040000InvestorDailyByMarketResponse.class));

        SupplyDemandContextDto result = service.getSupplyDemandContext("005930", "2026-05-20", "0001");

        assertThat(result.meta().toolName()).isEqualTo("GET_SUPPLY_DEMAND_CONTEXT");
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.identity().market()).isEqualTo("KOSPI");
        assertThat(result.intradayEstimate().estimated()).isTrue();
        assertThat(result.intradayEstimate().foreignNetBuyQuantity()).isEqualTo(-100000L);
        assertThat(result.dailyInvestorFlows()).hasSize(1);
        assertThat(result.dailyInvestorFlows().getFirst().foreignNetBuyAmount()).isEqualByComparingTo(new BigDecimal("-35000000000"));
        assertThat(result.marketInstitutionForeignRanks()).hasSize(1);
        assertThat(result.marketInstitutionForeignRanks().getFirst().netBuyQuantity()).isEqualTo(-125000L);
        assertThat(result.marketFlow().foreignNetBuyAmount()).isEqualByComparingTo(new BigDecimal("-120000000000"));
        assertThat(result.cautions()).contains("종목별 외인기관 추정가집계는 확정 수급이 아니라 장중 가집계입니다.");
        assertThat(result.meta().kisApiCalls()).hasSize(4);

        verify(quotationApiClient).inquireInvestorTradeByStockDaily(
                new KisFhptj04160001InvestorTradeByStockDailyRequest("J", "005930", "20260520", "0", "")
        );
        verify(quotationApiClient).inquireInvestorTrendEstimate(new KisHhptj04160200InvestorTrendEstimateRequest("005930"));
        verify(quotationApiClient).inquireForeignInstitutionTotal(
                new KisFhptj04400000ForeignInstitutionTotalRequest("J", "16449", "0000", "0", "0", "")
        );
        verify(quotationApiClient).inquireInvestorDailyByMarket(
                new KisFhptj04040000InvestorDailyByMarketRequest("U", "0001", "20260520", "KSP", "20260520", "0001")
        );
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
