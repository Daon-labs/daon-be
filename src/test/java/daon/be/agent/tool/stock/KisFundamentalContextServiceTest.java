package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.finance.KisFinanceApiClient;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430300FinancialRatioRequest;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430300FinancialRatioResponse;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430600StabilityRatioRequest;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430600StabilityRatioResponse;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430100BalanceSheetRequest;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430100BalanceSheetResponse;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430200IncomeStatementRequest;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430200IncomeStatementResponse;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.FundamentalContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisFundamentalContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisFinanceApiClient financeApiClient = mock(KisFinanceApiClient.class);
    private final KisFundamentalContextService service = new KisFundamentalContextService(quotationApiClient, financeApiClient);

    @Test
    void getFundamentalContextCombinesIdentityFinancialStatementsAndQualityNotes() throws Exception {
        when(quotationApiClient.searchStockInfo(new KisCtpf1002rSearchStockInfoRequest("300", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": {
                            "pdno": "005930",
                            "prdt_name": "삼성전자",
                            "prdt_abrv_name": "삼성전자",
                            "mket_id_cd": "STK",
                            "excg_dvsn_cd": "KRX",
                            "idx_bztp_scls_cd": "013",
                            "idx_bztp_scls_cd_name": "전기전자",
                            "lstg_stqt": "5969782550",
                            "lstg_cptl_amt": "897514000000",
                            "papr": "100",
                            "scts_mket_lstg_dt": "19750611",
                            "tr_stop_yn": "N",
                            "admn_item_yn": "N",
                            "cptt_trad_tr_psbl_yn": "Y"
                          }
                        }
                        """, KisCtpf1002rSearchStockInfoResponse.class));
        when(financeApiClient.inquireFinancialRatio(new KisFhkst66430300FinancialRatioRequest("0", "J", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": [
                            {
                              "stac_yymm": "202512",
                              "grs": "12.34",
                              "bsop_prfi_inrt": "99.99",
                              "ntin_inrt": "-5.50",
                              "roe_val": "8.20",
                              "eps": "5200",
                              "sps": "38000",
                              "bps": "46000",
                              "rsrv_rate": "1200.50",
                              "lblt_rate": "32.10"
                            }
                          ]
                        }
                        """, KisFhkst66430300FinancialRatioResponse.class));
        when(financeApiClient.inquireIncomeStatement(new KisFhkst66430200IncomeStatementRequest("0", "J", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": [
                            {
                              "stac_yymm": "202512",
                              "sale_account": "300000000000000",
                              "sale_totl_prfi": "120000000000000",
                              "bsop_prti": "40000000000000",
                              "thtr_ntin": "35000000000000"
                            }
                          ]
                        }
                        """, KisFhkst66430200IncomeStatementResponse.class));
        when(financeApiClient.inquireBalanceSheet(new KisFhkst66430100BalanceSheetRequest("0", "J", "005930")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": [
                            {
                              "stac_yymm": "202512",
                              "cras": "150000000000000",
                              "fxas": "250000000000000",
                              "total_aset": "400000000000000",
                              "flow_lblt": "45000000000000",
                              "fix_lblt": "55000000000000",
                              "total_lblt": "100000000000000",
                              "cpfn": "900000000000",
                              "prfi_surp": "250000000000000",
                              "total_cptl": "300000000000000"
                            }
                          ]
                        }
                        """, KisFhkst66430100BalanceSheetResponse.class));
        when(financeApiClient.inquireStabilityRatio(new KisFhkst66430600StabilityRatioRequest("005930", "0", "J")))
                .thenReturn(read("""
                        {
                          "rt_cd": "0",
                          "msg_cd": "OK",
                          "msg1": "정상처리",
                          "output": [
                            {
                              "stac_yymm": "202512",
                              "lblt_rate": "32.10",
                              "bram_depn": "4.50",
                              "crnt_rate": "260.20",
                              "quck_rate": "210.10"
                            }
                          ]
                        }
                        """, KisFhkst66430600StabilityRatioResponse.class));

        FundamentalContextDto result = service.getFundamentalContext("005930");

        assertThat(result.meta().toolName()).isEqualTo("GET_FUNDAMENTAL_CONTEXT");
        assertThat(result.meta().kisApiCalls()).hasSize(5);
        assertThat(result.identity().stockCode()).isEqualTo("005930");
        assertThat(result.identity().stockName()).isEqualTo("삼성전자");
        assertThat(result.identity().industryName()).isEqualTo("전기전자");
        assertThat(result.identity().listedShares()).isEqualTo(5969782550L);
        assertThat(result.identity().nxtTradable()).isTrue();
        assertThat(result.financialRatios()).hasSize(1);
        assertThat(result.financialRatios().getFirst().salesGrowthRate()).isEqualByComparingTo(new BigDecimal("12.34"));
        assertThat(result.financialRatios().getFirst().operatingProfitGrowthRate()).isNull();
        assertThat(result.incomeStatements().getFirst().operatingProfit()).isEqualByComparingTo(new BigDecimal("40000000000000"));
        assertThat(result.balanceSheets().getFirst().totalAssets()).isEqualByComparingTo(new BigDecimal("400000000000000"));
        assertThat(result.stabilityRatios().getFirst().currentRatio()).isEqualByComparingTo(new BigDecimal("260.20"));
        assertThat(result.summary().latestPeriod()).isEqualTo("202512");
        assertThat(result.summary().eps()).isEqualByComparingTo(new BigDecimal("5200"));
        assertThat(result.summary().roe()).isEqualByComparingTo(new BigDecimal("8.20"));
        assertThat(result.summary().debtRatio()).isEqualByComparingTo(new BigDecimal("32.10"));
        assertThat(result.summary().operatingProfit()).isEqualByComparingTo(new BigDecimal("40000000000000"));
        assertThat(result.dataQualityNotes()).contains("202512 영업 이익 증가율은 KIS 비정상 표시값 99.99로 판단해 null 처리했습니다.");

        verify(quotationApiClient).searchStockInfo(new KisCtpf1002rSearchStockInfoRequest("300", "005930"));
        verify(financeApiClient).inquireFinancialRatio(new KisFhkst66430300FinancialRatioRequest("0", "J", "005930"));
        verify(financeApiClient).inquireIncomeStatement(new KisFhkst66430200IncomeStatementRequest("0", "J", "005930"));
        verify(financeApiClient).inquireBalanceSheet(new KisFhkst66430100BalanceSheetRequest("0", "J", "005930"));
        verify(financeApiClient).inquireStabilityRatio(new KisFhkst66430600StabilityRatioRequest("005930", "0", "J"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
