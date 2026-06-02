package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 공매도 일별추이
 * TR ID: FHPST04830000
 * URL: /uapi/domestic-stock/v1/quotations/daily-short-sale
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04830000DailyShortSaleRequest(
        @JsonProperty("FID_INPUT_DATE_2") String fidInputDate2, // 입력 날짜2
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1 // 입력 날짜1
) {
}
