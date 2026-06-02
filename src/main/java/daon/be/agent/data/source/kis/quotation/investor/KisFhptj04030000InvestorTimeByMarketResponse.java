package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 시장별 투자자매매동향(시세)
 * TR ID: FHPTJ04030000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-investor-time-by-market
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04030000InvestorTimeByMarketResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("frgn_seln_vol") String frgnSelnVol, // 외국인 매도 거래량
            @JsonProperty("frgn_shnu_vol") String frgnShnuVol, // 외국인 매수2 거래량
            @JsonProperty("frgn_ntby_qty") String frgnNtbyQty, // 외국인 순매수 수량
            @JsonProperty("frgn_seln_tr_pbmn") String frgnSelnTrPbmn, // 외국인 매도 거래 대금
            @JsonProperty("frgn_shnu_tr_pbmn") String frgnShnuTrPbmn, // 외국인 매수2 거래 대금
            @JsonProperty("frgn_ntby_tr_pbmn") String frgnNtbyTrPbmn, // 외국인 순매수 거래 대금
            @JsonProperty("prsn_seln_vol") String prsnSelnVol, // 개인 매도 거래량
            @JsonProperty("prsn_shnu_vol") String prsnShnuVol, // 개인 매수2 거래량
            @JsonProperty("prsn_ntby_qty") String prsnNtbyQty, // 개인 순매수 수량
            @JsonProperty("prsn_seln_tr_pbmn") String prsnSelnTrPbmn, // 개인 매도 거래 대금
            @JsonProperty("prsn_shnu_tr_pbmn") String prsnShnuTrPbmn, // 개인 매수2 거래 대금
            @JsonProperty("prsn_ntby_tr_pbmn") String prsnNtbyTrPbmn, // 개인 순매수 거래 대금
            @JsonProperty("orgn_seln_vol") String orgnSelnVol, // 기관계 매도 거래량
            @JsonProperty("orgn_shnu_vol") String orgnShnuVol, // 기관계 매수2 거래량
            @JsonProperty("orgn_ntby_qty") String orgnNtbyQty, // 기관계 순매수 수량
            @JsonProperty("orgn_seln_tr_pbmn") String orgnSelnTrPbmn, // 기관계 매도 거래 대금
            @JsonProperty("orgn_shnu_tr_pbmn") String orgnShnuTrPbmn, // 기관계 매수2 거래 대금
            @JsonProperty("orgn_ntby_tr_pbmn") String orgnNtbyTrPbmn, // 기관계 순매수 거래 대금
            @JsonProperty("scrt_seln_vol") String scrtSelnVol, // 증권 매도 거래량
            @JsonProperty("scrt_shnu_vol") String scrtShnuVol, // 증권 매수2 거래량
            @JsonProperty("scrt_ntby_qty") String scrtNtbyQty, // 증권 순매수 수량
            @JsonProperty("scrt_seln_tr_pbmn") String scrtSelnTrPbmn, // 증권 매도 거래 대금
            @JsonProperty("scrt_shnu_tr_pbmn") String scrtShnuTrPbmn, // 증권 매수2 거래 대금
            @JsonProperty("scrt_ntby_tr_pbmn") String scrtNtbyTrPbmn, // 증권 순매수 거래 대금
            @JsonProperty("ivtr_seln_vol") String ivtrSelnVol, // 투자신탁 매도 거래량
            @JsonProperty("ivtr_shnu_vol") String ivtrShnuVol, // 투자신탁 매수2 거래량
            @JsonProperty("ivtr_ntby_qty") String ivtrNtbyQty, // 투자신탁 순매수 수량
            @JsonProperty("ivtr_seln_tr_pbmn") String ivtrSelnTrPbmn, // 투자신탁 매도 거래 대금
            @JsonProperty("ivtr_shnu_tr_pbmn") String ivtrShnuTrPbmn, // 투자신탁 매수2 거래 대금
            @JsonProperty("ivtr_ntby_tr_pbmn") String ivtrNtbyTrPbmn, // 투자신탁 순매수 거래 대금
            @JsonProperty("pe_fund_seln_tr_pbmn") String peFundSelnTrPbmn, // 사모 펀드 매도 거래 대금
            @JsonProperty("pe_fund_seln_vol") String peFundSelnVol, // 사모 펀드 매도 거래량
            @JsonProperty("pe_fund_ntby_vol") String peFundNtbyVol, // 사모 펀드 순매수 거래량
            @JsonProperty("pe_fund_shnu_tr_pbmn") String peFundShnuTrPbmn, // 사모 펀드 매수2 거래 대금
            @JsonProperty("pe_fund_shnu_vol") String peFundShnuVol, // 사모 펀드 매수2 거래량
            @JsonProperty("pe_fund_ntby_tr_pbmn") String peFundNtbyTrPbmn, // 사모 펀드 순매수 거래 대금
            @JsonProperty("bank_seln_vol") String bankSelnVol, // 은행 매도 거래량
            @JsonProperty("bank_shnu_vol") String bankShnuVol, // 은행 매수2 거래량
            @JsonProperty("bank_ntby_qty") String bankNtbyQty, // 은행 순매수 수량
            @JsonProperty("bank_seln_tr_pbmn") String bankSelnTrPbmn, // 은행 매도 거래 대금
            @JsonProperty("bank_shnu_tr_pbmn") String bankShnuTrPbmn, // 은행 매수2 거래 대금
            @JsonProperty("bank_ntby_tr_pbmn") String bankNtbyTrPbmn, // 은행 순매수 거래 대금
            @JsonProperty("insu_seln_vol") String insuSelnVol, // 보험 매도 거래량
            @JsonProperty("insu_shnu_vol") String insuShnuVol, // 보험 매수2 거래량
            @JsonProperty("insu_ntby_qty") String insuNtbyQty, // 보험 순매수 수량
            @JsonProperty("insu_seln_tr_pbmn") String insuSelnTrPbmn, // 보험 매도 거래 대금
            @JsonProperty("insu_shnu_tr_pbmn") String insuShnuTrPbmn, // 보험 매수2 거래 대금
            @JsonProperty("insu_ntby_tr_pbmn") String insuNtbyTrPbmn, // 보험 순매수 거래 대금
            @JsonProperty("mrbn_seln_vol") String mrbnSelnVol, // 종금 매도 거래량
            @JsonProperty("mrbn_shnu_vol") String mrbnShnuVol, // 종금 매수2 거래량
            @JsonProperty("mrbn_ntby_qty") String mrbnNtbyQty, // 종금 순매수 수량
            @JsonProperty("mrbn_seln_tr_pbmn") String mrbnSelnTrPbmn, // 종금 매도 거래 대금
            @JsonProperty("mrbn_shnu_tr_pbmn") String mrbnShnuTrPbmn, // 종금 매수2 거래 대금
            @JsonProperty("mrbn_ntby_tr_pbmn") String mrbnNtbyTrPbmn, // 종금 순매수 거래 대금
            @JsonProperty("fund_seln_vol") String fundSelnVol, // 기금 매도 거래량
            @JsonProperty("fund_shnu_vol") String fundShnuVol, // 기금 매수2 거래량
            @JsonProperty("fund_ntby_qty") String fundNtbyQty, // 기금 순매수 수량
            @JsonProperty("fund_seln_tr_pbmn") String fundSelnTrPbmn, // 기금 매도 거래 대금
            @JsonProperty("fund_shnu_tr_pbmn") String fundShnuTrPbmn, // 기금 매수2 거래 대금
            @JsonProperty("fund_ntby_tr_pbmn") String fundNtbyTrPbmn, // 기금 순매수 거래 대금
            @JsonProperty("etc_orgt_seln_vol") String etcOrgtSelnVol, // 기타 단체 매도 거래량
            @JsonProperty("etc_orgt_shnu_vol") String etcOrgtShnuVol, // 기타 단체 매수2 거래량
            @JsonProperty("etc_orgt_ntby_vol") String etcOrgtNtbyVol, // 기타 단체 순매수 거래량
            @JsonProperty("etc_orgt_seln_tr_pbmn") String etcOrgtSelnTrPbmn, // 기타 단체 매도 거래 대금
            @JsonProperty("etc_orgt_shnu_tr_pbmn") String etcOrgtShnuTrPbmn, // 기타 단체 매수2 거래 대금
            @JsonProperty("etc_orgt_ntby_tr_pbmn") String etcOrgtNtbyTrPbmn, // 기타 단체 순매수 거래 대금
            @JsonProperty("etc_corp_seln_vol") String etcCorpSelnVol, // 기타 법인 매도 거래량
            @JsonProperty("etc_corp_shnu_vol") String etcCorpShnuVol, // 기타 법인 매수2 거래량
            @JsonProperty("etc_corp_ntby_vol") String etcCorpNtbyVol, // 기타 법인 순매수 거래량
            @JsonProperty("etc_corp_seln_tr_pbmn") String etcCorpSelnTrPbmn, // 기타 법인 매도 거래 대금
            @JsonProperty("etc_corp_shnu_tr_pbmn") String etcCorpShnuTrPbmn, // 기타 법인 매수2 거래 대금
            @JsonProperty("etc_corp_ntby_tr_pbmn") String etcCorpNtbyTrPbmn // 기타 법인 순매수 거래 대금
    ) {
    }
}
