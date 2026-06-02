package daon.be.agent.data.source.kis.websocket.marketstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 장운영정보 (통합)
 * TR ID: H0UNMKO0
 * URL: /tryitout/H0UNMKO0
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisH0unmko0MarketOperationInfoRequest(
        @JsonProperty("tr_key") String trKey // 구분값
) {
}
