package daon.be.agent.data.source.kis.ranking.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 신용잔고 상위
 * TR ID: FHKST17010000
 * URL: /uapi/domestic-stock/v1/ranking/credit-balance
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst17010000CreditBalanceRankResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") List<Output1> output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("bstp_cls_code") String bstpClsCode, // 업종 구분 코드
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("stnd_date1") String stndDate1, // 기준 일자1
            @JsonProperty("stnd_date2") String stndDate2 // 기준 일자2
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("whol_loan_rmnd_stcn") String wholLoanRmndStcn, // 전체 융자 잔고 주수
            @JsonProperty("whol_loan_rmnd_amt") String wholLoanRmndAmt, // 전체 융자 잔고 금액
            @JsonProperty("whol_loan_rmnd_rate") String wholLoanRmndRate, // 전체 융자 잔고 비율
            @JsonProperty("whol_stln_rmnd_stcn") String wholStlnRmndStcn, // 전체 대주 잔고 주수
            @JsonProperty("whol_stln_rmnd_amt") String wholStlnRmndAmt, // 전체 대주 잔고 금액
            @JsonProperty("whol_stln_rmnd_rate") String wholStlnRmndRate, // 전체 대주 잔고 비율
            @JsonProperty("nday_vrss_loan_rmnd_inrt") String ndayVrssLoanRmndInrt, // N일 대비 융자 잔고 증가율
            @JsonProperty("nday_vrss_stln_rmnd_inrt") String ndayVrssStlnRmndInrt // N일 대비 대주 잔고 증가율
    ) {
    }
}
