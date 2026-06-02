package daon.be.agent.data.source.kis.ranking.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 신용잔고 상위
 * TR ID: FHKST17010000
 * URL: /uapi/domestic-stock/v1/ranking/credit-balance
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst17010000CreditBalanceRankRequest(
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_OPTION") String fidOption, // 증가율기간
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_RANK_SORT_CLS_CODE") String fidRankSortClsCode // 순위 정렬 구분 코드
) {
}
