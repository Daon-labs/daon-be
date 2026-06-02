package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 종목별 투자자매매동향(일별)
 * TR ID: FHPTJ04160001
 * URL: /uapi/domestic-stock/v1/quotations/investor-trade-by-stock-daily
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhptj04160001InvestorTradeByStockDailyRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd, // 입력 종목코드
        @JsonProperty("FID_INPUT_DATE_1") String fidInputDate1, // 입력 날짜1
        @JsonProperty("FID_ORG_ADJ_PRC") String fidOrgAdjPrc, // 수정주가 원주가 가격
        @JsonProperty("FID_ETC_CLS_CODE") String fidEtcClsCode // 기타 구분 코드
) {
}
