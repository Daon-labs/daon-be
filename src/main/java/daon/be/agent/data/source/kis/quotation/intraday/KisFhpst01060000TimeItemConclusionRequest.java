package daon.be.agent.data.source.kis.quotation.intraday;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식현재가 당일시간대별체결
 * TR ID: FHPST01060000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01060000TimeItemConclusionRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1 // 입력 시간1
) {
}
