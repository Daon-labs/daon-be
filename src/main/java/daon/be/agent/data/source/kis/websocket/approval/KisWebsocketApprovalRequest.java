package daon.be.agent.data.source.kis.websocket.approval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 실시간 웹소켓 접속키 발급
 * TR ID: -
 * URL: /oauth2/Approval
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisWebsocketApprovalRequest(
        @JsonProperty("grant_type") String grantType, // 권한부여타입
        @JsonProperty("appkey") String appkey, // 앱키
        @JsonProperty("secretkey") String secretkey // 시크릿키
) {
}
