package daon.be.agent.data.source.kis.ranking.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 공매도 상위종목
 * TR ID: FHPST04820000
 * URL: /uapi/domestic-stock/v1/ranking/short-sale
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04820000ShortSaleRankRequest(
        @JsonProperty("FID_APLY_RANG_VOL") String fidAplyRangVol, // FID 적용 범위 거래량
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_PERIOD_DIV_CODE") String fidPeriodDivCode, // 조회구분 (일/월)
        @JsonProperty("FID_INPUT_CNT_1") String fidInputCnt1, // 조회가간(일수
        @JsonProperty("FID_TRGT_EXLS_CLS_CODE") String fidTrgtExlsClsCode, // 대상 제외 구분 코드
        @JsonProperty("FID_TRGT_CLS_CODE") String fidTrgtClsCode, // FID 대상 구분 코드
        @JsonProperty("FID_APLY_RANG_PRC_1") String fidAplyRangPrc1, // FID 적용 범위 가격1
        @JsonProperty("FID_APLY_RANG_PRC_2") String fidAplyRangPrc2 // FID 적용 범위 가격2
) {
}
