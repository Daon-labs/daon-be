package daon.be.agent.data.source.kis.finance.ratio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 재무비율
 * TR ID: FHKST66430300
 * URL: /uapi/domestic-stock/v1/finance/financial-ratio
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430300FinancialRatioResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stac_yymm") String stacYymm, // 결산 년월
            @JsonProperty("grs") String grs, // 매출액 증가율
            @JsonProperty("bsop_prfi_inrt") String bsopPrfiInrt, // 영업 이익 증가율
            @JsonProperty("ntin_inrt") String ntinInrt, // 순이익 증가율
            @JsonProperty("roe_val") String roeVal, // ROE 값
            @JsonProperty("eps") String eps, // EPS
            @JsonProperty("sps") String sps, // 주당매출액
            @JsonProperty("bps") String bps, // BPS
            @JsonProperty("rsrv_rate") String rsrvRate, // 유보 비율
            @JsonProperty("lblt_rate") String lbltRate // 부채 비율
    ) {
    }
}
