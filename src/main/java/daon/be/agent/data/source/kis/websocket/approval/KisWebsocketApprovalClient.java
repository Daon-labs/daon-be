package daon.be.agent.data.source.kis.websocket.approval;

import daon.be.agent.data.source.kis.KisProperties;
import daon.be.agent.data.source.kis.common.KisEndpoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KisWebsocketApprovalClient {

    private final RestClient restClient;
    private final KisProperties kisProperties;

    public KisWebsocketApprovalClient(RestClient.Builder builder, KisProperties kisProperties) {
        this.restClient = builder
                .baseUrl(kisProperties.baseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.kisProperties = kisProperties;
    }

    public KisWebsocketApprovalResponse issueApprovalKey() {
        KisWebsocketApprovalRequest request = new KisWebsocketApprovalRequest(
                "client_credentials",
                kisProperties.appkey(),
                kisProperties.appsecret()
        );

        return restClient.post()
                .uri(KisEndpoint.WEBSOCKET_APPROVAL.url())
                .body(request)
                .retrieve()
                .body(KisWebsocketApprovalResponse.class);
    }
}
