package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내기관/외국인 매매종목가집계
 * TR ID: FHPTJ04400000
 * URL: /uapi/domestic-stock/v1/quotations/foreign-institution-total
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04400000ForeignInstitutionTotalRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 시장 분류 코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_DIV_CLS_CODE") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("FID_RANK_SORT_CLS_CODE") String fidRankSortClsCode, // 순위 정렬 구분 코드
        @JsonProperty("FID_ETC_CLS_CODE") String fidEtcClsCode // 기타 구분  정렬
) {
}
