package daon.be.agent.data.source.kis.quotation.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식현재가 호가/예상체결
 * TR ID: FHKST01010200
 * URL: /uapi/domestic-stock/v1/quotations/inquire-asking-price-exp-ccn
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst01010200AskingPriceResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output1") Output1 output1,
        @JsonProperty("output2") Output2 output2
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output1(
            @JsonProperty("aspr_acpt_hour") String asprAcptHour, // 호가 접수 시간
            @JsonProperty("askp1") String askp1, // 매도호가1
            @JsonProperty("askp2") String askp2, // 매도호가2
            @JsonProperty("askp3") String askp3, // 매도호가3
            @JsonProperty("askp4") String askp4, // 매도호가4
            @JsonProperty("askp5") String askp5, // 매도호가5
            @JsonProperty("askp6") String askp6, // 매도호가6
            @JsonProperty("askp7") String askp7, // 매도호가7
            @JsonProperty("askp8") String askp8, // 매도호가8
            @JsonProperty("askp9") String askp9, // 매도호가9
            @JsonProperty("askp10") String askp10, // 매도호가10
            @JsonProperty("bidp1") String bidp1, // 매수호가1
            @JsonProperty("bidp2") String bidp2, // 매수호가2
            @JsonProperty("bidp3") String bidp3, // 매수호가3
            @JsonProperty("bidp4") String bidp4, // 매수호가4
            @JsonProperty("bidp5") String bidp5, // 매수호가5
            @JsonProperty("bidp6") String bidp6, // 매수호가6
            @JsonProperty("bidp7") String bidp7, // 매수호가7
            @JsonProperty("bidp8") String bidp8, // 매수호가8
            @JsonProperty("bidp9") String bidp9, // 매수호가9
            @JsonProperty("bidp10") String bidp10, // 매수호가10
            @JsonProperty("askp_rsqn1") String askpRsqn1, // 매도호가 잔량1
            @JsonProperty("askp_rsqn2") String askpRsqn2, // 매도호가 잔량2
            @JsonProperty("askp_rsqn3") String askpRsqn3, // 매도호가 잔량3
            @JsonProperty("askp_rsqn4") String askpRsqn4, // 매도호가 잔량4
            @JsonProperty("askp_rsqn5") String askpRsqn5, // 매도호가 잔량5
            @JsonProperty("askp_rsqn6") String askpRsqn6, // 매도호가 잔량6
            @JsonProperty("askp_rsqn7") String askpRsqn7, // 매도호가 잔량7
            @JsonProperty("askp_rsqn8") String askpRsqn8, // 매도호가 잔량8
            @JsonProperty("askp_rsqn9") String askpRsqn9, // 매도호가 잔량9
            @JsonProperty("askp_rsqn10") String askpRsqn10, // 매도호가 잔량10
            @JsonProperty("bidp_rsqn1") String bidpRsqn1, // 매수호가 잔량1
            @JsonProperty("bidp_rsqn2") String bidpRsqn2, // 매수호가 잔량2
            @JsonProperty("bidp_rsqn3") String bidpRsqn3, // 매수호가 잔량3
            @JsonProperty("bidp_rsqn4") String bidpRsqn4, // 매수호가 잔량4
            @JsonProperty("bidp_rsqn5") String bidpRsqn5, // 매수호가 잔량5
            @JsonProperty("bidp_rsqn6") String bidpRsqn6, // 매수호가 잔량6
            @JsonProperty("bidp_rsqn7") String bidpRsqn7, // 매수호가 잔량7
            @JsonProperty("bidp_rsqn8") String bidpRsqn8, // 매수호가 잔량8
            @JsonProperty("bidp_rsqn9") String bidpRsqn9, // 매수호가 잔량9
            @JsonProperty("bidp_rsqn10") String bidpRsqn10, // 매수호가 잔량10
            @JsonProperty("askp_rsqn_icdc1") String askpRsqnIcdc1, // 매도호가 잔량 증감1
            @JsonProperty("askp_rsqn_icdc2") String askpRsqnIcdc2, // 매도호가 잔량 증감2
            @JsonProperty("askp_rsqn_icdc3") String askpRsqnIcdc3, // 매도호가 잔량 증감3
            @JsonProperty("askp_rsqn_icdc4") String askpRsqnIcdc4, // 매도호가 잔량 증감4
            @JsonProperty("askp_rsqn_icdc5") String askpRsqnIcdc5, // 매도호가 잔량 증감5
            @JsonProperty("askp_rsqn_icdc6") String askpRsqnIcdc6, // 매도호가 잔량 증감6
            @JsonProperty("askp_rsqn_icdc7") String askpRsqnIcdc7, // 매도호가 잔량 증감7
            @JsonProperty("askp_rsqn_icdc8") String askpRsqnIcdc8, // 매도호가 잔량 증감8
            @JsonProperty("askp_rsqn_icdc9") String askpRsqnIcdc9, // 매도호가 잔량 증감9
            @JsonProperty("askp_rsqn_icdc10") String askpRsqnIcdc10, // 매도호가 잔량 증감10
            @JsonProperty("bidp_rsqn_icdc1") String bidpRsqnIcdc1, // 매수호가 잔량 증감1
            @JsonProperty("bidp_rsqn_icdc2") String bidpRsqnIcdc2, // 매수호가 잔량 증감2
            @JsonProperty("bidp_rsqn_icdc3") String bidpRsqnIcdc3, // 매수호가 잔량 증감3
            @JsonProperty("bidp_rsqn_icdc4") String bidpRsqnIcdc4, // 매수호가 잔량 증감4
            @JsonProperty("bidp_rsqn_icdc5") String bidpRsqnIcdc5, // 매수호가 잔량 증감5
            @JsonProperty("bidp_rsqn_icdc6") String bidpRsqnIcdc6, // 매수호가 잔량 증감6
            @JsonProperty("bidp_rsqn_icdc7") String bidpRsqnIcdc7, // 매수호가 잔량 증감7
            @JsonProperty("bidp_rsqn_icdc8") String bidpRsqnIcdc8, // 매수호가 잔량 증감8
            @JsonProperty("bidp_rsqn_icdc9") String bidpRsqnIcdc9, // 매수호가 잔량 증감9
            @JsonProperty("bidp_rsqn_icdc10") String bidpRsqnIcdc10, // 매수호가 잔량 증감10
            @JsonProperty("total_askp_rsqn") String totalAskpRsqn, // 총 매도호가 잔량
            @JsonProperty("total_bidp_rsqn") String totalBidpRsqn, // 총 매수호가 잔량
            @JsonProperty("total_askp_rsqn_icdc") String totalAskpRsqnIcdc, // 총 매도호가 잔량 증감
            @JsonProperty("total_bidp_rsqn_icdc") String totalBidpRsqnIcdc, // 총 매수호가 잔량 증감
            @JsonProperty("ovtm_total_askp_icdc") String ovtmTotalAskpIcdc, // 시간외 총 매도호가 증감
            @JsonProperty("ovtm_total_bidp_icdc") String ovtmTotalBidpIcdc, // 시간외 총 매수호가 증감
            @JsonProperty("ovtm_total_askp_rsqn") String ovtmTotalAskpRsqn, // 시간외 총 매도호가 잔량
            @JsonProperty("ovtm_total_bidp_rsqn") String ovtmTotalBidpRsqn, // 시간외 총 매수호가 잔량
            @JsonProperty("ntby_aspr_rsqn") String ntbyAsprRsqn, // 순매수 호가 잔량
            @JsonProperty("new_mkop_cls_code") String newMkopClsCode // 신 장운영 구분 코드
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output2(
            @JsonProperty("antc_mkop_cls_code") String antcMkopClsCode, // 예상 장운영 구분 코드
            @JsonProperty("stck_prpr") String stckPrpr, // 주식 현재가
            @JsonProperty("stck_oprc") String stckOprc, // 주식 시가2
            @JsonProperty("stck_hgpr") String stckHgpr, // 주식 최고가
            @JsonProperty("stck_lwpr") String stckLwpr, // 주식 최저가
            @JsonProperty("stck_sdpr") String stckSdpr, // 주식 기준가
            @JsonProperty("antc_cnpr") String antcCnpr, // 예상 체결가
            @JsonProperty("antc_cntg_vrss_sign") String antcCntgVrssSign, // 예상 체결 대비 부호
            @JsonProperty("antc_cntg_vrss") String antcCntgVrss, // 예상 체결 대비
            @JsonProperty("antc_cntg_prdy_ctrt") String antcCntgPrdyCtrt, // 예상 체결 전일 대비율
            @JsonProperty("antc_vol") String antcVol, // 예상 거래량
            @JsonProperty("stck_shrn_iscd") String stckShrnIscd, // 주식 단축 종목코드
            @JsonProperty("vi_cls_code") String viClsCode // VI적용구분코드
    ) {
    }
}
