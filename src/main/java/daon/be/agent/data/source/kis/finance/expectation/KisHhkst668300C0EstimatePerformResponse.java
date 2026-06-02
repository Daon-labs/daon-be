package daon.be.agent.data.source.kis.finance.expectation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내주식 종목추정실적
 * TR ID: HHKST668300C0
 * URL: /uapi/domestic-stock/v1/quotations/estimate-perform
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisHhkst668300C0EstimatePerformResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") List<Output2> output2,
        @JsonProperty("output3") List<Output3> output3,
        @JsonProperty("output4") List<Output4> output4
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("sht_cd") String shtCd, // ELW단축종목코드
            @JsonProperty("item_kor_nm") String itemKorNm, // HTS한글종목명
            @JsonProperty("name1") String name1, // ELW현재가
            @JsonProperty("name2") String name2, // 전일대비
            @JsonProperty("estdate") String estdate, // 전일대비부호
            @JsonProperty("rcmd_name") String rcmdName, // 전일대비율
            @JsonProperty("capital") String capital, // 누적거래량
            @JsonProperty("forn_item_lmtrt") String fornItemLmtrt // 행사가
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("data1") String data1, // DATA1
            @JsonProperty("data2") String data2, // DATA2
            @JsonProperty("data3") String data3, // DATA3
            @JsonProperty("data4") String data4, // DATA4
            @JsonProperty("data5") String data5 // DATA5
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output3(
            @JsonProperty("data1") String data1, // DATA1
            @JsonProperty("data2") String data2, // DATA2
            @JsonProperty("data3") String data3, // DATA3
            @JsonProperty("data4") String data4, // DATA4
            @JsonProperty("data5") String data5 // DATA5
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output4(
            @JsonProperty("dt") String dt // 결산년월
    ) {
    }
}
