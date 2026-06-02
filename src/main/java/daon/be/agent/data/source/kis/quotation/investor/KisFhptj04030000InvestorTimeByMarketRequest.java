package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 시장별 투자자매매동향(시세)
 * TR ID: FHPTJ04030000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-investor-time-by-market
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04030000InvestorTimeByMarketRequest(
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 시장구분
        @JsonProperty("fid_input_iscd_2") String fidInputIscd2 // 업종구분
) {
}
