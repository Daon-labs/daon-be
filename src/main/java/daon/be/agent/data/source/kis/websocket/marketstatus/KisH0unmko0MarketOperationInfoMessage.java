package daon.be.agent.data.source.kis.websocket.marketstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 국내주식 장운영정보 (통합)
 * TR ID: H0UNMKO0
 * URL: /tryitout/H0UNMKO0
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisH0unmko0MarketOperationInfoMessage(
        @JsonProperty("TRHT_YN") String trhtYn, // 거래정지 여부
        @JsonProperty("TR_SUSP_REAS_CNTT") String trSuspReasCntt, // 거래 정지 사유 내용
        @JsonProperty("MKOP_CLS_CODE") String mkopClsCode, // 장운영 구분 코드
        @JsonProperty("ANTC_MKOP_CLS_CODE") String antcMkopClsCode, // 예상 장운영 구분 코드
        @JsonProperty("MRKT_TRTM_CLS_CODE") String mrktTrtmClsCode, // 임의연장구분코드
        @JsonProperty("DIVI_APP_CLS_CODE") String diviAppClsCode, // 동시호가배분처리구분코드
        @JsonProperty("ISCD_STAT_CLS_CODE") String iscdStatClsCode, // 종목상태구분코드
        @JsonProperty("VI_CLS_CODE") String viClsCode, // VI적용구분코드
        @JsonProperty("OVTM_VI_CLS_CODE") String ovtmViClsCode, // 시간외단일가VI적용구분코드
        @JsonProperty("EXCH_CLS_CODE") String exchClsCode // 거래소 구분코드
) {
}
