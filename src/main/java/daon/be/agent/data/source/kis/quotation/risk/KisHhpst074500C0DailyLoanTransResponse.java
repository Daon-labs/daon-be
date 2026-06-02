package daon.be.agent.data.source.kis.quotation.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 종목별 일별 대차거래추이
 * TR ID: HHPST074500C0
 * URL: /uapi/domestic-stock/v1/quotations/daily-loan-trans
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhpst074500C0DailyLoanTransResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") List<Output1> output1
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("bsop_date") String bsopDate, // 일자
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 종가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("new_stcn") String newStcn, // 당일 증가 주수 (체결)
            @JsonProperty("rdmp_stcn") String rdmpStcn, // 당일 감소 주수 (상환)
            @JsonProperty("prdy_rmnd_vrss") String prdyRmndVrss, // 대차거래 증감
            @JsonProperty("rmnd_stcn") String rmndStcn, // 당일 잔고 주수
            @JsonProperty("rmnd_amt") String rmndAmt // 당일 잔고 금액
    ) {
    }
}
