package daon.be.agent.data.source.kis.quotation.intraday;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식당일분봉조회
 * TR ID: FHKST03010200
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-itemchartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst03010200TodayMinuteChartRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1, // 입력 시간1
        @JsonProperty("FID_PW_DATA_INCU_YN") String fidPwDataIncuYn, // 과거 데이터 포함 여부
        @JsonProperty("FID_ETC_CLS_CODE") String fidEtcClsCode // 기타 구분 코드
) {
}
