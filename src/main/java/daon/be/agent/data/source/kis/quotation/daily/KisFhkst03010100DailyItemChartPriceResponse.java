package daon.be.agent.data.source.kis.quotation.daily;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식기간별시세(일/주/월/년)
 * TR ID: FHKST03010100
 * URL: /uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst03010100DailyItemChartPriceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("stck_prdy_clpr") String stckPrdyClpr, // 주식 전일 종가
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("stck_shrn_iscd") String stckShrnIscd, // 주식 단축 종목코드
            @JsonProperty("prdy_vol") String prdyVol, // 전일 거래량
            @JsonProperty("stck_mxpr") String stckMxpr, // 주식 상한가
            @JsonProperty("stck_llam") String stckLlam, // 주식 하한가
            @JsonProperty("stck_oprc") String stckOprc, // 주식 시가2
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("stck_lwpr") String stckLwpr, // 주식 최저가
            @JsonProperty("stck_prdy_oprc") String stckPrdyOprc, // 주식 전일 시가
            @JsonProperty("stck_prdy_hgpr") String stckPrdyHgpr, // 주식 전일 최고가
            @JsonProperty("stck_prdy_lwpr") String stckPrdyLwpr, // 주식 전일 최저가
            @JsonProperty("askp") String askp, // 매도호가
            @JsonProperty("bidp") String bidp, // 매수호가
            @JsonProperty("prdy_vrss_vol") String prdyVrssVol, // 전일 대비 거래량
            @JsonProperty("vol_tnrt") String volTnrt, // 거래량 회전율
            @JsonProperty("stck_fcam") String stckFcam, // 주식 액면가
            @JsonProperty("lstn_stcn") String lstnStcn, // 상장 주수
            @JsonProperty("cpfn") String cpfn, // 자본금
            @JsonProperty("hts_avls") String htsAvls, // HTS 시가총액
            @JsonProperty("per") String per, // PER
            @JsonProperty("eps") String eps, // EPS
            @JsonProperty("pbr") String pbr, // PBR
            @JsonProperty("itewhol_loan_rmnd_ratem") String itewholLoanRmndRatem // 전체 융자 잔고 비율
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("stck_clpr") String stckClpr, // 주식 종가
            @JsonProperty("stck_oprc") String stckOprc, // 주식 시가2
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("stck_lwpr") String stckLwpr, // 주식 최저가
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("flng_cls_code") String flngClsCode, // 락 구분 코드
            @JsonProperty("prtt_rate") String prttRate, // 분할 비율
            @JsonProperty("mod_yn") String modYn, // 변경 여부
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("revl_issu_reas") String revlIssuReas // 재평가사유코드
    ) {
    }
}
