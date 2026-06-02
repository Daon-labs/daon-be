package daon.be.agent.data.source.kis.quotation.daily;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식기간별시세(일/주/월/년)
 * TR ID: FHKST03010100
 * URL: /uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst03010100DailyItemChartPriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력 날짜 1
        @JsonProperty("FID_INPUT_DATE_2") String fidInputDate2, // 입력 날짜 2
        @JsonProperty("FID_PERIOD_DIV_CODE") String fidPeriodDivCode, // 기간분류코드
        @JsonProperty("FID_ORG_ADJ_PRC") String fidOrgAdjPrc // 수정주가 원주가 가격 여부
) {
}
