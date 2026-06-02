package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 프로그램매매 종합현황(시간)
 * TR ID: FHPPG04600101
 * URL: /uapi/domestic-stock/v1/quotations/comp-program-trade-today
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhppg04600101CompProgramTradeTodayResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") List<Output1> output1
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("bsop_hour") String bsopHour, // 영업 시간
            @JsonProperty("arbt_smtn_seln_tr_pbmn") String arbtSmtnSelnTrPbmn, // 차익 합계 매도 거래 대금
            @JsonProperty("arbt_smtm_seln_tr_pbmn_rate") String arbtSmtmSelnTrPbmnRate, // 차익 합계 매도 거래대금 비율
            @JsonProperty("arbt_smtn_shnu_tr_pbmn") String arbtSmtnShnuTrPbmn, // 차익 합계 매수2 거래 대금
            @JsonProperty("arbt_smtm_shun_tr_pbmn_rate") String arbtSmtmShunTrPbmnRate, // 차익합계매수거래대금비율
            @JsonProperty("nabt_smtn_seln_tr_pbmn") String nabtSmtnSelnTrPbmn, // 비차익 합계 매도 거래 대금
            @JsonProperty("nabt_smtm_seln_tr_pbmn_rate") String nabtSmtmSelnTrPbmnRate, // 비차익 합계 매도 거래대금 비율
            @JsonProperty("nabt_smtn_shnu_tr_pbmn") String nabtSmtnShnuTrPbmn, // 비차익 합계 매수2 거래 대금
            @JsonProperty("nabt_smtm_shun_tr_pbmn_rate") String nabtSmtmShunTrPbmnRate, // 비차익합계매수거래대금비율
            @JsonProperty("arbt_smtn_ntby_tr_pbmn") String arbtSmtnNtbyTrPbmn, // 차익 합계 순매수 거래 대금
            @JsonProperty("arbt_smtm_ntby_tr_pbmn_rate") String arbtSmtmNtbyTrPbmnRate, // 차익 합계 순매수 거래대금 비율
            @JsonProperty("nabt_smtn_ntby_tr_pbmn") String nabtSmtnNtbyTrPbmn, // 비차익 합계 순매수 거래 대금
            @JsonProperty("nabt_smtm_ntby_tr_pbmn_rate") String nabtSmtmNtbyTrPbmnRate, // 비차익 합계 순매수 거래대금 비
            @JsonProperty("whol_smtn_ntby_tr_pbmn") String wholSmtnNtbyTrPbmn, // 전체 합계 순매수 거래 대금
            @JsonProperty("whol_ntby_tr_pbmn_rate") String wholNtbyTrPbmnRate, // 전체 순매수 거래대금 비율
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("bstp_nmix_prdy_vrss") String bstpNmixPrdyVrss, // 업종 지수 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign // 전일 대비 부호
    ) {
    }
}
