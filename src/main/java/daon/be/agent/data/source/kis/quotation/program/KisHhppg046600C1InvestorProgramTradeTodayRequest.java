package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 프로그램매매 투자자매매동향(당일)
 * TR ID: HHPPG046600C1
 * URL: /uapi/domestic-stock/v1/quotations/investor-program-trade-today
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhppg046600C1InvestorProgramTradeTodayRequest(
        @JsonProperty("EXCH_DIV_CLS_CODE") String exchDivClsCode, // 거래소 구분 코드
        @JsonProperty("MRKT_DIV_CLS_CODE") String mrktDivClsCode // 시장 구분 코드
) {
}
