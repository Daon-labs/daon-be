package daon.be.agent.data.source.kis.quotation.investor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 종목별 외인기관 추정가집계
 * TR ID: HHPTJ04160200
 * URL: /uapi/domestic-stock/v1/quotations/investor-trend-estimate
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhptj04160200InvestorTrendEstimateResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("bsop_hour_gb") String bsopHourGb, // 입력구분
            @JsonProperty("frgn_fake_ntby_qty") String frgnFakeNtbyQty, // 외국인수량(가집계)
            @JsonProperty("orgn_fake_ntby_qty") String orgnFakeNtbyQty, // 기관수량(가집계)
            @JsonProperty("sum_fake_ntby_qty") String sumFakeNtbyQty // 합산수량(가집계)
    ) {
    }
}
