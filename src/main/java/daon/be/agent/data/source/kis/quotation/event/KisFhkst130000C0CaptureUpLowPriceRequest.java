package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 상하한가 포착
 * TR ID: FHKST130000C0
 * URL: /uapi/domestic-stock/v1/quotations/capture-uplowprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst130000C0CaptureUpLowPriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건시장분류코드
        @JsonProperty("FID_COND_SCR_DIV_CODE") String fidCondScrDivCode, // 조건화면분류코드
        @JsonProperty("FID_PRC_CLS_CODE") String fidPrcClsCode, // 상하한가 구분코드
        @JsonProperty("FID_DIV_CLS_CODE") String fidDivClsCode, // 분류구분코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력종목코드
        @JsonProperty("FID_TRGT_CLS_CODE") String fidTrgtClsCode, // 대상구분코드
        @JsonProperty("FID_TRGT_EXLS_CLS_CODE") String fidTrgtExlsClsCode, // 대상제외구분코드
        @JsonProperty("FID_INPUT_PRICE_1") String fidInputPrice1, // 입력가격1
        @JsonProperty("FID_INPUT_PRICE_2") String fidInputPrice2, // 입력가격2
        @JsonProperty("FID_VOL_CNT") String fidVolCnt // 거래량수
) {
}
