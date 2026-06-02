package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 프로그램매매 종합현황(일별)
 * TR ID: FHPPG04600001
 * URL: /uapi/domestic-stock/v1/quotations/comp-program-trade-daily
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhppg04600001CompProgramTradeDailyResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("nabt_entm_seln_tr_pbmn") String nabtEntmSelnTrPbmn, // 비차익 위탁 매도 거래 대금
            @JsonProperty("nabt_onsl_seln_vol") String nabtOnslSelnVol, // 비차익 자기 매도 거래량
            @JsonProperty("whol_onsl_seln_tr_pbmn") String wholOnslSelnTrPbmn, // 전체 자기 매도 거래 대금
            @JsonProperty("arbt_smtn_shnu_vol") String arbtSmtnShnuVol, // 차익 합계 매수2 거래량
            @JsonProperty("nabt_smtn_shnu_tr_pbmn") String nabtSmtnShnuTrPbmn, // 비차익 합계 매수2 거래 대금
            @JsonProperty("arbt_entm_ntby_qty") String arbtEntmNtbyQty, // 차익 위탁 순매수 수량
            @JsonProperty("nabt_entm_ntby_tr_pbmn") String nabtEntmNtbyTrPbmn, // 비차익 위탁 순매수 거래 대금
            @JsonProperty("arbt_entm_seln_vol") String arbtEntmSelnVol, // 차익 위탁 매도 거래량
            @JsonProperty("nabt_entm_seln_vol_rate") String nabtEntmSelnVolRate, // 비차익 위탁 매도 거래량 비율
            @JsonProperty("nabt_onsl_seln_vol_rate") String nabtOnslSelnVolRate, // 비차익 자기 매도 거래량 비율
            @JsonProperty("whol_onsl_seln_tr_pbmn_rate") String wholOnslSelnTrPbmnRate, // 전체 자기 매도 거래 대금 비율
            @JsonProperty("arbt_smtm_shun_vol_rate") String arbtSmtmShunVolRate, // 차익 합계 매수 거래량 비율
            @JsonProperty("nabt_smtm_shun_tr_pbmn_rate") String nabtSmtmShunTrPbmnRate, // 비차익 합계 매수 거래대금 비율
            @JsonProperty("arbt_entm_ntby_qty_rate") String arbtEntmNtbyQtyRate, // 차익 위탁 순매수 수량 비율
            @JsonProperty("nabt_entm_ntby_tr_pbmn_rate") String nabtEntmNtbyTrPbmnRate, // 비차익 위탁 순매수 거래 대금
            @JsonProperty("arbt_entm_seln_vol_rate") String arbtEntmSelnVolRate, // 차익 위탁 매도 거래량 비율
            @JsonProperty("nabt_entm_seln_tr_pbmn_rate") String nabtEntmSelnTrPbmnRate, // 비차익 위탁 매도 거래 대금 비
            @JsonProperty("nabt_onsl_seln_tr_pbmn") String nabtOnslSelnTrPbmn, // 비차익 자기 매도 거래 대금
            @JsonProperty("whol_smtn_seln_vol") String wholSmtnSelnVol, // 전체 합계 매도 거래량
            @JsonProperty("arbt_smtn_shnu_tr_pbmn") String arbtSmtnShnuTrPbmn, // 차익 합계 매수2 거래 대금
            @JsonProperty("whol_entm_shnu_vol") String wholEntmShnuVol, // 전체 위탁 매수2 거래량
            @JsonProperty("arbt_entm_ntby_tr_pbmn") String arbtEntmNtbyTrPbmn, // 차익 위탁 순매수 거래 대금
            @JsonProperty("nabt_onsl_ntby_qty") String nabtOnslNtbyQty, // 비차익 자기 순매수 수량
            @JsonProperty("arbt_entm_seln_tr_pbmn") String arbtEntmSelnTrPbmn, // 차익 위탁 매도 거래 대금
            @JsonProperty("nabt_onsl_seln_tr_pbmn_rate") String nabtOnslSelnTrPbmnRate, // 비차익 자기 매도 거래 대금 비
            @JsonProperty("whol_seln_vol_rate") String wholSelnVolRate, // 전체 매도 거래량 비율
            @JsonProperty("arbt_smtm_shun_tr_pbmn_rate") String arbtSmtmShunTrPbmnRate, // 차익 합계 매수 거래대금 비율
            @JsonProperty("whol_entm_shnu_vol_rate") String wholEntmShnuVolRate, // 전체 위탁 매수 거래량 비율
            @JsonProperty("arbt_entm_ntby_tr_pbmn_rate") String arbtEntmNtbyTrPbmnRate, // 차익 위탁 순매수 거래 대금 비
            @JsonProperty("nabt_onsl_ntby_qty_rate") String nabtOnslNtbyQtyRate, // 비차익 자기 순매수 수량 비율
            @JsonProperty("arbt_entm_seln_tr_pbmn_rate") String arbtEntmSelnTrPbmnRate, // 차익 위탁 매도 거래 대금 비율
            @JsonProperty("nabt_smtn_seln_vol") String nabtSmtnSelnVol, // 비차익 합계 매도 거래량
            @JsonProperty("whol_smtn_seln_tr_pbmn") String wholSmtnSelnTrPbmn, // 전체 합계 매도 거래 대금
            @JsonProperty("nabt_entm_shnu_vol") String nabtEntmShnuVol, // 비차익 위탁 매수2 거래량
            @JsonProperty("whol_entm_shnu_tr_pbmn") String wholEntmShnuTrPbmn, // 전체 위탁 매수2 거래 대금
            @JsonProperty("arbt_onsl_ntby_qty") String arbtOnslNtbyQty, // 차익 자기 순매수 수량
            @JsonProperty("nabt_onsl_ntby_tr_pbmn") String nabtOnslNtbyTrPbmn, // 비차익 자기 순매수 거래 대금
            @JsonProperty("arbt_onsl_seln_tr_pbmn") String arbtOnslSelnTrPbmn, // 차익 자기 매도 거래 대금
            @JsonProperty("nabt_smtm_seln_vol_rate") String nabtSmtmSelnVolRate, // 비차익 합계 매도 거래량 비율
            @JsonProperty("whol_seln_tr_pbmn_rate") String wholSelnTrPbmnRate, // 전체 매도 거래대금 비율
            @JsonProperty("nabt_entm_shnu_vol_rate") String nabtEntmShnuVolRate, // 비차익 위탁 매수 거래량 비율
            @JsonProperty("whol_entm_shnu_tr_pbmn_rate") String wholEntmShnuTrPbmnRate, // 전체 위탁 매수 거래 대금 비율
            @JsonProperty("arbt_onsl_ntby_qty_rate") String arbtOnslNtbyQtyRate, // 차익 자기 순매수 수량 비율
            @JsonProperty("nabt_onsl_ntby_tr_pbmn_rate") String nabtOnslNtbyTrPbmnRate, // 비차익 자기 순매수 거래 대금
            @JsonProperty("arbt_onsl_seln_tr_pbmn_rate") String arbtOnslSelnTrPbmnRate, // 차익 자기 매도 거래 대금 비율
            @JsonProperty("nabt_smtn_seln_tr_pbmn") String nabtSmtnSelnTrPbmn, // 비차익 합계 매도 거래 대금
            @JsonProperty("arbt_entm_shnu_vol") String arbtEntmShnuVol, // 차익 위탁 매수2 거래량
            @JsonProperty("nabt_entm_shnu_tr_pbmn") String nabtEntmShnuTrPbmn, // 비차익 위탁 매수2 거래 대금
            @JsonProperty("whol_onsl_shnu_vol") String wholOnslShnuVol, // 전체 자기 매수2 거래량
            @JsonProperty("arbt_onsl_ntby_tr_pbmn") String arbtOnslNtbyTrPbmn, // 차익 자기 순매수 거래 대금
            @JsonProperty("nabt_smtn_ntby_qty") String nabtSmtnNtbyQty, // 비차익 합계 순매수 수량
            @JsonProperty("arbt_onsl_seln_vol") String arbtOnslSelnVol, // 차익 자기 매도 거래량
            @JsonProperty("nabt_smtm_seln_tr_pbmn_rate") String nabtSmtmSelnTrPbmnRate, // 비차익 합계 매도 거래대금 비율
            @JsonProperty("arbt_entm_shnu_vol_rate") String arbtEntmShnuVolRate, // 차익 위탁 매수 거래량 비율
            @JsonProperty("nabt_entm_shnu_tr_pbmn_rate") String nabtEntmShnuTrPbmnRate, // 비차익 위탁 매수 거래 대금 비
            @JsonProperty("whol_onsl_shnu_tr_pbmn") String wholOnslShnuTrPbmn, // 전체 자기 매수2 거래 대금
            @JsonProperty("arbt_onsl_ntby_tr_pbmn_rate") String arbtOnslNtbyTrPbmnRate, // 차익 자기 순매수 거래 대금 비
            @JsonProperty("nabt_smtm_ntby_qty_rate") String nabtSmtmNtbyQtyRate, // 비차익 합계 순매수 수량 비율
            @JsonProperty("arbt_onsl_seln_vol_rate") String arbtOnslSelnVolRate, // 차익 자기 매도 거래량 비율
            @JsonProperty("whol_entm_seln_vol") String wholEntmSelnVol, // 전체 위탁 매도 거래량
            @JsonProperty("arbt_entm_shnu_tr_pbmn") String arbtEntmShnuTrPbmn, // 차익 위탁 매수2 거래 대금
            @JsonProperty("nabt_onsl_shnu_vol") String nabtOnslShnuVol, // 비차익 자기 매수2 거래량
            @JsonProperty("whol_onsl_shnu_tr_pbmn_rate") String wholOnslShnuTrPbmnRate, // 전체 자기 매수 거래 대금 비율
            @JsonProperty("arbt_smtn_ntby_qty") String arbtSmtnNtbyQty, // 차익 합계 순매수 수량
            @JsonProperty("nabt_smtn_ntby_tr_pbmn") String nabtSmtnNtbyTrPbmn, // 비차익 합계 순매수 거래 대금
            @JsonProperty("arbt_smtn_seln_vol") String arbtSmtnSelnVol, // 차익 합계 매도 거래량
            @JsonProperty("whol_entm_seln_tr_pbmn") String wholEntmSelnTrPbmn, // 전체 위탁 매도 거래 대금
            @JsonProperty("arbt_entm_shnu_tr_pbmn_rate") String arbtEntmShnuTrPbmnRate, // 차익 위탁 매수 거래 대금 비율
            @JsonProperty("nabt_onsl_shnu_vol_rate") String nabtOnslShnuVolRate, // 비차익 자기 매수 거래량 비율
            @JsonProperty("whol_onsl_shnu_vol_rate") String wholOnslShnuVolRate, // 전체 자기 매수 거래량 비율
            @JsonProperty("arbt_smtm_ntby_qty_rate") String arbtSmtmNtbyQtyRate, // 차익 합계 순매수 수량 비율
            @JsonProperty("nabt_smtm_ntby_tr_pbmn_rate") String nabtSmtmNtbyTrPbmnRate, // 비차익 합계 순매수 거래대금 비
            @JsonProperty("arbt_smtm_seln_vol_rate") String arbtSmtmSelnVolRate, // 차익 합계 매도 거래량 비율
            @JsonProperty("whol_entm_seln_vol_rate") String wholEntmSelnVolRate, // 전체 위탁 매도 거래량 비율
            @JsonProperty("arbt_onsl_shnu_vol") String arbtOnslShnuVol, // 차익 자기 매수2 거래량
            @JsonProperty("nabt_onsl_shnu_tr_pbmn") String nabtOnslShnuTrPbmn, // 비차익 자기 매수2 거래 대금
            @JsonProperty("whol_smtn_shnu_vol") String wholSmtnShnuVol, // 전체 합계 매수2 거래량
            @JsonProperty("arbt_smtn_ntby_tr_pbmn") String arbtSmtnNtbyTrPbmn, // 차익 합계 순매수 거래 대금
            @JsonProperty("whol_entm_ntby_qty") String wholEntmNtbyQty, // 전체 위탁 순매수 수량
            @JsonProperty("arbt_smtn_seln_tr_pbmn") String arbtSmtnSelnTrPbmn, // 차익 합계 매도 거래 대금
            @JsonProperty("whol_entm_seln_tr_pbmn_rate") String wholEntmSelnTrPbmnRate, // 전체 위탁 매도 거래 대금 비율
            @JsonProperty("arbt_onsl_shnu_vol_rate") String arbtOnslShnuVolRate, // 차익 자기 매수 거래량 비율
            @JsonProperty("nabt_onsl_shnu_tr_pbmn_rate") String nabtOnslShnuTrPbmnRate, // 비차익 자기 매수 거래 대금 비
            @JsonProperty("whol_shun_vol_rate") String wholShunVolRate, // 전체 매수 거래량 비율
            @JsonProperty("arbt_smtm_ntby_tr_pbmn_rate") String arbtSmtmNtbyTrPbmnRate, // 차익 합계 순매수 거래대금 비율
            @JsonProperty("whol_entm_ntby_qty_rate") String wholEntmNtbyQtyRate, // 전체 위탁 순매수 수량 비율
            @JsonProperty("arbt_smtm_seln_tr_pbmn_rate") String arbtSmtmSelnTrPbmnRate, // 차익 합계 매도 거래대금 비율
            @JsonProperty("whol_onsl_seln_vol") String wholOnslSelnVol, // 전체 자기 매도 거래량
            @JsonProperty("arbt_onsl_shnu_tr_pbmn") String arbtOnslShnuTrPbmn, // 차익 자기 매수2 거래 대금
            @JsonProperty("nabt_smtn_shnu_vol") String nabtSmtnShnuVol, // 비차익 합계 매수2 거래량
            @JsonProperty("whol_smtn_shnu_tr_pbmn") String wholSmtnShnuTrPbmn, // 전체 합계 매수2 거래 대금
            @JsonProperty("nabt_entm_ntby_qty") String nabtEntmNtbyQty, // 비차익 위탁 순매수 수량
            @JsonProperty("whol_entm_ntby_tr_pbmn") String wholEntmNtbyTrPbmn, // 전체 위탁 순매수 거래 대금
            @JsonProperty("nabt_entm_seln_vol") String nabtEntmSelnVol, // 비차익 위탁 매도 거래량
            @JsonProperty("whol_onsl_seln_vol_rate") String wholOnslSelnVolRate, // 전체 자기 매도 거래량 비율
            @JsonProperty("arbt_onsl_shnu_tr_pbmn_rate") String arbtOnslShnuTrPbmnRate, // 차익 자기 매수 거래 대금 비율
            @JsonProperty("nabt_smtm_shun_vol_rate") String nabtSmtmShunVolRate, // 비차익 합계 매수 거래량 비율
            @JsonProperty("whol_shun_tr_pbmn_rate") String wholShunTrPbmnRate, // 전체 매수 거래대금 비율
            @JsonProperty("nabt_entm_ntby_qty_rate") String nabtEntmNtbyQtyRate // 비차익 위탁 순매수 수량 비율
    ) {
    }
}
