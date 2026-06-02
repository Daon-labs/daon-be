package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 공매도 일별추이
 * TR ID: FHPST04830000
 * URL: /uapi/domestic-stock/v1/quotations/daily-short-sale
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04830000DailyShortSaleResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("prdy_vol") String prdyVol // 전일 거래량
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("stck_clpr") String stckClpr, // 주식 종가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("stnd_vol_smtn") String stndVolSmtn, // 기준 거래량 합계
            @JsonProperty("ssts_cntg_qty") String sstsCntgQty, // 공매도 체결 수량
            @JsonProperty("ssts_vol_rlim") String sstsVolRlim, // 공매도 거래량 비중
            @JsonProperty("acml_ssts_cntg_qty") String acmlSstsCntgQty, // 누적 공매도 체결 수량
            @JsonProperty("acml_ssts_cntg_qty_rlim") String acmlSstsCntgQtyRlim, // 누적 공매도 체결 수량 비중
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("stnd_tr_pbmn_smtn") String stndTrPbmnSmtn, // 기준 거래대금 합계
            @JsonProperty("ssts_tr_pbmn") String sstsTrPbmn, // 공매도 거래 대금
            @JsonProperty("ssts_tr_pbmn_rlim") String sstsTrPbmnRlim, // 공매도 거래대금 비중
            @JsonProperty("acml_ssts_tr_pbmn") String acmlSstsTrPbmn, // 누적 공매도 거래 대금
            @JsonProperty("acml_ssts_tr_pbmn_rlim") String acmlSstsTrPbmnRlim, // 누적 공매도 거래 대금 비중
            @JsonProperty("stck_oprc") String stckOprc, // 주식 시가2
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("stck_lwpr") String stckLwpr, // 주식 최저가
            @JsonProperty("avrg_prc") String avrgPrc // 평균가격
    ) {
    }
}
