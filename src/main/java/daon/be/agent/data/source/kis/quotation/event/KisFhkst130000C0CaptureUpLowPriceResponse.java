package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 상하한가 포착
 * TR ID: FHKST130000C0
 * URL: /uapi/domestic-stock/v1/quotations/capture-uplowprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst130000C0CaptureUpLowPriceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권단축종목코드
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS한글종목명
            @JsonProperty("stck_prpr") String stckPrpr, // 주식현재가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일대비부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일대비
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적거래량
            @JsonProperty("total_askp_rsqn") String totalAskpRsqn, // 총매도호가잔량
            @JsonProperty("total_bidp_rsqn") String totalBidpRsqn, // 총매수호가잔량
            @JsonProperty("askp_rsqn1") String askpRsqn1, // 매도호가잔량1
            @JsonProperty("bidp_rsqn1") String bidpRsqn1, // 매수호가잔량1
            @JsonProperty("prdy_vol") String prdyVol, // 전일거래량
            @JsonProperty("seln_cnqn") String selnCnqn, // 매도체결량
            @JsonProperty("shnu_cnqn") String shnuCnqn, // 매수2체결량
            @JsonProperty("stck_llam") String stckLlam, // 주식하한가
            @JsonProperty("stck_mxpr") String stckMxpr, // 주식상한가
            @JsonProperty("prdy_vrss_vol_rate") String prdyVrssVolRate // 전일대비거래량비율
    ) {
    }
}
