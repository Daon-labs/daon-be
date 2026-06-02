package daon.be.agent.data.source.kis.ranking.volume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 거래량순위
 * TR ID: FHPST01710000
 * URL: /uapi/domestic-stock/v1/quotations/volume-rank
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01710000VolumeRankResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("Output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("data_rank") String dataRank, // 데이터 순위
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("prdy_vol") String prdyVol, // 전일 거래량
            @JsonProperty("lstn_stcn") String lstnStcn, // 상장 주수
            @JsonProperty("avrg_vol") String avrgVol, // 평균 거래량
            @JsonProperty("n_befr_clpr_vrss_prpr_rate") String nBefrClprVrssPrprRate, // N일전종가대비현재가대비율
            @JsonProperty("vol_inrt") String volInrt, // 거래량증가율
            @JsonProperty("vol_tnrt") String volTnrt, // 거래량 회전율
            @JsonProperty("nday_vol_tnrt") String ndayVolTnrt, // N일 거래량 회전율
            @JsonProperty("avrg_tr_pbmn") String avrgTrPbmn, // 평균 거래 대금
            @JsonProperty("tr_pbmn_tnrt") String trPbmnTnrt, // 거래대금회전율
            @JsonProperty("nday_tr_pbmn_tnrt") String ndayTrPbmnTnrt, // N일 거래대금 회전율
            @JsonProperty("acml_tr_pbmn") String acmlTrPbmn // 누적 거래 대금
    ) {
    }
}
