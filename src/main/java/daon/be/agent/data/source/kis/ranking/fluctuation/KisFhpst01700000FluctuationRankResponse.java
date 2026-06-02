package daon.be.agent.data.source.kis.ranking.fluctuation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 등락률 순위
 * TR ID: FHPST01700000
 * URL: /uapi/domestic-stock/v1/ranking/fluctuation
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01700000FluctuationRankResponse(
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
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("hgpr_hour") String hgprHour, // 최고가 시간
            @JsonProperty("acml_hgpr_date") String acmlHgprDate, // 누적 최고가 일자
            @JsonProperty("stck_lwpr") String stckLwpr, // 주식 최저가
            @JsonProperty("lwpr_hour") String lwprHour, // 최저가 시간
            @JsonProperty("acml_lwpr_date") String acmlLwprDate, // 누적 최저가 일자
            @JsonProperty("lwpr_vrss_prpr_rate") String lwprVrssPrprRate, // 최저가 대비 현재가 비율
            @JsonProperty("dsgt_date_clpr_vrss_prpr_rate") String dsgtDateClprVrssPrprRate, // 지정 일자 종가 대비 현재가 비
            @JsonProperty("cnnt_ascn_dynu") String cnntAscnDynu, // 연속 상승 일수
            @JsonProperty("hgpr_vrss_prpr_rate") String hgprVrssPrprRate, // 최고가 대비 현재가 비율
            @JsonProperty("cnnt_down_dynu") String cnntDownDynu, // 연속 하락 일수
            @JsonProperty("oprc_vrss_prpr_sign") String oprcVrssPrprSign, // 시가2 대비 현재가 부호
            @JsonProperty("oprc_vrss_prpr") String oprcVrssPrpr, // 시가2 대비 현재가
            @JsonProperty("oprc_vrss_prpr_rate") String oprcVrssPrprRate, // 시가2 대비 현재가 비율
            @JsonProperty("prd_rsfl") String prdRsfl, // 기간 등락
            @JsonProperty("prd_rsfl_rate") String prdRsflRate // 기간 등락 비율
    ) {
    }
}
