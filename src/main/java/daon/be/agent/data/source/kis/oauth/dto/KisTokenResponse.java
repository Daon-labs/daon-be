package daon.be.agent.data.source.kis.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KisTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,     // 접근 토큰 유형 (Bearer)
        @JsonProperty("expires_in") long expiresIn,     // 접근 토큰 유효기간
        @JsonProperty("access_token_token_expired") String accessTokenExpiredAt
        // 접근 토큰 유효기간 (YYYY-MM-DD HH:MM:SS 일시 표시)
) {
}
