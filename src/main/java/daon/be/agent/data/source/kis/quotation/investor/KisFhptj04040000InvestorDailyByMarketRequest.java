package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 시장별 투자자매매동향(일별)
 * TR ID: FHPTJ04040000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-investor-daily-by-market
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04040000InvestorDailyByMarketRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력 날짜1
        @JsonProperty("FID_INPUT_ISCD_1") String fidInputIscd1, // 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_2") String fidInputDate2, // 입력 날짜2
        @JsonProperty("FID_INPUT_ISCD_2") String fidInputIscd2 // 하위 분류코드
) {
}
