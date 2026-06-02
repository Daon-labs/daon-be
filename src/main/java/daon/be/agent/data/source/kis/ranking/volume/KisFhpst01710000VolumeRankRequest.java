package daon.be.agent.data.source.kis.ranking.volume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 거래량순위
 * TR ID: FHPST01710000
 * URL: /uapi/domestic-stock/v1/quotations/volume-rank
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01710000VolumeRankRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_DIV_CLS_CODE") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("FID_BLNG_CLS_CODE") String fidBlngClsCode, // 소속 구분 코드
        @JsonProperty("FID_TRGT_CLS_CODE") String fidTrgtClsCode, // 대상 구분 코드
        @JsonProperty("FID_TRGT_EXLS_CLS_CODE") String fidTrgtExlsClsCode, // 대상 제외 구분 코드
        @JsonProperty("FID_INPUT_PRICE_1") String fidInputPrice1, // 입력 가격1
        @JsonProperty("FID_INPUT_PRICE_2") String fidInputPrice2, // 입력 가격2
        @JsonProperty("FID_VOL_CNT") String fidVolCnt // 거래량 수
) {
}
