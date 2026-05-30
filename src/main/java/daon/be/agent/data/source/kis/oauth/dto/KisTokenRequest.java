package daon.be.agent.data.source.kis.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record KisTokenRequest(
        @JsonProperty("grant_type") String grantType,
        String appkey,
        String appsecret
) {
}
