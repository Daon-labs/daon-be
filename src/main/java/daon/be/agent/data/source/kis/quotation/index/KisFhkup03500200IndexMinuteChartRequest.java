package daon.be.agent.data.source.kis.quotation.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 업종 분봉조회
 * TR ID: FHKUP03500200
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-indexchartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkup03500200IndexMinuteChartRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // FID 조건 시장 분류 코드
        @JsonProperty("FID_ETC_CLS_CODE") String fidEtcClsCode, // FID 기타 구분 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // FID 입력 종목코드
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1, // FID 입력 시간1
        @JsonProperty("FID_PW_DATA_INCU_YN") String fidPwDataIncuYn // FID 과거 데이터 포함 여부
) {
}
