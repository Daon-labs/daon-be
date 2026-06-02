package daon.be.agent.data.source.kis.ranking.tradepower;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 체결강도 상위
 * TR ID: FHPST01680000
 * URL: /uapi/domestic-stock/v1/ranking/volume-power
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01680000VolumePowerResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stck_shrn_iscd") String stckShrnIscd, // 주식 단축 종목코드
            @JsonProperty("data_rank") String dataRank, // 데이터 순위
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("tday_rltv") String tdayRltv, // 당일 체결강도
            @JsonProperty("seln_cnqn_smtn") String selnCnqnSmtn, // 매도 체결량 합계
            @JsonProperty("shnu_cnqn_smtn") String shnuCnqnSmtn // 매수2 체결량 합계
    ) {
    }
}
