package daon.be.agent.data.source.kis.quotation.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * KIS API: 국내휴장일조회
 * TR ID: CTCA0903R
 * URL: /uapi/domestic-stock/v1/quotations/chk-holiday
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisCtca0903rCheckHolidayResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") List<Output> output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("bass_dt") String bassDt, // 기준일자
            @JsonProperty("wday_dvsn_cd") String wdayDvsnCd, // 요일구분코드
            @JsonProperty("bzdy_yn") String bzdyYn, // 영업일여부
            @JsonProperty("tr_day_yn") String trDayYn, // 거래일여부
            @JsonProperty("opnd_yn") String opndYn, // 개장일여부
            @JsonProperty("sttl_day_yn") String sttlDayYn // 결제일여부
    ) {
    }
}
