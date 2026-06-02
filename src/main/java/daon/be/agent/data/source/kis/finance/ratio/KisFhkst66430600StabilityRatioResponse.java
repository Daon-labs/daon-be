package daon.be.agent.data.source.kis.finance.ratio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 안정성비율
 * TR ID: FHKST66430600
 * URL: /uapi/domestic-stock/v1/finance/stability-ratio
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430600StabilityRatioResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stac_yymm") String stacYymm, // 결산 년월
            @JsonProperty("lblt_rate") String lbltRate, // 부채 비율
            @JsonProperty("bram_depn") String bramDepn, // 차입금 의존도
            @JsonProperty("crnt_rate") String crntRate, // 유동 비율
            @JsonProperty("quck_rate") String quckRate // 당좌 비율
    ) {
    }
}
