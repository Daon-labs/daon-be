package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 프로그램매매 종합현황(시간)
 * TR ID: FHPPG04600101
 * URL: /uapi/domestic-stock/v1/quotations/comp-program-trade-today
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhppg04600101CompProgramTradeTodayRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 시장 분류 코드
        @JsonProperty("FID_MRKT_CLS_CODE") String fidMrktClsCode, // 시장 구분 코드
        @JsonProperty("FID_SCTN_CLS_CODE") String fidSctnClsCode, // 구간 구분 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_COND_MRKT_DIV_CODE1") String fidCondMrktDivCode1, // 시장 분류코드1
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1 // 입력 시간1
) {
}
