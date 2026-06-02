package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 종목별 일별 대차거래추이
 * TR ID: HHPST074500C0
 * URL: /uapi/domestic-stock/v1/quotations/daily-loan-trans
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhpst074500C0DailyLoanTransRequest(
        @JsonProperty("MRKT_DIV_CLS_CODE") String mrktDivClsCode, // 조회구분
        @JsonProperty("MKSC_SHRN_ISCD") String mkscShrnIscd, // 종목코드
        @JsonProperty("START_DATE") String startDate, // 조회시작일시
        @JsonProperty("END_DATE") String endDate, // 조회종료일시
        @JsonProperty("CTS") String cts // 이전조회KEY
) {
}
