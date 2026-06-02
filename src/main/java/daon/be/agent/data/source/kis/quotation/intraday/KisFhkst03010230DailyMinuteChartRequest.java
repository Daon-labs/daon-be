package daon.be.agent.data.source.kis.quotation.intraday;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식일별분봉조회
 * TR ID: FHKST03010230
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-dailychartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst03010230DailyMinuteChartRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1, // 입력 시간1
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력 날짜1
        @JsonProperty("FID_PW_DATA_INCU_YN") String fidPwDataIncuYn, // 과거 데이터 포함 여부
        @JsonProperty("FID_FAKE_TICK_INCU_YN") String fidFakeTickIncuYn // 허봉 포함 여부
) {
}
