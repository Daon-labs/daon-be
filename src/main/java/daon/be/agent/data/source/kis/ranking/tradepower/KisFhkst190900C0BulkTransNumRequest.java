package daon.be.agent.data.source.kis.ranking.tradepower;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 대량체결건수 상위
 * TR ID: FHKST190900C0
 * URL: /uapi/domestic-stock/v1/ranking/bulk-trans-num
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst190900C0BulkTransNumRequest(
        @JsonProperty("fid_aply_rang_prc_2") String fidAplyRangPrc2, // 적용 범위 가격2
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("fid_cond_scr_div_code") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 입력 종목코드
        @JsonProperty("fid_rank_sort_cls_code") String fidRankSortClsCode, // 순위 정렬 구분 코드
        @JsonProperty("fid_div_cls_code") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("fid_input_price_1") String fidInputPrice1, // 입력 가격1
        @JsonProperty("fid_aply_rang_prc_1") String fidAplyRangPrc1, // 적용 범위 가격1
        @JsonProperty("fid_input_iscd_2") String fidInputIscd2, // 입력 종목코드2
        @JsonProperty("fid_trgt_exls_cls_code") String fidTrgtExlsClsCode, // 대상 제외 구분 코드
        @JsonProperty("fid_trgt_cls_code") String fidTrgtClsCode, // 대상 구분 코드
        @JsonProperty("fid_vol_cnt") String fidVolCnt // 거래량 수
) {
}
