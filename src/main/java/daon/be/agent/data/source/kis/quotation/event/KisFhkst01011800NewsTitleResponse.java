package daon.be.agent.data.source.kis.quotation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 종합 시황/공시(제목)
 * TR ID: FHKST01011800
 * URL: /uapi/domestic-stock/v1/quotations/news-title
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst01011800NewsTitleResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("cntt_usiq_srno") String cnttUsiqSrno, // 내용 조회용 일련번호
            @JsonProperty("news_ofer_entp_code") String newsOferEntpCode, // 뉴스 제공 업체 코드
            @JsonProperty("data_dt") String dataDt, // 작성일자
            @JsonProperty("data_tm") String dataTm, // 작성시간
            @JsonProperty("hts_pbnt_titl_cntt") String htsPbntTitlCntt, // HTS 공시 제목 내용
            @JsonProperty("news_lrdv_code") String newsLrdvCode, // 뉴스 대구분
            @JsonProperty("dorg") String dorg, // 자료원
            @JsonProperty("iscd1") String iscd1, // 종목 코드1
            @JsonProperty("iscd2") String iscd2, // 종목 코드2
            @JsonProperty("iscd3") String iscd3, // 종목 코드3
            @JsonProperty("iscd4") String iscd4, // 종목 코드4
            @JsonProperty("iscd5") String iscd5 // 종목 코드5
    ) {
    }
}
