package daon.be.agent.data.source.kis.oauth;

import daon.be.agent.data.source.kis.KisProperties;
import daon.be.agent.data.source.kis.oauth.dto.KisTokenRequest;
import daon.be.agent.data.source.kis.oauth.dto.KisTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class KisTokenService {

    private final RestClient restClient;
    private final KisProperties kisProps;
    private String cachedToken;

    private static final DateTimeFormatter TOKEN_EXPIRED_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final long TOKEN_EXPIRATION_BUFFER_SECONDS = 60; // 토큰 만료 60초 전부터 새 토큰 발급
    private Instant tokenExpiresAt;

    public KisTokenService(KisProperties kisProps) {
        this.kisProps = kisProps;

        // 토큰 발급 전용 클라이언트는 독립적으로 생성하여 순환 참조 방지
        this.restClient = RestClient.builder()
                .baseUrl(kisProps.baseUrl())
                .build();
    }

    public synchronized String getAccessToken() {
        if (isTokenValid()) {
            return cachedToken;
        }

        KisTokenRequest requestBody = KisTokenRequest.builder()
                .grantType("client_credentials")
                .appkey(kisProps.appkey())
                .appsecret(kisProps.appsecret())
                .build();

        KisTokenResponse response = restClient.post()
                .uri("/oauth2/tokenP")
                .body(requestBody)
                .retrieve()
                .body(KisTokenResponse.class);

        if (response == null) {
            throw new IllegalStateException("KIS 토큰 발급 응답이 올바르지 않습니다.");
        }

        this.cachedToken = response.accessToken();
        this.tokenExpiresAt = parseTokenExpiresAt(response.accessTokenExpiredAt());

        return this.cachedToken;
    }

    private boolean isTokenValid() {
        return cachedToken != null
                && tokenExpiresAt != null
                && Instant.now().isBefore(tokenExpiresAt.minusSeconds(TOKEN_EXPIRATION_BUFFER_SECONDS));
    }

    private Instant parseTokenExpiresAt(String accessTokenExpiredAt) {
        LocalDateTime localDateTime = LocalDateTime.parse(
                accessTokenExpiredAt,
                TOKEN_EXPIRED_AT_FORMATTER
        );

        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }
}
