package daon.be.agent.data.source.kis.finance.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 손익계산서
 * TR ID: FHKST66430200
 * URL: /uapi/domestic-stock/v1/finance/income-statement
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430200IncomeStatementResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stac_yymm") String stacYymm, // 결산 년월
            @JsonProperty("sale_account") String saleAccount, // 매출액
            @JsonProperty("sale_cost") String saleCost, // 매출 원가
            @JsonProperty("sale_totl_prfi") String saleTotlPrfi, // 매출 총 이익
            @JsonProperty("depr_cost") String deprCost, // 감가상각비
            @JsonProperty("sell_mang") String sellMang, // 판매 및 관리비
            @JsonProperty("bsop_prti") String bsopPrti, // 영업 이익
            @JsonProperty("bsop_non_ernn") String bsopNonErnn, // 영업 외 수익
            @JsonProperty("bsop_non_expn") String bsopNonExpn, // 영업 외 비용
            @JsonProperty("op_prfi") String opPrfi, // 경상 이익
            @JsonProperty("spec_prfi") String specPrfi, // 특별 이익
            @JsonProperty("spec_loss") String specLoss, // 특별 손실
            @JsonProperty("thtr_ntin") String thtrNtin // 당기순이익
    ) {
    }
}
