package daon.be.agent.data.source.kis.ranking.fluctuation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 등락률 순위
 * TR ID: FHPST01700000
 * URL: /uapi/domestic-stock/v1/ranking/fluctuation
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01700000FluctuationRankRequest(
        @JsonProperty("fid_rsfl_rate2") String fidRsflRate2, // 등락 비율2
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("fid_cond_scr_div_code") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 입력 종목코드
        @JsonProperty("fid_rank_sort_cls_code") String fidRankSortClsCode, // 순위 정렬 구분 코드
        @JsonProperty("fid_input_cnt_1") String fidInputCnt1, // 입력 수1
        @JsonProperty("fid_prc_cls_code") String fidPrcClsCode, // 가격 구분 코드
        @JsonProperty("fid_input_price_1") String fidInputPrice1, // 입력 가격1
        @JsonProperty("fid_input_price_2") String fidInputPrice2, // 입력 가격2
        @JsonProperty("fid_vol_cnt") String fidVolCnt, // 거래량 수
        @JsonProperty("fid_trgt_cls_code") String fidTrgtClsCode, // 대상 구분 코드
        @JsonProperty("fid_trgt_exls_cls_code") String fidTrgtExlsClsCode, // 대상 제외 구분 코드
        @JsonProperty("fid_div_cls_code") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("fid_rsfl_rate1") String fidRsflRate1 // 등락 비율1
) {
}
