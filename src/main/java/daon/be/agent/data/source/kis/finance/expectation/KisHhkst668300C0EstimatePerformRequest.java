package daon.be.agent.data.source.kis.finance.expectation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 종목추정실적
 * TR ID: HHKST668300C0
 * URL: /uapi/domestic-stock/v1/quotations/estimate-perform
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhkst668300C0EstimatePerformRequest(
        @JsonProperty("SHT_CD") String shtCd // 종목코드
) {
}
