package daon.be.agent.data.source.kis.common;

// PRD-002 에서 사용하기로 결정한 KIS API 목록
public enum KisEndpoint {
    WEBSOCKET_APPROVAL("실시간 웹소켓 접속키 발급", "-", "/oauth2/Approval", true),
    FHKST01010100_INQUIRE_PRICE("주식현재가 시세", "FHKST01010100", "/uapi/domestic-stock/v1/quotations/inquire-price", true),
    FHKST01010200_ASKING_PRICE("주식현재가 호가/예상체결", "FHKST01010200", "/uapi/domestic-stock/v1/quotations/inquire-asking-price-exp-ccn", true),
    FHPST01060000_TIME_ITEM_CONCLUSION("주식현재가 당일시간대별체결", "FHPST01060000", "/uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion", true),
    FHKST11300006_INTSTOCK_MULTPRICE("관심종목(멀티종목) 시세조회", "FHKST11300006", "/uapi/domestic-stock/v1/quotations/intstock-multprice", true),
    FHKST03010200_TODAY_MINUTE_CHART("주식당일분봉조회", "FHKST03010200", "/uapi/domestic-stock/v1/quotations/inquire-time-itemchartprice", true),
    FHKST03010230_DAILY_MINUTE_CHART("주식일별분봉조회", "FHKST03010230", "/uapi/domestic-stock/v1/quotations/inquire-time-dailychartprice", true),
    FHKST03010100_DAILY_ITEM_CHART_PRICE("국내주식기간별시세(일/주/월/년)", "FHKST03010100", "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice", true),
    FHPUP02100000_INDEX_PRICE("국내업종 현재지수", "FHPUP02100000", "/uapi/domestic-stock/v1/quotations/inquire-index-price", true),
    FHKUP03500200_INDEX_MINUTE_CHART("업종 분봉조회", "FHKUP03500200", "/uapi/domestic-stock/v1/quotations/inquire-time-indexchartprice", true),
    FHPUP02120000_INDEX_DAILY_PRICE("국내업종 일자별지수", "FHPUP02120000", "/uapi/domestic-stock/v1/quotations/inquire-index-daily-price", true),
    CTCA0903R_CHECK_HOLIDAY("국내휴장일조회", "CTCA0903R", "/uapi/domestic-stock/v1/quotations/chk-holiday", true),
    FHPST01390000_VI_STATUS("변동성완화장치(VI) 현황", "FHPST01390000", "/uapi/domestic-stock/v1/quotations/inquire-vi-status", true),
    FHKST01011800_NEWS_TITLE("종합 시황/공시(제목)", "FHKST01011800", "/uapi/domestic-stock/v1/quotations/news-title", true),
    FHKST130000_C0_CAPTURE_UP_LOW_PRICE("국내주식 상하한가 포착", "FHKST130000C0", "/uapi/domestic-stock/v1/quotations/capture-uplowprice", true),
    FHPTJ04030000_INVESTOR_TIME_BY_MARKET("시장별 투자자매매동향(시세)", "FHPTJ04030000", "/uapi/domestic-stock/v1/quotations/inquire-investor-time-by-market", true),
    FHPTJ04040000_INVESTOR_DAILY_BY_MARKET("시장별 투자자매매동향(일별)", "FHPTJ04040000", "/uapi/domestic-stock/v1/quotations/inquire-investor-daily-by-market", true),
    FHPTJ04160001_INVESTOR_TRADE_BY_STOCK_DAILY("종목별 투자자매매동향(일별)", "FHPTJ04160001", "/uapi/domestic-stock/v1/quotations/investor-trade-by-stock-daily", true),
    HHPTJ04160200_INVESTOR_TREND_ESTIMATE("종목별 외인기관 추정가집계", "HHPTJ04160200", "/uapi/domestic-stock/v1/quotations/investor-trend-estimate", true),
    FHPTJ04400000_FOREIGN_INSTITUTION_TOTAL("국내기관/외국인 매매종목가집계", "FHPTJ04400000", "/uapi/domestic-stock/v1/quotations/foreign-institution-total", true),
    FHPPG04650101_PROGRAM_TRADE_BY_STOCK("종목별 프로그램매매추이(체결)", "FHPPG04650101", "/uapi/domestic-stock/v1/quotations/program-trade-by-stock", true),
    FHPPG04650201_PROGRAM_TRADE_BY_STOCK_DAILY("종목별 프로그램매매추이(일별)", "FHPPG04650201", "/uapi/domestic-stock/v1/quotations/program-trade-by-stock-daily", true),
    FHPPG04600101_COMP_PROGRAM_TRADE_TODAY("프로그램매매 종합현황(시간)", "FHPPG04600101", "/uapi/domestic-stock/v1/quotations/comp-program-trade-today", true),
    FHPPG04600001_COMP_PROGRAM_TRADE_DAILY("프로그램매매 종합현황(일별)", "FHPPG04600001", "/uapi/domestic-stock/v1/quotations/comp-program-trade-daily", true),
    HHPPG046600_C1_INVESTOR_PROGRAM_TRADE_TODAY("프로그램매매 투자자매매동향(당일)", "HHPPG046600C1", "/uapi/domestic-stock/v1/quotations/investor-program-trade-today", true),
    FHPST04830000_DAILY_SHORT_SALE("국내주식 공매도 일별추이", "FHPST04830000", "/uapi/domestic-stock/v1/quotations/daily-short-sale", true),
    FHPST04760000_DAILY_CREDIT_BALANCE("국내주식 신용잔고 일별추이", "FHPST04760000", "/uapi/domestic-stock/v1/quotations/daily-credit-balance", true),
    HHPST074500_C0_DAILY_LOAN_TRANS("종목별 일별 대차거래추이", "HHPST074500C0", "/uapi/domestic-stock/v1/quotations/daily-loan-trans", true),
    CTPF1002R_SEARCH_STOCK_INFO("주식기본조회", "CTPF1002R", "/uapi/domestic-stock/v1/quotations/search-stock-info", true),
    FHKST66430300_FINANCIAL_RATIO("국내주식 재무비율", "FHKST66430300", "/uapi/domestic-stock/v1/finance/financial-ratio", true),
    FHKST66430200_INCOME_STATEMENT("국내주식 손익계산서", "FHKST66430200", "/uapi/domestic-stock/v1/finance/income-statement", true),
    FHKST66430100_BALANCE_SHEET("국내주식 대차대조표", "FHKST66430100", "/uapi/domestic-stock/v1/finance/balance-sheet", true),
    FHKST66430600_STABILITY_RATIO("국내주식 안정성비율", "FHKST66430600", "/uapi/domestic-stock/v1/finance/stability-ratio", true),
    FHKST663300_C0_INVEST_OPINION("국내주식 종목투자의견", "FHKST663300C0", "/uapi/domestic-stock/v1/quotations/invest-opinion", true),
    HHKST668300_C0_ESTIMATE_PERFORM("국내주식 종목추정실적", "HHKST668300C0", "/uapi/domestic-stock/v1/quotations/estimate-perform", true),
    FHPST01710000_VOLUME_RANK("거래량순위", "FHPST01710000", "/uapi/domestic-stock/v1/quotations/volume-rank", true),
    FHPST01700000_FLUCTUATION_RANK("국내주식 등락률 순위", "FHPST01700000", "/uapi/domestic-stock/v1/ranking/fluctuation", true),
    FHPST01680000_VOLUME_POWER("국내주식 체결강도 상위", "FHPST01680000", "/uapi/domestic-stock/v1/ranking/volume-power", true),
    FHKST190900_C0_BULK_TRANS_NUM("국내주식 대량체결건수 상위", "FHKST190900C0", "/uapi/domestic-stock/v1/ranking/bulk-trans-num", true),
    HHMCM000100_C0_HTS_TOP_VIEW("HTS조회상위20종목", "HHMCM000100C0", "/uapi/domestic-stock/v1/ranking/hts-top-view", true),
    FHPST01740000_MARKET_CAP("국내주식 시가총액 상위", "FHPST01740000", "/uapi/domestic-stock/v1/ranking/market-cap", true),
    FHPST01870000_NEAR_NEW_HIGH_LOW("국내주식 신고/신저근접종목 상위", "FHPST01870000", "/uapi/domestic-stock/v1/ranking/near-new-highlow", true),
    FHPST04820000_SHORT_SALE_RANK("국내주식 공매도 상위종목", "FHPST04820000", "/uapi/domestic-stock/v1/ranking/short-sale", true),
    FHKST17010000_CREDIT_BALANCE_RANK("국내주식 신용잔고 상위", "FHKST17010000", "/uapi/domestic-stock/v1/ranking/credit-balance", true),
    FHPST01750000_FINANCE_RATIO_RANK("국내주식 재무비율 순위", "FHPST01750000", "/uapi/domestic-stock/v1/ranking/finance-ratio", true),
    FHPST01790000_MARKET_VALUE_RANK("국내주식 시장가치 순위", "FHPST01790000", "/uapi/domestic-stock/v1/ranking/market-value", true),
    FHPST02350000_OVERTIME_VOLUME("국내주식 시간외거래량순위", "FHPST02350000", "/uapi/domestic-stock/v1/ranking/overtime-volume", true),
    FHPST02340000_OVERTIME_FLUCTUATION("국내주식 시간외등락율순위", "FHPST02340000", "/uapi/domestic-stock/v1/ranking/overtime-fluctuation", true),
    H0UNMKO0_MARKET_OPERATION_INFO("국내주식 장운영정보 (통합)", "H0UNMKO0", "/tryitout/H0UNMKO0", true);

    private final String apiKoreanName;
    private final String trId;
    private final String url;
    private final boolean usedByPrd002;

    KisEndpoint(String apiKoreanName, String trId, String url, boolean usedByPrd002) {
        this.apiKoreanName = apiKoreanName;
        this.trId = trId;
        this.url = url;
        this.usedByPrd002 = usedByPrd002;
    }

    public String apiKoreanName() {
        return apiKoreanName;
    }

    public String trId() {
        return trId;
    }

    public String url() {
        return url;
    }

    public boolean usedByPrd002() {
        return usedByPrd002;
    }
}
