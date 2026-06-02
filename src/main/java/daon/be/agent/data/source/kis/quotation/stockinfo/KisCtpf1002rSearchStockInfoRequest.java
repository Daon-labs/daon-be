package daon.be.agent.data.source.kis.quotation.stockinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식기본조회
 * TR ID: CTPF1002R
 * URL: /uapi/domestic-stock/v1/quotations/search-stock-info
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisCtpf1002rSearchStockInfoRequest(
        @JsonProperty("PRDT_TYPE_CD") String prdtTypeCd, // 상품유형코드
        @JsonProperty("PDNO") String pdno // 상품번호
) {
}
