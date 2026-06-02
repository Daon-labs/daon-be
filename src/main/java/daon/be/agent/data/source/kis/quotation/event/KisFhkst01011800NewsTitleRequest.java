package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 종합 시황/공시(제목)
 * TR ID: FHKST01011800
 * URL: /uapi/domestic-stock/v1/quotations/news-title
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst01011800NewsTitleRequest(
        @JsonProperty("FID_NEWS_OFER_ENTP_CODE") String fidNewsOferEntpCode, // 뉴스 제공 업체 코드
        @JsonProperty("FID_COND_MRKT_CLS_CODE") String fidCondMrktClsCode, // 조건 시장 구분 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_TITL_CNTT") String fidTitlCntt, // 제목 내용
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력 날짜
        @JsonProperty("FID_INPUT_HOUR_1") String fidInputHour1, // 입력 시간
        @JsonProperty("FID_RANK_SORT_CLS_CODE") String fidRankSortClsCode, // 순위 정렬 구분 코드
        @JsonProperty("FID_INPUT_SRNO") String fidInputSrno // 입력 일련번호
) {
}
