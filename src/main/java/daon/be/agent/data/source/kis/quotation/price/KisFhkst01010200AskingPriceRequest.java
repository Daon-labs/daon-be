package daon.be.agent.data.source.kis.quotation.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식현재가 호가/예상체결
 * TR ID: FHKST01010200
 * URL: /uapi/domestic-stock/v1/quotations/inquire-asking-price-exp-ccn
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst01010200AskingPriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd // 입력 종목코드
) {
}
