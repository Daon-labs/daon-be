package daon.be.agent.data.source.kis.ranking.trend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 신고/신저근접종목 상위
 * TR ID: FHPST01870000
 * URL: /uapi/domestic-stock/v1/ranking/near-new-highlow
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01870000NearNewHighLowResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("askp") String askp, // 매도호가
            @JsonProperty("askp_rsqn1") String askpRsqn1, // 매도호가 잔량1
            @JsonProperty("bidp") String bidp, // 매수호가
            @JsonProperty("bidp_rsqn1") String bidpRsqn1, // 매수호가 잔량1
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("new_hgpr") String newHgpr, // 신 최고가
            @JsonProperty("hprc_near_rate") String hprcNearRate, // 고가 근접 비율
            @JsonProperty("new_lwpr") String newLwpr, // 신 최저가
            @JsonProperty("lwpr_near_rate") String lwprNearRate, // 저가 근접 비율
            @JsonProperty("stck_sdpr") String stckSdpr // 주식 기준가
    ) {
    }
}
