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
public record KisWebsocketApprovalResponse(
        @JsonProperty("approval_key") String approvalKey // 웹소켓 접속키
) {
}
