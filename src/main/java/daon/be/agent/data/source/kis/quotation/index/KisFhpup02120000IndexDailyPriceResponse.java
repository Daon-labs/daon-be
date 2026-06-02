package daon.be.agent.data.source.kis.quotation.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내업종 일자별지수
 * TR ID: FHPUP02120000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-index-daily-price
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpup02120000IndexDailyPriceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("bstp_nmix_prdy_vrss") String bstpNmixPrdyVrss, // 업종 지수 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("bstp_nmix_prdy_ctrt") String bstpNmixPrdyCtrt, // 업종 지수 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("bstp_nmix_oprc") String bstpNmixOprc, // 업종 지수 시가2
            @JsonProperty("bstp_nmix_hgpr") String bstpNmixHgpr, // 업종 지수 최고가
            @JsonProperty("bstp_nmix_lwpr") String bstpNmixLwpr, // 업종 지수 최저가
            @JsonProperty("prdy_vol") String prdyVol, // 전일 거래량
            @JsonProperty("ascn_issu_cnt") String ascnIssuCnt, // 상승 종목 수
            @JsonProperty("down_issu_cnt") String downIssuCnt, // 하락 종목 수
            @JsonProperty("stnr_issu_cnt") String stnrIssuCnt, // 보합 종목 수
            @JsonProperty("uplm_issu_cnt") String uplmIssuCnt, // 상한 종목 수
            @JsonProperty("lslm_issu_cnt") String lslmIssuCnt, // 하한 종목 수
            @JsonProperty("prdy_tr_pbmn") String prdyTrPbmn, // 전일 거래 대금
            @JsonProperty("dryy_bstp_nmix_hgpr_date") String dryyBstpNmixHgprDate, // 연중업종지수최고가일자
            @JsonProperty("dryy_bstp_nmix_hgpr") String dryyBstpNmixHgpr, // 연중업종지수최고가
            @JsonProperty("dryy_bstp_nmix_lwpr") String dryyBstpNmixLwpr, // 연중업종지수최저가
            @JsonProperty("dryy_bstp_nmix_lwpr_date") String dryyBstpNmixLwprDate // 연중업종지수최저가일자
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("bstp_nmix_prdy_vrss") String bstpNmixPrdyVrss, // 업종 지수 전일 대비
            @JsonProperty("bstp_nmix_prdy_ctrt") String bstpNmixPrdyCtrt, // 업종 지수 전일 대비율
            @JsonProperty("bstp_nmix_oprc") String bstpNmixOprc, // 업종 지수 시가2
            @JsonProperty("bstp_nmix_hgpr") String bstpNmixHgpr, // 업종 지수 최고가
            @JsonProperty("bstp_nmix_lwpr") String bstpNmixLwpr, // 업종 지수 최저가
            @JsonProperty("acml_vol_rlim") String acmlVolRlim, // 누적 거래량 비중
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("invt_new_psdg") String invtNewPsdg, // 투자 신 심리도
            @JsonProperty("d20_dsrt") String d20Dsrt // 20일 이격도
    ) {
    }
}
