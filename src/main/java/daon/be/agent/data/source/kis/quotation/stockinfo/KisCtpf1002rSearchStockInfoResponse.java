package daon.be.agent.data.source.kis.quotation.stockinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KIS API: 주식기본조회
 * TR ID: CTPF1002R
 * URL: /uapi/domestic-stock/v1/quotations/search-stock-info
 * PRD-002 사용 여부: o
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KisCtpf1002rSearchStockInfoResponse(
        @JsonProperty("rt_cd") String rtCd, // 성공 실패 여부
        @JsonProperty("msg_cd") String msgCd, // 응답코드
        @JsonProperty("msg1") String msg1, // 응답메세지
        @JsonProperty("output") Output output
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            @JsonProperty("pdno") String pdno, // 상품번호
            @JsonProperty("prdt_type_cd") String prdtTypeCd, // 상품유형코드
            @JsonProperty("mket_id_cd") String mketIdCd, // 시장ID코드
            @JsonProperty("scty_grp_id_cd") String sctyGrpIdCd, // 증권그룹ID코드
            @JsonProperty("excg_dvsn_cd") String excgDvsnCd, // 거래소구분코드
            @JsonProperty("setl_mmdd") String setlMmdd, // 결산월일
            @JsonProperty("lstg_stqt") String lstgStqt, // 상장주수
            @JsonProperty("lstg_cptl_amt") String lstgCptlAmt, // 상장자본금액
            @JsonProperty("cpta") String cpta, // 자본금
            @JsonProperty("papr") String papr, // 액면가
            @JsonProperty("issu_pric") String issuPric, // 발행가격
            @JsonProperty("kospi200_item_yn") String kospi200ItemYn, // 코스피200종목여부
            @JsonProperty("scts_mket_lstg_dt") String sctsMketLstgDt, // 유가증권시장상장일자
            @JsonProperty("scts_mket_lstg_abol_dt") String sctsMketLstgAbolDt, // 유가증권시장상장폐지일자
            @JsonProperty("kosdaq_mket_lstg_dt") String kosdaqMketLstgDt, // 코스닥시장상장일자
            @JsonProperty("kosdaq_mket_lstg_abol_dt") String kosdaqMketLstgAbolDt, // 코스닥시장상장폐지일자
            @JsonProperty("frbd_mket_lstg_dt") String frbdMketLstgDt, // 프리보드시장상장일자
            @JsonProperty("frbd_mket_lstg_abol_dt") String frbdMketLstgAbolDt, // 프리보드시장상장폐지일자
            @JsonProperty("reits_kind_cd") String reitsKindCd, // 리츠종류코드
            @JsonProperty("etf_dvsn_cd") String etfDvsnCd, // ETF구분코드
            @JsonProperty("oilf_fund_yn") String oilfFundYn, // 유전펀드여부
            @JsonProperty("idx_bztp_lcls_cd") String idxBztpLclsCd, // 지수업종대분류코드
            @JsonProperty("idx_bztp_mcls_cd") String idxBztpMclsCd, // 지수업종중분류코드
            @JsonProperty("idx_bztp_scls_cd") String idxBztpSclsCd, // 지수업종소분류코드
            @JsonProperty("stck_kind_cd") String stckKindCd, // 주식종류코드
            @JsonProperty("mfnd_opng_dt") String mfndOpngDt, // 뮤추얼펀드개시일자
            @JsonProperty("mfnd_end_dt") String mfndEndDt, // 뮤추얼펀드종료일자
            @JsonProperty("dpsi_erlm_cncl_dt") String dpsiErlmCnclDt, // 예탁등록취소일자
            @JsonProperty("etf_cu_qty") String etfCuQty, // ETFCU수량
            @JsonProperty("prdt_name") String prdtName, // 상품명
            @JsonProperty("prdt_name120") String prdtName120, // 상품명120
            @JsonProperty("prdt_abrv_name") String prdtAbrvName, // 상품약어명
            @JsonProperty("std_pdno") String stdPdno, // 표준상품번호
            @JsonProperty("prdt_eng_name") String prdtEngName, // 상품영문명
            @JsonProperty("prdt_eng_name120") String prdtEngName120, // 상품영문명120
            @JsonProperty("prdt_eng_abrv_name") String prdtEngAbrvName, // 상품영문약어명
            @JsonProperty("dpsi_aptm_erlm_yn") String dpsiAptmErlmYn, // 예탁지정등록여부
            @JsonProperty("etf_txtn_type_cd") String etfTxtnTypeCd, // ETF과세유형코드
            @JsonProperty("etf_type_cd") String etfTypeCd, // ETF유형코드
            @JsonProperty("lstg_abol_dt") String lstgAbolDt, // 상장폐지일자
            @JsonProperty("nwst_odst_dvsn_cd") String nwstOdstDvsnCd, // 신주구주구분코드
            @JsonProperty("sbst_pric") String sbstPric, // 대용가격
            @JsonProperty("thco_sbst_pric") String thcoSbstPric, // 당사대용가격
            @JsonProperty("thco_sbst_pric_chng_dt") String thcoSbstPricChngDt, // 당사대용가격변경일자
            @JsonProperty("tr_stop_yn") String trStopYn, // 거래정지여부
            @JsonProperty("admn_item_yn") String admnItemYn, // 관리종목여부
            @JsonProperty("thdt_clpr") String thdtClpr, // 당일종가
            @JsonProperty("bfdy_clpr") String bfdyClpr, // 전일종가
            @JsonProperty("clpr_chng_dt") String clprChngDt, // 종가변경일자
            @JsonProperty("std_idst_clsf_cd") String stdIdstClsfCd, // 표준산업분류코드
            @JsonProperty("std_idst_clsf_cd_name") String stdIdstClsfCdName, // 표준산업분류코드명
            @JsonProperty("idx_bztp_lcls_cd_name") String idxBztpLclsCdName, // 지수업종대분류코드명
            @JsonProperty("idx_bztp_mcls_cd_name") String idxBztpMclsCdName, // 지수업종중분류코드명
            @JsonProperty("idx_bztp_scls_cd_name") String idxBztpSclsCdName, // 지수업종소분류코드명
            @JsonProperty("ocr_no") String ocrNo, // OCR번호
            @JsonProperty("crfd_item_yn") String crfdItemYn, // 크라우드펀딩종목여부
            @JsonProperty("elec_scty_yn") String elecSctyYn, // 전자증권여부
            @JsonProperty("issu_istt_cd") String issuIsttCd, // 발행기관코드
            @JsonProperty("etf_chas_erng_rt_dbnb") String etfChasErngRtDbnb, // ETF추적수익율배수
            @JsonProperty("etf_etn_ivst_heed_item_yn") String etfEtnIvstHeedItemYn, // ETFETN투자유의종목여부
            @JsonProperty("stln_int_rt_dvsn_cd") String stlnIntRtDvsnCd, // 대주이자율구분코드
            @JsonProperty("frnr_psnl_lmt_rt") String frnrPsnlLmtRt, // 외국인개인한도비율
            @JsonProperty("lstg_rqsr_issu_istt_cd") String lstgRqsrIssuIsttCd, // 상장신청인발행기관코드
            @JsonProperty("lstg_rqsr_item_cd") String lstgRqsrItemCd, // 상장신청인종목코드
            @JsonProperty("trst_istt_issu_istt_cd") String trstIsttIssuIsttCd, // 신탁기관발행기관코드
            @JsonProperty("cptt_trad_tr_psbl_yn") String cpttTradTrPsblYn, // NXT 거래종목여부
            @JsonProperty("nxt_tr_stop_yn") String nxtTrStopYn // NXT 거래정지여부
    ) {
    }
}
