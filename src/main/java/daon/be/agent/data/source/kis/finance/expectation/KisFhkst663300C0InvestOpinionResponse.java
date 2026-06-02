package daon.be.agent.data.source.kis.finance.expectation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 종목투자의견
 * TR ID: FHKST663300C0
 * URL: /uapi/domestic-stock/v1/quotations/invest-opinion
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst663300C0InvestOpinionResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식영업일자
            @JsonProperty("invt_opnn") String invtOpnn, // 투자의견
            @JsonProperty("invt_opnn_cls_code") String invtOpnnClsCode, // 투자의견구분코드
            @JsonProperty("rgbf_invt_opnn") String rgbfInvtOpnn, // 직전투자의견
            @JsonProperty("rgbf_invt_opnn_cls_code") String rgbfInvtOpnnClsCode, // 직전투자의견구분코드
            @JsonProperty("mbcr_name") String mbcrName, // 회원사명
            @JsonProperty("hts_goal_prc") String htsGoalPrc, // HTS목표가격
            @JsonProperty("stck_prdy_clpr") String stckPrdyClpr, // 주식전일종가
            @JsonProperty("stck_nday_esdg") String stckNdayEsdg, // 주식N일괴리도
            @JsonProperty("nday_dprt") String ndayDprt, // N일괴리율
            @JsonProperty("stft_esdg") String stftEsdg, // 주식선물괴리도
            @JsonProperty("dprt") String dprt // 괴리율
    ) {
    }
}
