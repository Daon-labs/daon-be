package daon.be.agent.data.source.kis.quotation.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 업종 분봉조회
 * TR ID: FHKUP03500200
 * URL: /uapi/domestic-stock/v1/quotations/inquire-time-indexchartprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkup03500200IndexMinuteChartResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("Output1") List<Output1> output1,
        @JsonProperty("Output2") List<Output2> output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("bstp_nmix_prdy_vrss") String bstpNmixPrdyVrss, // 업종 지수 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("bstp_nmix_prdy_ctrt") String bstpNmixPrdyCtrt, // 업종 지수 전일 대비율
            @JsonProperty("prdy_nmix") String prdyNmix, // 전일 지수
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("bstp_cls_code") String bstpClsCode, // 업종 구분 코드
            @JsonProperty("prdy_vol") String prdyVol, // 전일 거래량
            @JsonProperty("bstp_nmix_oprc") String bstpNmixOprc, // 업종 지수 시가2
            @JsonProperty("bstp_nmix_hgpr") String bstpNmixHgpr, // 업종 지수 최고가
            @JsonProperty("bstp_nmix_lwpr") String bstpNmixLwpr, // 업종 지수 최저가
            @JsonProperty("futs_prdy_oprc") String futsPrdyOprc, // 선물 전일 시가
            @JsonProperty("futs_prdy_hgpr") String futsPrdyHgpr, // 선물 전일 최고가
            @JsonProperty("futs_prdy_lwpr") String futsPrdyLwpr // 선물 전일 최저가
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("stck_bsop_date") String stckBsopDate, // 주식 영업 일자
            @JsonProperty("stck_cntg_hour") String stckCntgHour, // 주식 체결 시간
            @JsonProperty("bstp_nmix_prpr") String bstpNmixPrpr, // 업종 지수 현재가
            @JsonProperty("bstp_nmix_oprc") String bstpNmixOprc, // 업종 지수 시가2
            @JsonProperty("bstp_nmix_hgpr") String bstpNmixHgpr, // 업종 지수 최고가
            @JsonProperty("bstp_nmix_lwpr") String bstpNmixLwpr, // 업종 지수 최저가
            @JsonProperty("cntg_vol") String cntgVol, // 체결 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn // 누적 거래 대금
    ) {
    }
}
