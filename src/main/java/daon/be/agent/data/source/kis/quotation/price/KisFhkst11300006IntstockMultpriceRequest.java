package daon.be.agent.data.source.kis.quotation.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 관심종목(멀티종목) 시세조회
 * TR ID: FHKST11300006
 * URL: /uapi/domestic-stock/v1/quotations/intstock-multprice
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisFhkst11300006IntstockMultpriceRequest(
        @JsonProperty("FID_COND_MRKT_DIV_CODE_1") String fidCondMrktDivCode1, // 조건 시장 분류 코드1
        @JsonProperty("FID_INPUT_ISCD_1") String fidInputIscd1, // 입력 종목코드1
        @JsonProperty("FID_COND_MRKT_DIV_CODE_2") String fidCondMrktDivCode2, // 조건 시장 분류 코드2
        @JsonProperty("FID_INPUT_ISCD_2") String fidInputIscd2, // 입력 종목코드2
        @JsonProperty("FID_COND_MRKT_DIV_CODE_3") String fidCondMrktDivCode3, // 조건 시장 분류 코드3
        @JsonProperty("FID_INPUT_ISCD_3") String fidInputIscd3, // 입력 종목코드3
        @JsonProperty("FID_COND_MRKT_DIV_CODE_4") String fidCondMrktDivCode4, // 조건 시장 분류 코드4
        @JsonProperty("FID_INPUT_ISCD_4") String fidInputIscd4, // 입력 종목코드4
        @JsonProperty("FID_COND_MRKT_DIV_CODE_5") String fidCondMrktDivCode5, // 조건 시장 분류 코드5
        @JsonProperty("FID_INPUT_ISCD_5") String fidInputIscd5, // 입력 종목코드5
        @JsonProperty("FID_COND_MRKT_DIV_CODE_6") String fidCondMrktDivCode6, // 조건 시장 분류 코드6
        @JsonProperty("FID_INPUT_ISCD_6") String fidInputIscd6, // 입력 종목코드6
        @JsonProperty("FID_COND_MRKT_DIV_CODE_7") String fidCondMrktDivCode7, // 조건 시장 분류 코드7
        @JsonProperty("FID_INPUT_ISCD_7") String fidInputIscd7, // 입력 종목코드7
        @JsonProperty("FID_COND_MRKT_DIV_CODE_8") String fidCondMrktDivCode8, // 조건 시장 분류 코드8
        @JsonProperty("FID_INPUT_ISCD_8") String fidInputIscd8, // 입력 종목코드8
        @JsonProperty("FID_COND_MRKT_DIV_CODE_9") String fidCondMrktDivCode9, // 조건 시장 분류 코드9
        @JsonProperty("FID_INPUT_ISCD_9") String fidInputIscd9, // 입력 종목코드9
        @JsonProperty("FID_COND_MRKT_DIV_CODE_10") String fidCondMrktDivCode10, // 조건 시장 분류 코드10
        @JsonProperty("FID_INPUT_ISCD_10") String fidInputIscd10, // 입력 종목코드10
        @JsonProperty("FID_COND_MRKT_DIV_CODE_11") String fidCondMrktDivCode11, // 조건 시장 분류 코드11
        @JsonProperty("FID_INPUT_ISCD_11") String fidInputIscd11, // 입력 종목코드11
        @JsonProperty("FID_COND_MRKT_DIV_CODE_12") String fidCondMrktDivCode12, // 조건 시장 분류 코드12
        @JsonProperty("FID_INPUT_ISCD_12") String fidInputIscd12, // 입력 종목코드12
        @JsonProperty("FID_COND_MRKT_DIV_CODE_13") String fidCondMrktDivCode13, // 조건 시장 분류 코드13
        @JsonProperty("FID_INPUT_ISCD_13") String fidInputIscd13, // 입력 종목코드13
        @JsonProperty("FID_COND_MRKT_DIV_CODE_14") String fidCondMrktDivCode14, // 조건 시장 분류 코드14
        @JsonProperty("FID_INPUT_ISCD_14") String fidInputIscd14, // 입력 종목코드14
        @JsonProperty("FID_COND_MRKT_DIV_CODE_15") String fidCondMrktDivCode15, // 조건 시장 분류 코드15
        @JsonProperty("FID_INPUT_ISCD_15") String fidInputIscd15, // 입력 종목코드15
        @JsonProperty("FID_COND_MRKT_DIV_CODE_16") String fidCondMrktDivCode16, // 조건 시장 분류 코드16
        @JsonProperty("FID_INPUT_ISCD_16") String fidInputIscd16, // 입력 종목코드16
        @JsonProperty("FID_COND_MRKT_DIV_CODE_17") String fidCondMrktDivCode17, // 조건 시장 분류 코드17
        @JsonProperty("FID_INPUT_ISCD_17") String fidInputIscd17, // 입력 종목코드17
        @JsonProperty("FID_COND_MRKT_DIV_CODE_18") String fidCondMrktDivCode18, // 조건 시장 분류 코드18
        @JsonProperty("FID_INPUT_ISCD_18") String fidInputIscd18, // 입력 종목코드18
        @JsonProperty("FID_COND_MRKT_DIV_CODE_19") String fidCondMrktDivCode19, // 조건 시장 분류 코드19
        @JsonProperty("FID_INPUT_ISCD_19") String fidInputIscd19, // 입력 종목코드19
        @JsonProperty("FID_COND_MRKT_DIV_CODE_20") String fidCondMrktDivCode20, // 조건 시장 분류 코드20
        @JsonProperty("FID_INPUT_ISCD_20") String fidInputIscd20, // 입력 종목코드20
        @JsonProperty("FID_COND_MRKT_DIV_CODE_21") String fidCondMrktDivCode21, // 조건 시장 분류 코드21
        @JsonProperty("FID_INPUT_ISCD_21") String fidInputIscd21, // 입력 종목코드21
        @JsonProperty("FID_COND_MRKT_DIV_CODE_22") String fidCondMrktDivCode22, // 조건 시장 분류 코드22
        @JsonProperty("FID_INPUT_ISCD_22") String fidInputIscd22, // 입력 종목코드22
        @JsonProperty("FID_COND_MRKT_DIV_CODE_23") String fidCondMrktDivCode23, // 조건 시장 분류 코드23
        @JsonProperty("FID_INPUT_ISCD_23") String fidInputIscd23, // 입력 종목코드23
        @JsonProperty("FID_COND_MRKT_DIV_CODE_24") String fidCondMrktDivCode24, // 조건 시장 분류 코드24
        @JsonProperty("FID_INPUT_ISCD_24") String fidInputIscd24, // 입력 종목코드24
        @JsonProperty("FID_COND_MRKT_DIV_CODE_25") String fidCondMrktDivCode25, // 조건 시장 분류 코드25
        @JsonProperty("FID_INPUT_ISCD_25") String fidInputIscd25, // 입력 종목코드25
        @JsonProperty("FID_COND_MRKT_DIV_CODE_26") String fidCondMrktDivCode26, // 조건 시장 분류 코드26
        @JsonProperty("FID_INPUT_ISCD_26") String fidInputIscd26, // 입력 종목코드26
        @JsonProperty("FID_COND_MRKT_DIV_CODE_27") String fidCondMrktDivCode27, // 조건 시장 분류 코드27
        @JsonProperty("FID_INPUT_ISCD_27") String fidInputIscd27, // 입력 종목코드27
        @JsonProperty("FID_COND_MRKT_DIV_CODE_28") String fidCondMrktDivCode28, // 조건 시장 분류 코드28
        @JsonProperty("FID_INPUT_ISCD_28") String fidInputIscd28, // 입력 종목코드28
        @JsonProperty("FID_COND_MRKT_DIV_CODE_29") String fidCondMrktDivCode29, // 조건 시장 분류 코드29
        @JsonProperty("FID_INPUT_ISCD_29") String fidInputIscd29, // 입력 종목코드29
        @JsonProperty("FID_COND_MRKT_DIV_CODE_30") String fidCondMrktDivCode30, // 조건 시장 분류 코드30
        @JsonProperty("FID_INPUT_ISCD_30") String fidInputIscd30 // 입력 종목코드30
) {
}
