package daon.be.agent.data.source.kis.quotation.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 관심종목(멀티종목) 시세조회
 * TR ID: FHKST11300006
 * URL: /uapi/domestic-stock/v1/quotations/intstock-multprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst11300006IntstockMultpriceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("kospi_kosdaq_cls_name") String kospiKosdaqClsName, // 코스피 코스닥 구분 명
            @JsonProperty("mrkt_trtm_cls_name") String mrktTrtmClsName, // 시장 조치 구분 명
            @JsonProperty("hour_cls_code") String hourClsCode, // 시간 구분 코드
            @JsonProperty("inter_shrn_iscd") String interShrnIscd, // 관심 단축 종목코드
            @JsonProperty("inter_kor_isnm") String interKorIsnm, // 관심 한글 종목명
            @JsonProperty("inter2_prpr") String inter2Prpr, // 관심2 현재가
            @JsonProperty("inter2_prdy_vrss") String inter2PrdyVrss, // 관심2 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("inter2_oprc") String inter2Oprc, // 관심2 시가
            @JsonProperty("inter2_hgpr") String inter2Hgpr, // 관심2 고가
            @JsonProperty("inter2_lwpr") String inter2Lwpr, // 관심2 저가
            @JsonProperty("inter2_llam") String inter2Llam, // 관심2 하한가
            @JsonProperty("inter2_mxpr") String inter2Mxpr, // 관심2 상한가
            @JsonProperty("inter2_askp") String inter2Askp, // 관심2 매도호가
            @JsonProperty("inter2_bidp") String inter2Bidp, // 관심2 매수호가
            @JsonProperty("seln_rsqn") String selnRsqn, // 매도 잔량
            @JsonProperty("shnu_rsqn") String shnuRsqn, // 매수2 잔량
            @JsonProperty("total_askp_rsqn") String totalAskpRsqn, // 총 매도호가 잔량
            @JsonProperty("total_bidp_rsqn") String totalBidpRsqn, // 총 매수호가 잔량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("inter2_prdy_clpr") String inter2PrdyClpr, // 관심2 전일 종가
            @JsonProperty("oprc_vrss_hgpr_rate") String oprcVrssHgprRate, // 시가 대비 최고가 비율
            @JsonProperty("intr_antc_cntg_vrss") String intrAntcCntgVrss, // 관심 예상 체결 대비
            @JsonProperty("intr_antc_cntg_vrss_sign") String intrAntcCntgVrssSign, // 관심 예상 체결 대비 부호
            @JsonProperty("intr_antc_cntg_prdy_ctrt") String intrAntcCntgPrdyCtrt, // 관심 예상 체결 전일 대비율
            @JsonProperty("intr_antc_vol") String intrAntcVol, // 관심 예상 거래량
            @JsonProperty("inter2_sdpr") String inter2Sdpr // 관심2 기준가
    ) {
    }
}
