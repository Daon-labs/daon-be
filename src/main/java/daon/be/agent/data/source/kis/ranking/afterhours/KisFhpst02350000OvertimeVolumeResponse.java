package daon.be.agent.data.source.kis.ranking.afterhours;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 시간외거래량순위
 * TR ID: FHPST02350000
 * URL: /uapi/domestic-stock/v1/ranking/overtime-volume
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst02350000OvertimeVolumeResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("ovtm_untp_exch_vol") String ovtmUntpExchVol, // 시간외 단일가 거래소 거래량
            @JsonProperty("ovtm_untp_exch_tr_pbmn") String ovtmUntpExchTrPbmn, // 시간외 단일가 거래소 거래대금
            @JsonProperty("ovtm_untp_kosdaq_vol") String ovtmUntpKosdaqVol, // 시간외 단일가 KOSDAQ 거래량
            @JsonProperty("ovtm_untp_kosdaq_tr_pbmn") String ovtmUntpKosdaqTrPbmn // 시간외 단일가 KOSDAQ 거래대금
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_shrn_iscd") String stckShrnIscd, // 주식 단축 종목코드
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("ovtm_untp_prpr") String ovtmUntpPrpr, // 시간외 단일가 현재가
            @JsonProperty("ovtm_untp_prdy_vrss") String ovtmUntpPrdyVrss, // 시간외 단일가 전일 대비
            @JsonProperty("ovtm_untp_prdy_vrss_sign") String ovtmUntpPrdyVrssSign, // 시간외 단일가 전일 대비 부호
            @JsonProperty("ovtm_untp_prdy_ctrt") String ovtmUntpPrdyCtrt, // 시간외 단일가 전일 대비율
            @JsonProperty("ovtm_untp_seln_rsqn") String ovtmUntpSelnRsqn, // 시간외 단일가 매도 잔량
            @JsonProperty("ovtm_untp_shnu_rsqn") String ovtmUntpShnuRsqn, // 시간외 단일가 매수 잔량
            @JsonProperty("ovtm_untp_vol") String ovtmUntpVol, // 시간외 단일가 거래량
            @JsonProperty("ovtm_vrss_acml_vol_rlim") String ovtmVrssAcmlVolRlim, // 시간외 대비 누적 거래량 비중
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("bidp") String bidp, // 매수호가
            @JsonProperty("askp") String askp // 매도호가
    ) {
    }
}
