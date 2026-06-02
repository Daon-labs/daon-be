package daon.be.agent.data.source.kis.ranking.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 공매도 상위종목
 * TR ID: FHPST04820000
 * URL: /uapi/domestic-stock/v1/ranking/short-sale
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst04820000ShortSaleRankResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn, // 누적 거래 대금
            @JsonProperty("ssts_cntg_qty") String sstsCntgQty, // 공매도 체결 수량
            @JsonProperty("ssts_vol_rlim") String sstsVolRlim, // 공매도 거래량 비중
            @JsonProperty("ssts_tr_pbmn") String sstsTrPbmn, // 공매도 거래 대금
            @JsonProperty("ssts_tr_pbmn_rlim") String sstsTrPbmnRlim, // 공매도 거래대금 비중
            @JsonProperty("stnd_date1") String stndDate1, // 기준 일자1
            @JsonProperty("stnd_date2") String stndDate2, // 기준 일자2
            @JsonProperty("avrg_prc") String avrgPrc // 평균가격
    ) {
    }
}
