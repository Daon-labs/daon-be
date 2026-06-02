package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 신용잔고 일별추이
 * TR ID: FHPST04760000
 * URL: /uapi/domestic-stock/v1/quotations/daily-credit-balance
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04760000DailyCreditBalanceRequest(
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode, // 시장 분류 코드
        @JsonProperty("fid_cond_scr_div_code") String fidCondScrDivCode, // 화면 분류 코드
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 종목코드
        @JsonProperty("fid_input_date_1") String fidInputDate1 // 결제일자
) {
}
