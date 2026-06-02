package daon.be.agent.data.source.kis.ranking.attention;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: HTS조회상위20종목
 * TR ID: HHMCM000100C0
 * URL: /uapi/domestic-stock/v1/ranking/hts-top-view
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhmcm000100C0HtsTopViewRequest() {
}
