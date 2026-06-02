package daon.be.agent.data.source.kis.finance.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 대차대조표
 * TR ID: FHKST66430100
 * URL: /uapi/domestic-stock/v1/finance/balance-sheet
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430100BalanceSheetResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stac_yymm") String stacYymm, // 결산 년월
            @JsonProperty("cras") String cras, // 유동자산
            @JsonProperty("fxas") String fxas, // 고정자산
            @JsonProperty("total_aset") String totalAset, // 자산총계
            @JsonProperty("flow_lblt") String flowLblt, // 유동부채
            @JsonProperty("fix_lblt") String fixLblt, // 고정부채
            @JsonProperty("total_lblt") String totalLblt, // 부채총계
            @JsonProperty("cpfn") String cpfn, // 자본금
            @JsonProperty("cfp_surp") String cfpSurp, // 자본 잉여금
            @JsonProperty("prfi_surp") String prfiSurp, // 이익 잉여금
            @JsonProperty("total_cptl") String totalCptl // 자본총계
    ) {
    }
}
