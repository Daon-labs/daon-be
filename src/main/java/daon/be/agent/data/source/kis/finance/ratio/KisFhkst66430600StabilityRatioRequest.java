package daon.be.agent.data.source.kis.finance.ratio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 안정성비율
 * TR ID: FHKST66430600
 * URL: /uapi/domestic-stock/v1/finance/stability-ratio
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst66430600StabilityRatioRequest(
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 입력 종목코드
        @JsonProperty("fid_div_cls_code") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode // 조건 시장 분류 코드
) {
}
