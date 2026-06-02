package daon.be.agent.data.source.kis.ranking.trend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 신고/신저근접종목 상위
 * TR ID: FHPST01870000
 * URL: /uapi/domestic-stock/v1/ranking/near-new-highlow
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01870000NearNewHighLowRequest(
        @JsonProperty("fid_aply_rang_vol") String fidAplyRangVol, // 적용 범위 거래량
        @JsonProperty("fid_cond_mrkt_div_code") String fidCondMrktDivCode, // 조건 시장 분류 코드
        @JsonProperty("fid_cond_scr_div_code") String fidCondScrDivCode, // 조건 화면 분류 코드
        @JsonProperty("fid_div_cls_code") String fidDivClsCode, // 분류 구분 코드
        @JsonProperty("fid_input_cnt_1") String fidInputCnt1, // 입력 수1
        @JsonProperty("fid_input_cnt_2") String fidInputCnt2, // 입력 수2
        @JsonProperty("fid_prc_cls_code") String fidPrcClsCode, // 가격 구분 코드
        @JsonProperty("fid_input_iscd") String fidInputIscd, // 입력 종목코드
        @JsonProperty("fid_trgt_cls_code") String fidTrgtClsCode, // 대상 구분 코드
        @JsonProperty("fid_trgt_exls_cls_code") String fidTrgtExlsClsCode, // 대상 제외 구분 코드
        @JsonProperty("fid_aply_rang_prc_1") String fidAplyRangPrc1, // 적용 범위 가격1
        @JsonProperty("fid_aply_rang_prc_2") String fidAplyRangPrc2 // 적용 범위 가격2
) {
}
