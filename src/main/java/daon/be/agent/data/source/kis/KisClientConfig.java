package daon.be.agent.data.source.kis;

import daon.be.agent.data.source.kis.oauth.KisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class KisClientConfig {

    private final KisProperties kisProps;
    private final KisTokenService kisTokenService;

    @Bean
    public RestClient kisRestClient(RestClient.Builder builder) {

        return builder
                .baseUrl(kisProps.baseUrl())
                // 공통 헤더 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor((request, body, execution) -> {
                    // token 주입
                    String accessToken = kisTokenService.getAccessToken();
                    request.getHeaders().setBearerAuth(accessToken);
                    // 공통 헤더 주입
                    request.getHeaders().set("appkey", kisProps.appkey());
                    request.getHeaders().set("appsecret", kisProps.appsecret());
                    request.getHeaders().set("custtype", "P");  // 개인(P) / 법인(B) 설정

                    return execution.execute(request, body);
                })
                .build();
    }

}
