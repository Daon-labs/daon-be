package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04760000DailyCreditBalanceRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04760000DailyCreditBalanceResponse;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04830000DailyShortSaleRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04830000DailyShortSaleResponse;
import daon.be.agent.data.source.kis.quotation.risk.KisHhpst074500C0DailyLoanTransRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisHhpst074500C0DailyLoanTransResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.risk.KisFhkst17010000CreditBalanceRankRequest;
import daon.be.agent.data.source.kis.ranking.risk.KisFhkst17010000CreditBalanceRankResponse;
import daon.be.agent.data.source.kis.ranking.risk.KisFhpst04820000ShortSaleRankRequest;
import daon.be.agent.data.source.kis.ranking.risk.KisFhpst04820000ShortSaleRankResponse;
import daon.be.agent.tool.stock.dto.ShortCreditLoanContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisShortCreditLoanContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisRankingApiClient rankingApiClient = mock(KisRankingApiClient.class);
    private final KisShortCreditLoanContextService service = new KisShortCreditLoanContextService(quotationApiClient, rankingApiClient);

    @Test
    void getShortCreditLoanContextCombinesShortSaleCreditAndLoanRisk() throws Exception {
        when(quotationApiClient.inquireDailyShortSale(
                new KisFhpst04830000DailyShortSaleRequest("20260602", "J", "005930", "20260520")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_clpr": "70000",
                      "prdy_vrss": "-1000",
                      "prdy_vrss_sign": "5",
                      "prdy_ctrt": "-1.41",
                      "acml_vol": "12345678",
                      "ssts_cntg_qty": "420000",
                      "ssts_vol_rlim": "3.40",
                      "ssts_tr_pbmn": "29400000000",
                      "ssts_tr_pbmn_rlim": "3.15"
                    }
                  ]
                }
                """, KisFhpst04830000DailyShortSaleResponse.class));
        when(quotationApiClient.inquireDailyCreditBalance(
                new KisFhpst04760000DailyCreditBalanceRequest("J", "20476", "005930", "20260520")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "deal_date": "20260602",
                      "stlm_date": "20260604",
                      "stck_prpr": "70000",
                      "whol_loan_new_stcn": "500000",
                      "whol_loan_rdmp_stcn": "300000",
                      "whol_loan_rmnd_stcn": "12000000",
                      "whol_loan_rmnd_amt": "840000000000",
                      "whol_loan_rmnd_rate": "0.21",
                      "whol_stln_rmnd_stcn": "20000",
                      "whol_stln_rmnd_amt": "1400000000",
                      "whol_stln_rmnd_rate": "0.01"
                    }
                  ]
                }
                """, KisFhpst04760000DailyCreditBalanceResponse.class));
        when(quotationApiClient.inquireDailyLoanTrans(
                new KisHhpst074500C0DailyLoanTransRequest("1", "005930", "20260520", "20260602", "")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": [
                    {
                      "bsop_date": "20260602",
                      "stck_prpr": "70000",
                      "new_stcn": "900000",
                      "rdmp_stcn": "400000",
                      "prdy_rmnd_vrss": "500000",
                      "rmnd_stcn": "35000000",
                      "rmnd_amt": "2450000000000"
                    }
                  ]
                }
                """, KisHhpst074500C0DailyLoanTransResponse.class));
        when(rankingApiClient.inquireShortSaleRank(
                new KisFhpst04820000ShortSaleRankRequest("0", "J", "20482", "0000", "D", "0", "0", "0", "0", "0")
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
                      "prdy_vrss": "-1000",
                      "prdy_vrss_sign": "5",
                      "prdy_ctrt": "-1.41",
                      "ssts_cntg_qty": "420000",
                      "ssts_vol_rlim": "3.40",
                      "ssts_tr_pbmn": "29400000000",
                      "ssts_tr_pbmn_rlim": "3.15"
                    }
                  ]
                }
                """, KisFhpst04820000ShortSaleRankResponse.class));
        when(rankingApiClient.inquireCreditBalanceRank(
                new KisFhkst17010000CreditBalanceRankRequest("11701", "0000", "0", "J", "0")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output2": [
                    {
                      "mksc_shrn_iscd": "005930",
                      "hts_kor_isnm": "삼성전자",
                      "stck_prpr": "70000",
                      "prdy_vrss": "-1000",
                      "prdy_vrss_sign": "5",
                      "prdy_ctrt": "-1.41",
                      "whol_loan_rmnd_stcn": "12000000",
                      "whol_loan_rmnd_amt": "840000000000",
                      "whol_loan_rmnd_rate": "0.21",
                      "whol_stln_rmnd_stcn": "20000",
                      "whol_stln_rmnd_amt": "1400000000",
                      "whol_stln_rmnd_rate": "0.01",
                      "nday_vrss_loan_rmnd_inrt": "5.25",
                      "nday_vrss_stln_rmnd_inrt": "1.20"
                    }
                  ]
                }
                """, KisFhkst17010000CreditBalanceRankResponse.class));

        ShortCreditLoanContextDto result = service.getShortCreditLoanContext("005930", "2026-05-20", "2026-06-02", "J");

        assertThat(result.meta().toolName()).isEqualTo("GET_SHORT_CREDIT_LOAN_CONTEXT");
        assertThat(result.meta().kisApiCalls()).hasSize(5);
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().market()).isEqualTo("J");
        assertThat(result.shortSaleTrend().getFirst().date()).isEqualTo("20260602");
        assertThat(result.shortSaleTrend().getFirst().shortSaleQuantity()).isEqualTo(420000L);
        assertThat(result.shortSaleTrend().getFirst().shortSaleAmount()).isEqualByComparingTo(new BigDecimal("29400000000"));
        assertThat(result.creditBalanceTrend().getFirst().loanBalanceQuantity()).isEqualTo(12000000L);
        assertThat(result.creditBalanceTrend().getFirst().loanBalanceRate()).isEqualByComparingTo(new BigDecimal("0.21"));
        assertThat(result.loanTransactionTrend().getFirst().loanBalanceQuantity()).isEqualTo(35000000L);
        assertThat(result.loanTransactionTrend().getFirst().loanBalanceChange()).isEqualTo(500000L);
        assertThat(result.shortSaleRankCandidates().getFirst().rank()).isEqualTo(1);
        assertThat(result.shortSaleRankCandidates().getFirst().stockCode()).isEqualTo("005930");
        assertThat(result.shortSaleRankCandidates().getFirst().signal()).isEqualTo("SHORT_SALE_VOLUME_RATIO");
        assertThat(result.creditBalanceRankCandidates().getFirst().signal()).isEqualTo("CREDIT_BALANCE_INCREASE_RATE");
        assertThat(result.riskSummary().latestShortSaleQuantity()).isEqualTo(420000L);
        assertThat(result.riskSummary().latestCreditLoanBalanceQuantity()).isEqualTo(12000000L);
        assertThat(result.riskSummary().latestLoanTransactionBalanceQuantity()).isEqualTo(35000000L);
        assertThat(result.cautions()).contains(
                "공매도, 신용잔고, 대차거래는 원인 확정 근거가 아니라 수급 리스크 후보입니다.",
                "대차잔고 증가는 실제 공매도 체결과 다릅니다."
        );

        verify(quotationApiClient).inquireDailyShortSale(new KisFhpst04830000DailyShortSaleRequest("20260602", "J", "005930", "20260520"));
        verify(quotationApiClient).inquireDailyCreditBalance(new KisFhpst04760000DailyCreditBalanceRequest("J", "20476", "005930", "20260520"));
        verify(quotationApiClient).inquireDailyLoanTrans(new KisHhpst074500C0DailyLoanTransRequest("1", "005930", "20260520", "20260602", ""));
        verify(rankingApiClient).inquireShortSaleRank(new KisFhpst04820000ShortSaleRankRequest("0", "J", "20482", "0000", "D", "0", "0", "0", "0", "0"));
        verify(rankingApiClient).inquireCreditBalanceRank(new KisFhkst17010000CreditBalanceRankRequest("11701", "0000", "0", "J", "0"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
