package daon.be.agent.data.source.kis.client;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisQueryParamMapper;
import daon.be.agent.data.source.kis.common.KisTrCont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KisRestApiClient {

    private final RestClient kisRestClient;

    public <T> T get(KisEndpoint endpoint, Object request, Class<T> responseType) {
        return get(endpoint, request, null, responseType);
    }

    public <T> T get(KisEndpoint endpoint, Object request, KisTrCont trCont, Class<T> responseType) {
        return kisRestClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, endpoint, request))
                .headers(headers -> {
                    if (!endpoint.trId().isBlank() && !"-".equals(endpoint.trId())) {
                        headers.set("tr_id", endpoint.trId());
                    }
                    if (trCont != null && !trCont.value().isBlank()) {
                        headers.set("tr_cont", trCont.value());
                    }
                })
                .retrieve()
                .body(responseType);
    }

    private URI buildUri(UriBuilder uriBuilder, KisEndpoint endpoint, Object request) {
        UriBuilder builder = uriBuilder.path(endpoint.url());
        for (KisQueryParamMapper.KisQueryParam queryParam : KisQueryParamMapper.toQueryParams(request)) {
            builder.queryParam(queryParam.name(), queryParam.value());
        }
        return builder.build();
    }
}
