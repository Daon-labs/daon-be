package daon.be.agent.data.source.kis.ranking.valuation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 시장가치 순위
 * TR ID: FHPST01790000
 * URL: /uapi/domestic-stock/v1/ranking/market-value
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01790000MarketValueRankResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("data_rank") String dataRank, // 데이터 순위
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("prdy_vrss") String prdyVrss, // 전일 대비
            @JsonProperty("prdy_vrss_sign") String prdyVrssSign, // 전일 대비 부호
            @JsonProperty("prdy_ctrt") String prdyCtrt, // 전일 대비율
            @JsonProperty("acml_vol") String acmlVol, // 누적 거래량
            @JsonProperty("per") String per, // PER
            @JsonProperty("pbr") String pbr, // PBR
            @JsonProperty("pcr") String pcr, // PCR
            @JsonProperty("psr") String psr, // PSR
            @JsonProperty("eps") String eps, // EPS
            @JsonProperty("eva") String eva, // EVA
            @JsonProperty("ebitda") String ebitda, // EBITDA
            @JsonProperty("pv_div_ebitda") String pvDivEbitda, // PV DIV EBITDA
            @JsonProperty("ebitda_div_fnnc_expn") String ebitdaDivFnncExpn, // EBITDA DIV 금융비용
            @JsonProperty("stac_month") String stacMonth, // 결산 월
            @JsonProperty("stac_month_cls_code") String stacMonthClsCode, // 결산 월 구분 코드
            @JsonProperty("iqry_csnu") String iqryCsnu // 조회 건수
    ) {
    }
}
