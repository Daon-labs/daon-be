package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 시장별 투자자매매동향(일별)
 * TR ID: FHPTJ04040000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-investor-daily-by-market
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04040000InvestorDailyByMarketResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("bstp_nmix_prdy_vrss") String bstpNmixPrdyVrss, // 업종 지수 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("bstp_nmix_prdy_ctrt") String bstpNmixPrdyCtrt, // 업종 지수 전일 대비율
            @JsonProperty("bstp_nmix_oprc") String bstpNmixOprc, // 업종 지수 시가2
            @JsonProperty("bstp_nmix_hgpr") String bstpNmixHgpr, // 업종 지수 최고가
            @JsonProperty("bstp_nmix_lwpr") String bstpNmixLwpr, // 업종 지수 최저가
            @JsonProperty("stck_prdy_clpr") String stckPrdyClpr, // 주식 전일 종가
            @JsonProperty("frgn_ntby_qty") String frgnNtbyQty, // 외국인 순매수 수량
            @JsonProperty("frgn_reg_ntby_qty") String frgnRegNtbyQty, // 외국인 등록 순매수 수량
            @JsonProperty("frgn_nreg_ntby_qty") String frgnNregNtbyQty, // 외국인 비등록 순매수 수량
            @JsonProperty("prsn_ntby_qty") String prsnNtbyQty, // 개인 순매수 수량
            @JsonProperty("orgn_ntby_qty") String orgnNtbyQty, // 기관계 순매수 수량
            @JsonProperty("scrt_ntby_qty") String scrtNtbyQty, // 증권 순매수 수량
            @JsonProperty("ivtr_ntby_qty") String ivtrNtbyQty, // 투자신탁 순매수 수량
            @JsonProperty("pe_fund_ntby_vol") String peFundNtbyVol, // 사모 펀드 순매수 거래량
            @JsonProperty("bank_ntby_qty") String bankNtbyQty, // 은행 순매수 수량
            @JsonProperty("insu_ntby_qty") String insuNtbyQty, // 보험 순매수 수량
            @JsonProperty("mrbn_ntby_qty") String mrbnNtbyQty, // 종금 순매수 수량
            @JsonProperty("fund_ntby_qty") String fundNtbyQty, // 기금 순매수 수량
            @JsonProperty("etc_ntby_qty") String etcNtbyQty, // 기타 순매수 수량
            @JsonProperty("etc_orgt_ntby_vol") String etcOrgtNtbyVol, // 기타 단체 순매수 거래량
            @JsonProperty("etc_corp_ntby_vol") String etcCorpNtbyVol, // 기타 법인 순매수 거래량
            @JsonProperty("frgn_ntby_tr_pbmn") String frgnNtbyTrPbmn, // 외국인 순매수 거래 대금
            @JsonProperty("frgn_reg_ntby_pbmn") String frgnRegNtbyPbmn, // 외국인 등록 순매수 대금
            @JsonProperty("frgn_nreg_ntby_pbmn") String frgnNregNtbyPbmn, // 외국인 비등록 순매수 대금
            @JsonProperty("prsn_ntby_tr_pbmn") String prsnNtbyTrPbmn, // 개인 순매수 거래 대금
            @JsonProperty("orgn_ntby_tr_pbmn") String orgnNtbyTrPbmn, // 기관계 순매수 거래 대금
            @JsonProperty("scrt_ntby_tr_pbmn") String scrtNtbyTrPbmn, // 증권 순매수 거래 대금
            @JsonProperty("ivtr_ntby_tr_pbmn") String ivtrNtbyTrPbmn, // 투자신탁 순매수 거래 대금
            @JsonProperty("pe_fund_ntby_tr_pbmn") String peFundNtbyTrPbmn, // 사모 펀드 순매수 거래 대금
            @JsonProperty("bank_ntby_tr_pbmn") String bankNtbyTrPbmn, // 은행 순매수 거래 대금
            @JsonProperty("insu_ntby_tr_pbmn") String insuNtbyTrPbmn, // 보험 순매수 거래 대금
            @JsonProperty("mrbn_ntby_tr_pbmn") String mrbnNtbyTrPbmn, // 종금 순매수 거래 대금
            @JsonProperty("fund_ntby_tr_pbmn") String fundNtbyTrPbmn, // 기금 순매수 거래 대금
            @JsonProperty("etc_ntby_tr_pbmn") String etcNtbyTrPbmn, // 기타 순매수 거래 대금
            @JsonProperty("etc_orgt_ntby_tr_pbmn") String etcOrgtNtbyTrPbmn, // 기타 단체 순매수 거래 대금
            @JsonProperty("etc_corp_ntby_tr_pbmn") String etcCorpNtbyTrPbmn // 기타 법인 순매수 거래 대금
    ) {
    }
}
