package daon.be.agent.data.source.kis.ranking.valuation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 재무비율 순위
 * TR ID: FHPST01750000
 * URL: /uapi/domestic-stock/v1/ranking/finance-ratio
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01750000FinanceRatioRankResponse(
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
            @JsonProperty("cptl_op_prfi") String cptlOpPrfi, // 총자본경상이익율
            @JsonProperty("cptl_ntin_rate") String cptlNtinRate, // 총자본 순이익율
            @JsonProperty("sale_totl_rate") String saleTotlRate, // 매출액 총이익율
            @JsonProperty("sale_ntin_rate") String saleNtinRate, // 매출액 순이익율
            @JsonProperty("bis") String bis, // 자기자본비율
            @JsonProperty("lblt_rate") String lbltRate, // 부채 비율
            @JsonProperty("bram_depn") String bramDepn, // 차입금 의존도
            @JsonProperty("rsrv_rate") String rsrvRate, // 유보 비율
            @JsonProperty("grs") String grs, // 매출액 증가율
            @JsonProperty("op_prfi_inrt") String opPrfiInrt, // 경상 이익 증가율
            @JsonProperty("bsop_prfi_inrt") String bsopPrfiInrt, // 영업 이익 증가율
            @JsonProperty("ntin_inrt") String ntinInrt, // 순이익 증가율
            @JsonProperty("equt_inrt") String equtInrt, // 자기자본 증가율
            @JsonProperty("cptl_tnrt") String cptlTnrt, // 총자본회전율
            @JsonProperty("sale_bond_tnrt") String saleBondTnrt, // 매출 채권 회전율
            @JsonProperty("totl_aset_inrt") String totlAsetInrt, // 총자산 증가율
            @JsonProperty("stac_month") String stacMonth, // 결산 월
            @JsonProperty("stac_month_cls_code") String stacMonthClsCode, // 결산 월 구분 코드
            @JsonProperty("iqry_csnu") String iqryCsnu // 조회 건수
    ) {
    }
}
