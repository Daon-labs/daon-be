package daon.be.agent.data.source.kis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kis")
public record KisProperties(

        String baseUrl,
        String appkey,
        String appsecret
) {
}
