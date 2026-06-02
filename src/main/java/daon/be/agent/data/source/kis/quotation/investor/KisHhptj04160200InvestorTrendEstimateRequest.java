package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 종목별 외인기관 추정가집계
 * TR ID: HHPTJ04160200
 * URL: /uapi/domestic-stock/v1/quotations/investor-trend-estimate
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhptj04160200InvestorTrendEstimateRequest(
        @JsonProperty("MKSC_SHRN_ISCD") String mkscShrnIscd // 종목코드
) {
}
