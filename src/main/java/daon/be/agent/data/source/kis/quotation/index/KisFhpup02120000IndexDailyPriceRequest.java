package daon.be.agent.data.source.kis.quotation.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내업종 일자별지수
 * TR ID: FHPUP02120000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-index-daily-price
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpup02120000IndexDailyPriceRequest(
        @JsonProperty("FID_PERIOD_DIV_CODE") String fidPeriodDivCode, // FID 기간 분류 코드
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // FID 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // FID 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1 // FID 입력 날짜1
) {
}
