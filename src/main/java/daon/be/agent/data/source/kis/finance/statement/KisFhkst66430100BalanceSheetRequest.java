package daon.be.agent.data.source.kis.finance.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 대차대조표
 * TR ID: FHKST66430100
 * URL: /uapi/domestic-stock/v1/finance/balance-sheet
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430100BalanceSheetRequest(
        @JsonProperty("FID_DIV_CLS_CODE") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("fid_input_iscd") String fidInputIscd // 입력 종목코드
) {
}
