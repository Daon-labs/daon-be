package daon.be.agent.data.source.kis.quotation.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식현재가 시세
 * TR ID: FHKST01010100
 * URL: /uapi/domestic-stock/v1/quotations/inquire-price
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst01010100InquirePriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd // 입력 종목코드
) {
}
