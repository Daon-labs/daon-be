package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 변동성완화장치(VI) 현황
 * TR ID: FHPST01390000
 * URL: /uapi/domestic-stock/v1/quotations/inquire-vi-status
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhpst01390000ViStatusResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("hts_kor_isnm") String htsKorIsnm, // HTS 한글 종목명
            @JsonProperty("mksc_shrn_iscd") String mkscShrnIscd, // 유가증권 단축 종목코드
            @JsonProperty("vi_cls_code") String viClsCode, // VI발동상태
            @JsonProperty("bsop_date") String bsopDate, // 영업 일자
            @JsonProperty("cntg_vi_hour") String cntgViHour, // VI발동시간
            @JsonProperty("vi_cncl_hour") String viCnclHour, // VI해제시간
            @JsonProperty("vi_kind_code") String viKindCode, // VI종류코드
            @JsonProperty("vi_prc") String viPrc, // VI발동가격
            @JsonProperty("vi_stnd_prc") String viStndPrc, // 정적VI발동기준가격
            @JsonProperty("vi_dprt") String viDprt, // 정적VI발동괴리율
            @JsonProperty("vi_dmc_stnd_prc") String viDmcStndPrc, // 동적VI발동기준가격
            @JsonProperty("vi_dmc_dprt") String viDmcDprt, // 동적VI발동괴리율
            @JsonProperty("vi_count") String viCount // VI발동횟수
    ) {
    }
}
