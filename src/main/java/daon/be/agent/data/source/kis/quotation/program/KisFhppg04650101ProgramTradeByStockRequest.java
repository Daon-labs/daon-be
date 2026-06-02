package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 종목별 프로그램매매추이(체결)
 * TR ID: FHPPG04650101
 * URL: /uapi/domestic-stock/v1/quotations/program-trade-by-stock
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhppg04650101ProgramTradeByStockRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("FID_INPUT_ISCD") String fidInputIscd // 입력 종목코드
) {
}
