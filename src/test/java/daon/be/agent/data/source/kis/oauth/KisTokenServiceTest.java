package daon.be.agent.data.source.kis.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(properties = "spring.ai.openai.api-key=test")
@EnabledIfEnvironmentVariable(named = "KIS_APPKEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "KIS_APPSECRET", matches = ".+")
class KisTokenServiceTest {

    @Autowired
    private KisTokenService kisTokenService;

    // 캐시된 KIS accessToken이 없으면 KIS 토큰 API를 호출하고 토큰 반환
    @Test
    void getAccessToken_isCachedFalse() {
        // given

        // when
        String accessToken = kisTokenService.getAccessToken();

        // then
        log.info("Access Token: {}", accessToken);
        assertThat(accessToken).isNotEmpty();

    }
}
