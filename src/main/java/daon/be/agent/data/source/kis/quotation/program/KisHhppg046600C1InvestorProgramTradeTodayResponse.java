package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 프로그램매매 투자자매매동향(당일)
 * TR ID: HHPPG046600C1
 * URL: /uapi/domestic-stock/v1/quotations/investor-program-trade-today
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhppg046600C1InvestorProgramTradeTodayResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") List<Output1> output1
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("invr_cls_code") String invrClsCode, // 투자자코드
            @JsonProperty("all_seln_qty") String allSelnQty, // 전체매도수량
            @JsonProperty("all_seln_amt") String allSelnAmt, // 전체매도대금
            @JsonProperty("invr_cls_name") String invrClsName, // 투자자 구분 명
            @JsonProperty("all_shnu_qty") String allShnuQty, // 전체매수수량
            @JsonProperty("all_shnu_amt") String allShnuAmt, // 전체매수대금
            @JsonProperty("all_ntby_amt") String allNtbyAmt, // 전체순매수대금
            @JsonProperty("arbt_seln_qty") String arbtSelnQty, // 차익매도수량
            @JsonProperty("all_ntby_qty") String allNtbyQty, // 전체순매수수량
            @JsonProperty("arbt_shnu_qty") String arbtShnuQty, // 차익매수수량
            @JsonProperty("arbt_ntby_qty") String arbtNtbyQty, // 차익순매수수량
            @JsonProperty("arbt_seln_amt") String arbtSelnAmt, // 차익매도대금
            @JsonProperty("arbt_shnu_amt") String arbtShnuAmt, // 차익매수대금
            @JsonProperty("arbt_ntby_amt") String arbtNtbyAmt, // 차익순매수대금
            @JsonProperty("nabt_seln_qty") String nabtSelnQty, // 비차익매도수량
            @JsonProperty("nabt_shnu_qty") String nabtShnuQty, // 비차익매수수량
            @JsonProperty("nabt_ntby_qty") String nabtNtbyQty, // 비차익순매수수량
            @JsonProperty("nabt_seln_amt") String nabtSelnAmt, // 비차익매도대금
            @JsonProperty("nabt_shnu_amt") String nabtShnuAmt, // 비차익매수대금
            @JsonProperty("nabt_ntby_amt") String nabtNtbyAmt // 비차익순매수대금
    ) {
    }
}
