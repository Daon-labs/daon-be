package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 신용잔고 일별추이
 * TR ID: FHPST04760000
 * URL: /uapi/domestic-stock/v1/quotations/daily-credit-balance
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04760000DailyCreditBalanceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("deal_date") String dealDate, // 매매 일자
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("stlm_date") String stlmDate, // 결제 일자
            @JsonProperty("whol_loan_new_stcn") String wholLoanNewStcn, // 전체 융자 신규 주수
            @JsonProperty("whol_loan_rdmp_stcn") String wholLoanRdmpStcn, // 전체 융자 상환 주수
            @JsonProperty("whol_loan_rmnd_stcn") String wholLoanRmndStcn, // 전체 융자 잔고 주수
            @JsonProperty("whol_loan_new_amt") String wholLoanNewAmt, // 전체 융자 신규 금액
            @JsonProperty("whol_loan_rdmp_amt") String wholLoanRdmpAmt, // 전체 융자 상환 금액
            @JsonProperty("whol_loan_rmnd_amt") String wholLoanRmndAmt, // 전체 융자 잔고 금액
            @JsonProperty("whol_loan_rmnd_rate") String wholLoanRmndRate, // 전체 융자 잔고 비율
            @JsonProperty("whol_loan_gvrt") String wholLoanGvrt, // 전체 융자 공여율
            @JsonProperty("whol_stln_new_stcn") String wholStlnNewStcn, // 전체 대주 신규 주수
            @JsonProperty("whol_stln_rdmp_stcn") String wholStlnRdmpStcn, // 전체 대주 상환 주수
            @JsonProperty("whol_stln_rmnd_stcn") String wholStlnRmndStcn, // 전체 대주 잔고 주수
            @JsonProperty("whol_stln_new_amt") String wholStlnNewAmt, // 전체 대주 신규 금액
            @JsonProperty("whol_stln_rdmp_amt") String wholStlnRdmpAmt, // 전체 대주 상환 금액
            @JsonProperty("whol_stln_rmnd_amt") String wholStlnRmndAmt, // 전체 대주 잔고 금액
            @JsonProperty("whol_stln_rmnd_rate") String wholStlnRmndRate, // 전체 대주 잔고 비율
            @JsonProperty("whol_stln_gvrt") String wholStlnGvrt, // 전체 대주 공여율
            @JsonProperty("stck_oprc") String stckOprc, // 주식 시가2
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("stck_lwpr") String stckLwpr // 주식 최저가
    ) {
    }
}
