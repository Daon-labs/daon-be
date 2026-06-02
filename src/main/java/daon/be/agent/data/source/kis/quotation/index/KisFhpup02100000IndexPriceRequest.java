package daon.be.agent.data.source.kis.quotation.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내업종 현재지수
 * TR ID: FHPUP02100000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-index-price
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpup02100000IndexPriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // FID 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd // FID 입력 종목코드
) {
}
