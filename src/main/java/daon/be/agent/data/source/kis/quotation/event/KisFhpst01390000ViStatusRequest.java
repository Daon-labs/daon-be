package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 변동성완화장치(VI) 현황
 * TR ID: FHPST01390000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-vi-status
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01390000ViStatusRequest(
        @JsonProperty("FID_DIV_CLS_CODE") String fidDivClsCode, // FID 분류 구분 코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // FID 조건 화면 분류 코드
        @JsonProperty("FID_MRKT_CLS_CODE") String fidMrktClsCode, // FID 시장 구분 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // FID 입력 종목코드
        @JsonProperty("FID_RANK_SORT_CLS_CODE") String fidRankSortClsCode, // FID 순위 정렬 구분 코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // FID 입력 날짜1
        @JsonProperty("FID_TRGT_CLS_CODE") String fidTrgtClsCode, // FID 대상 구분 코드
        @JsonProperty("FID_TRGT_EXLS_CLS_CODE") String fidTrgtExlsClsCode // FID 대상 제외 구분 코드
) {
}
