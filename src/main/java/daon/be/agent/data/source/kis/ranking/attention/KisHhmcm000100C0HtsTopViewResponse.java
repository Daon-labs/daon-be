package daon.be.agent.data.source.kis.ranking.attention;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: HTS조회상위20종목
 * TR ID: HHMCM000100C0
 * URL: /uapi/domestic-stock/v1/ranking/hts-top-view
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhmcm000100C0HtsTopViewResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") List<Output1> output1
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("mrkt_div_cls_code") String mrktDivClsCode, // 시장구분
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd // 종목코드
    ) {
    }
}
