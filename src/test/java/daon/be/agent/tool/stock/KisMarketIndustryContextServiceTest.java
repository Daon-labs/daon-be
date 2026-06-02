package daon.be.agent.tool.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.index.KisFhkup03500200IndexMinuteChartRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhkup03500200IndexMinuteChartResponse;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02100000IndexPriceRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02100000IndexPriceResponse;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02120000IndexDailyPriceRequest;
import daon.be.agent.data.source.kis.quotation.index.KisFhpup02120000IndexDailyPriceResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04030000InvestorTimeByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04030000InvestorTimeByMarketResponse;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketRequest;
import daon.be.agent.data.source.kis.quotation.investor.KisFhptj04040000InvestorDailyByMarketResponse;
import daon.be.agent.tool.stock.dto.MarketIndustryContextDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KisMarketIndustryContextServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KisQuotationApiClient quotationApiClient = mock(KisQuotationApiClient.class);
    private final KisMarketIndustryContextService service = new KisMarketIndustryContextService(quotationApiClient);

    @Test
    void getMarketIndustryContextCombinesIndexChartsAndInvestorFlows() throws Exception {
        when(quotationApiClient.inquireIndexPrice(
                new KisFhpup02100000IndexPriceRequest("U", "0001")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": {
                    "bstp_nmix_prpr": "2780.50",
                    "bstp_nmix_prdy_vrss": "-18.20",
                    "prdy_vrss_sign": "5",
                    "bstp_nmix_prdy_ctrt": "-0.65",
                    "acml_vol": "450000000",
                    "acml_tr_pbmn": "9800000000000",
                    "bstp_nmix_oprc": "2795.00",
                    "bstp_nmix_hgpr": "2801.30",
                    "bstp_nmix_lwpr": "2775.10",
                    "ascn_issu_cnt": "320",
                    "down_issu_cnt": "540",
                    "stnr_issu_cnt": "70"
                  }
                }
                """, KisFhpup02100000IndexPriceResponse.class));
        when(quotationApiClient.inquireIndexMinuteChart(
                new KisFhkup03500200IndexMinuteChartRequest("U", "", "0001", "093000", "N")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "Output1": [
                    {
                      "hts_kor_isnm": "코스피",
                      "bstp_cls_code": "0001",
                      "bstp_nmix_prpr": "2780.50",
                      "bstp_nmix_prdy_ctrt": "-0.65"
                    }
                  ],
                  "Output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "stck_cntg_hour": "093000",
                      "bstp_nmix_oprc": "2795.00",
                      "bstp_nmix_hgpr": "2798.20",
                      "bstp_nmix_lwpr": "2788.40",
                      "bstp_nmix_prpr": "2790.10",
                      "cntg_vol": "12000000",
                      "acml_tr_pbmn": "250000000000"
                    }
                  ]
                }
                """, KisFhkup03500200IndexMinuteChartResponse.class));
        when(quotationApiClient.inquireIndexDailyPrice(
                new KisFhpup02120000IndexDailyPriceRequest("D", "U", "0001", "20260520")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output1": {
                    "bstp_nmix_prpr": "2780.50",
                    "bstp_nmix_prdy_ctrt": "-0.65"
                  },
                  "output2": [
                    {
                      "stck_bsop_date": "20260602",
                      "bstp_nmix_oprc": "2795.00",
                      "bstp_nmix_hgpr": "2801.30",
                      "bstp_nmix_lwpr": "2775.10",
                      "bstp_nmix_prpr": "2780.50",
                      "acml_vol": "450000000",
                      "acml_tr_pbmn": "9800000000000",
                      "bstp_nmix_prdy_ctrt": "-0.65"
                    }
                  ]
                }
                """, KisFhpup02120000IndexDailyPriceResponse.class));
        when(quotationApiClient.inquireInvestorTimeByMarket(
                new KisFhptj04030000InvestorTimeByMarketRequest("0001", "0000")
        )).thenReturn(read("""
                {
                  "rt_cd": "0",
                  "msg_cd": "OK",
                  "msg1": "정상처리",
                  "output": [
                    {
                      "frgn_ntby_qty": "-120000",
                      "frgn_ntby_tr_pbmn": "-35000000000",
                      "prsn_ntby_qty": "150000",
                      "prsn_ntby_tr_pbmn": "42000000000",
                      "orgn_ntby_qty": "-30000",
                      "orgn_ntby_tr_pbmn": "-7000000000"
                    }
                  ]
                }
                """, KisFhptj04030000InvestorTimeByMarketResponse.class));
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
                      "bstp_nmix_prpr": "2780.50",
                      "bstp_nmix_prdy_ctrt": "-0.65",
                      "frgn_ntby_qty": "-120000",
                      "frgn_ntby_tr_pbmn": "-35000000000",
                      "prsn_ntby_qty": "150000",
                      "prsn_ntby_tr_pbmn": "42000000000",
                      "orgn_ntby_qty": "-30000",
                      "orgn_ntby_tr_pbmn": "-7000000000"
                    }
                  ]
                }
                """, KisFhptj04040000InvestorDailyByMarketResponse.class));

        MarketIndustryContextDto result = service.getMarketIndustryContext("U", "0001", "093000", "2026-05-20");

        assertThat(result.meta().toolName()).isEqualTo("GET_MARKET_INDUSTRY_CONTEXT");
        assertThat(result.market()).isEqualTo("KOSPI");
        assertThat(result.industrySnapshot().industryCode()).isEqualTo("0001");
        assertThat(result.industrySnapshot().industryName()).isEqualTo("코스피");
        assertThat(result.industrySnapshot().currentIndex()).isEqualByComparingTo(new BigDecimal("2780.50"));
        assertThat(result.industryMinuteBars()).hasSize(1);
        assertThat(result.industryMinuteBars().getFirst().time()).isEqualTo("093000");
        assertThat(result.industryDailyBars()).hasSize(1);
        assertThat(result.intradayMarketInvestorFlow().foreignNetBuyAmount()).isEqualByComparingTo(new BigDecimal("-35000000000"));
        assertThat(result.dailyMarketInvestorFlows()).hasSize(1);
        assertThat(result.relativeStrength().industryChangeRate()).isEqualByComparingTo(new BigDecimal("-0.65"));
        assertThat(result.interpretationHints()).contains("시장/업종 수급은 시장별 투자자매매동향 기준이며 종목별 수급과 다를 수 있습니다.");
        assertThat(result.meta().kisApiCalls()).hasSize(5);

        verify(quotationApiClient).inquireIndexPrice(new KisFhpup02100000IndexPriceRequest("U", "0001"));
        verify(quotationApiClient).inquireIndexMinuteChart(new KisFhkup03500200IndexMinuteChartRequest("U", "", "0001", "093000", "N"));
        verify(quotationApiClient).inquireIndexDailyPrice(new KisFhpup02120000IndexDailyPriceRequest("D", "U", "0001", "20260520"));
        verify(quotationApiClient).inquireInvestorTimeByMarket(new KisFhptj04030000InvestorTimeByMarketRequest("0001", "0000"));
        verify(quotationApiClient).inquireInvestorDailyByMarket(new KisFhptj04040000InvestorDailyByMarketRequest("U", "0001", "20260520", "KSP", "20260520", "0001"));
    }

    private <T> T read(String json, Class<T> type) throws Exception {
        return objectMapper.readValue(json, type);
    }
}
