package daon.be.agent.data.source.kis.quotation.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 종목별 프로그램매매추이(체결)
 * TR ID: FHPPG04650101
 * URL: /uapi/domestic-stock/v1/quotations/program-trade-by-stock
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhppg04650101ProgramTradeByStockResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("bsop_hour") String bsopHour, // 영업 시간
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("whol_smtn_seln_vol") String wholSmtnSelnVol, // 전체 합계 매도 거래량
            @JsonProperty("whol_smtn_shnu_vol") String wholSmtnShnuVol, // 전체 합계 매수2 거래량
            @JsonProperty("whol_smtn_ntby_qty") String wholSmtnNtbyQty, // 전체 합계 순매수 수량
            @JsonProperty("whol_smtn_seln_tr_pbmn") String wholSmtnSelnTrPbmn, // 전체 합계 매도 거래 대금
            @JsonProperty("whol_smtn_shnu_tr_pbmn") String wholSmtnShnuTrPbmn, // 전체 합계 매수2 거래 대금
            @JsonProperty("whol_smtn_ntby_tr_pbmn") String wholSmtnNtbyTrPbmn, // 전체 합계 순매수 거래 대금
            @JsonProperty("whol_ntby_vol_icdc") String wholNtbyVolIcdc, // 전체 순매수 거래량 증감
            @JsonProperty("whol_ntby_tr_pbmn_icdc") String wholNtbyTrPbmnIcdc // 전체 순매수 거래 대금 증감
    ) {
    }
}
