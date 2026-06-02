package daon.be.agent.data.source.kis.finance.expectation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 종목투자의견
 * TR ID: FHKST663300C0
 * URL: /uapi/domestic-stock/v1/quotations/invest-opinion
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst663300C0InvestOpinionRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건시장분류코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건화면분류코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력날짜1
        @JsonProperty("FID_INPUT_DATE_2") String fidInputDate2 // 입력날짜2
) {
}
