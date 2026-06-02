package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600001CompProgramTradeDailyRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600001CompProgramTradeDailyResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600101CompProgramTradeTodayRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04600101CompProgramTradeTodayResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650101ProgramTradeByStockRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650101ProgramTradeByStockResponse;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650201ProgramTradeByStockDailyRequest;
import daon.be.agent.data.source.kis.quotation.program.KisFhppg04650201ProgramTradeByStockDailyResponse;
import daon.be.agent.data.source.kis.quotation.program.KisHhppg046600C1InvestorProgramTradeTodayRequest;
import daon.be.agent.data.source.kis.quotation.program.KisHhppg046600C1InvestorProgramTradeTodayResponse;
import daon.be.agent.tool.stock.dto.ProgramTradingContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisProgramTradingContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisProgramTradingContextService service = new KisProgramTradingContextService(quotationApiClient);

    @Test
    void getProgramTradingContextCombinesStockMarketAndInvestorProgramFlows() throws Exception {
        when(quotationApiClient.inquireProgramTradeByStock(
                new KisFhppg04650101ProgramTradeByStockRequest("J", "005930")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "bsop_hour": "153000",
                      "stck_prpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678",
                      "whol_smtn_seln_vol": "200000",
                      "whol_smtn_shnu_vol": "350000",
                      "whol_smtn_ntby_qty": "150000",
                      "whol_smtn_seln_tr_pbmn": "14000000000",
                      "whol_smtn_shnu_tr_pbmn": "24500000000",
                      "whol_smtn_ntby_tr_pbmn": "10500000000"
                    }
                  ]
                }
                """, KisFhppg04650101ProgramTradeByStockResponse.class));
        when(quotationApiClient.inquireProgramTradeByStockDaily(
                new KisFhppg04650201ProgramTradeByStockDailyRequest("J", "005930", "20260520")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_clpr": "70000",
                      "prdy_vrss": "1200",
                      "prdy_vrss_sign": "2",
                      "prdy_ctrt": "1.74",
                      "acml_vol": "12345678",
                      "acml_tr_pbmn": "987654321000",
                      "whol_smtn_seln_vol": "500000",
                      "whol_smtn_shnu_vol": "800000",
                      "whol_smtn_ntby_qty": "300000",
                      "whol_smtn_seln_tr_pbmn": "35000000000",
                      "whol_smtn_shnu_tr_pbmn": "56000000000",
                      "whol_smtn_ntby_tr_pbmn": "21000000000"
                    }
                  ]
                }
                """, KisFhppg04650201ProgramTradeByStockDailyResponse.class));
        when(quotationApiClient.inquireCompProgramTradeToday(
                new KisFhppg04600101CompProgramTradeTodayRequest("J", "K", "0", "0000", "J", "090000")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": [
                    {
                      "bsop_hour": "153000",
                      "arbt_smtn_ntby_tr_pbmn": "1000000000",
                      "nabt_smtn_ntby_tr_pbmn": "-3000000000",
                      "whol_smtn_ntby_tr_pbmn": "-2000000000",
                      "bstp_nmix_prpr": "2800.50",
                      "bstp_nmix_prdy_vrss": "-15.20",
                      "prdy_vrss_sign": "5"
                    }
                  ]
                }
                """, KisFhppg04600101CompProgramTradeTodayResponse.class));
        when(quotationApiClient.inquireCompProgramTradeDaily(
                new KisFhppg04600001CompProgramTradeDailyRequest("J", "K", "20260520", "20260602")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "stck_bsop_date": "20260602",
                      "arbt_smtn_ntby_tr_pbmn": "1200000000",
                      "nabt_smtn_ntby_tr_pbmn": "-3200000000",
                      "arbt_smtn_ntby_qty": "10000",
                      "nabt_smtn_ntby_qty": "-25000"
                    }
                  ]
                }
                """, KisFhppg04600001CompProgramTradeDailyResponse.class));
        when(quotationApiClient.inquireInvestorProgramTradeToday(
                new KisHhppg046600C1InvestorProgramTradeTodayRequest("J", "1")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": [
                    {
                      "invr_cls_code": "1000",
                      "invr_cls_name": "금융투자",
                      "all_seln_qty": "100000",
                      "all_shnu_qty": "160000",
                      "all_ntby_qty": "60000",
                      "all_seln_amt": "7000000000",
                      "all_shnu_amt": "11200000000",
                      "all_ntby_amt": "4200000000",
                      "arbt_ntby_amt": "1000000000",
                      "nabt_ntby_amt": "3200000000"
                    }
                  ]
                }
                """, KisHhppg046600C1InvestorProgramTradeTodayResponse.class));

        ProgramTradingContextDto result = service.getProgramTradingContext("005930", "2026-05-20", "2026-06-02", "J", "090000");

        assertThat(result.meta().toolName()).isEqualTo("GET_PROGRAM_TRADING_CONTEXT");
        assertThat(result.meta().kisApiCalls()).hasSize(5);
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().market()).isEqualTo("J");
        assertThat(result.stockIntradayProgramTrades()).hasSize(1);
        assertThat(result.stockIntradayProgramTrades().getFirst().netBuyQuantity()).isEqualTo(150000L);
        assertThat(result.stockIntradayProgramTrades().getFirst().netBuyAmount()).isEqualByComparingTo(new BigDecimal("10500000000"));
        assertThat(result.stockDailyProgramTrades().getFirst().date()).isEqualTo("20260602");
        assertThat(result.stockDailyProgramTrades().getFirst().netBuyQuantity()).isEqualTo(300000L);
        assertThat(result.marketIntradayProgramTrades().getFirst().wholeNetBuyAmount()).isEqualByComparingTo(new BigDecimal("-2000000000"));
        assertThat(result.marketDailyProgramTrades().getFirst().nonArbitrageNetBuyAmount()).isEqualByComparingTo(new BigDecimal("-3200000000"));
        assertThat(result.investorBreakdown().investors()).hasSize(1);
        assertThat(result.investorBreakdown().investors().getFirst().investorName()).isEqualTo("금융투자");
        assertThat(result.investorBreakdown().investors().getFirst().wholeNetBuyAmount()).isEqualByComparingTo(new BigDecimal("4200000000"));
        assertThat(result.interpretationHints()).contains("프로그램매매 데이터는 수급 원인 후보이며 가격 변동의 단독 원인으로 단정하지 않습니다.");

        verify(quotationApiClient).inquireProgramTradeByStock(new KisFhppg04650101ProgramTradeByStockRequest("J", "005930"));
        verify(quotationApiClient).inquireProgramTradeByStockDaily(new KisFhppg04650201ProgramTradeByStockDailyRequest("J", "005930", "20260520"));
        verify(quotationApiClient).inquireCompProgramTradeToday(new KisFhppg04600101CompProgramTradeTodayRequest("J", "K", "0", "0000", "J", "090000"));
        verify(quotationApiClient).inquireCompProgramTradeDaily(new KisFhppg04600001CompProgramTradeDailyRequest("J", "K", "20260520", "20260602"));
        verify(quotationApiClient).inquireInvestorProgramTradeToday(new KisHhppg046600C1InvestorProgramTradeTodayRequest("J", "1"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
