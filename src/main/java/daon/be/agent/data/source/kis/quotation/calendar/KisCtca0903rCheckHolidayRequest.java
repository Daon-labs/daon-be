package daon.be.agent.data.source.kis.quotation.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내휴장일조회
 * TR ID: CTCA0903R
 * URL: /uapi/domestic-stock/v1/quotations/chk-holiday
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisCtca0903rCheckHolidayRequest(
        @JsonProperty("BASS_DT") String bassDt, // 기준일자
        @JsonProperty("CTX_AREA_NK") String ctxAreaNk, // 연속조회키
        @JsonProperty("CTX_AREA_FK") String ctxAreaFk // 연속조회검색조건
) {
}
