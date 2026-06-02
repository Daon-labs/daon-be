package daon.be.agent.data.source.kis.quotation.intraday;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 주식현재가 당일시간대별체결
 * TR ID: FHPST01060000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01060000TimeItemConclusionResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("prdy_vol") String prdyVol, // 전일 거래량
            @JsonProperty("rprs_mrkt_kor_name") String rprsMrktKorName // 대표 시장 한글 명
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_cntg_hour") String stckCntgHour, // 주식 체결 시간
            @JsonProperty("stck_pbpr") String stckPbpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("askp") String askp, // 매도호가
            @JsonProperty("bidp") String bidp, // 매수호가
            @JsonProperty("tday_rltv") String tdayRltv, // 당일 체결강도
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("cnqn") String cnqn // 체결량
    ) {
    }
}
