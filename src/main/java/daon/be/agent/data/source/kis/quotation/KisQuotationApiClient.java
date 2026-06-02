package daon.be.agent.data.source.kis.quotation;

import daon.be.agent.data.source.kis.client.KisRestApiClient;
import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisTrCont;
import daon.be.agent.data.source.kis.quotation.calendar.*;
import daon.be.agent.data.source.kis.quotation.daily.*;
import daon.be.agent.data.source.kis.quotation.event.*;
import daon.be.agent.data.source.kis.quotation.index.*;
import daon.be.agent.data.source.kis.quotation.intraday.*;
import daon.be.agent.data.source.kis.quotation.investor.*;
import daon.be.agent.data.source.kis.quotation.price.*;
import daon.be.agent.data.source.kis.quotation.program.*;
import daon.be.agent.data.source.kis.quotation.risk.*;
import daon.be.agent.data.source.kis.quotation.stockinfo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KisQuotationApiClient {

    private final KisRestApiClient kisRestApiClient;

    public KisFhkst01010100InquirePriceResponse inquirePrice(KisFhkst01010100InquirePriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST01010100_INQUIRE_PRICE, request, KisFhkst01010100InquirePriceResponse.class);
    }

    public KisFhkst01010200AskingPriceResponse inquireAskingPrice(KisFhkst01010200AskingPriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST01010200_ASKING_PRICE, request, KisFhkst01010200AskingPriceResponse.class);
    }

    public KisFhpst01060000TimeItemConclusionResponse inquireTimeItemConclusion(KisFhpst01060000TimeItemConclusionRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01060000_TIME_ITEM_CONCLUSION, request, KisFhpst01060000TimeItemConclusionResponse.class);
    }

    public KisFhkst11300006IntstockMultpriceResponse inquireIntstockMultprice(KisFhkst11300006IntstockMultpriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST11300006_INTSTOCK_MULTPRICE, request, KisFhkst11300006IntstockMultpriceResponse.class);
    }

    public KisFhkst03010200TodayMinuteChartResponse inquireTodayMinuteChart(KisFhkst03010200TodayMinuteChartRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST03010200_TODAY_MINUTE_CHART, request, KisFhkst03010200TodayMinuteChartResponse.class);
    }

    public KisFhkst03010230DailyMinuteChartResponse inquireDailyMinuteChart(KisFhkst03010230DailyMinuteChartRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST03010230_DAILY_MINUTE_CHART, request, KisFhkst03010230DailyMinuteChartResponse.class);
    }

    public KisFhkst03010100DailyItemChartPriceResponse inquireDailyItemChartPrice(KisFhkst03010100DailyItemChartPriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST03010100_DAILY_ITEM_CHART_PRICE, request, KisFhkst03010100DailyItemChartPriceResponse.class);
    }

    public KisFhpup02100000IndexPriceResponse inquireIndexPrice(KisFhpup02100000IndexPriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPUP02100000_INDEX_PRICE, request, KisFhpup02100000IndexPriceResponse.class);
    }

    public KisFhkup03500200IndexMinuteChartResponse inquireIndexMinuteChart(KisFhkup03500200IndexMinuteChartRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKUP03500200_INDEX_MINUTE_CHART, request, KisFhkup03500200IndexMinuteChartResponse.class);
    }

    public KisFhpup02120000IndexDailyPriceResponse inquireIndexDailyPrice(KisFhpup02120000IndexDailyPriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPUP02120000_INDEX_DAILY_PRICE, request, KisFhpup02120000IndexDailyPriceResponse.class);
    }

    public KisCtca0903rCheckHolidayResponse checkHoliday(KisCtca0903rCheckHolidayRequest request) {
        return kisRestApiClient.get(KisEndpoint.CTCA0903R_CHECK_HOLIDAY, request, KisCtca0903rCheckHolidayResponse.class);
    }

    public KisFhpst01390000ViStatusResponse inquireViStatus(KisFhpst01390000ViStatusRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01390000_VI_STATUS, request, KisFhpst01390000ViStatusResponse.class);
    }

    public KisFhkst01011800NewsTitleResponse inquireNewsTitle(KisFhkst01011800NewsTitleRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST01011800_NEWS_TITLE, request, KisFhkst01011800NewsTitleResponse.class);
    }

    public KisFhkst130000C0CaptureUpLowPriceResponse captureUpLowPrice(KisFhkst130000C0CaptureUpLowPriceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST130000_C0_CAPTURE_UP_LOW_PRICE, request, KisFhkst130000C0CaptureUpLowPriceResponse.class);
    }

    public KisFhptj04030000InvestorTimeByMarketResponse inquireInvestorTimeByMarket(KisFhptj04030000InvestorTimeByMarketRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPTJ04030000_INVESTOR_TIME_BY_MARKET, request, KisFhptj04030000InvestorTimeByMarketResponse.class);
    }

    public KisFhptj04040000InvestorDailyByMarketResponse inquireInvestorDailyByMarket(KisFhptj04040000InvestorDailyByMarketRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPTJ04040000_INVESTOR_DAILY_BY_MARKET, request, KisFhptj04040000InvestorDailyByMarketResponse.class);
    }

    public KisFhptj04160001InvestorTradeByStockDailyResponse inquireInvestorTradeByStockDaily(
            KisFhptj04160001InvestorTradeByStockDailyRequest request
    ) {
        return kisRestApiClient.get(
                KisEndpoint.FHPTJ04160001_INVESTOR_TRADE_BY_STOCK_DAILY,
                request,
                KisFhptj04160001InvestorTradeByStockDailyResponse.class
        );
    }

    public KisFhptj04160001InvestorTradeByStockDailyResponse inquireInvestorTradeByStockDaily(
            KisFhptj04160001InvestorTradeByStockDailyRequest request,
            KisTrCont trCont
    ) {
        return kisRestApiClient.get(
                KisEndpoint.FHPTJ04160001_INVESTOR_TRADE_BY_STOCK_DAILY,
                request,
                trCont,
                KisFhptj04160001InvestorTradeByStockDailyResponse.class
        );
    }

    public KisHhptj04160200InvestorTrendEstimateResponse inquireInvestorTrendEstimate(KisHhptj04160200InvestorTrendEstimateRequest request) {
        return kisRestApiClient.get(KisEndpoint.HHPTJ04160200_INVESTOR_TREND_ESTIMATE, request, KisHhptj04160200InvestorTrendEstimateResponse.class);
    }

    public KisFhptj04400000ForeignInstitutionTotalResponse inquireForeignInstitutionTotal(KisFhptj04400000ForeignInstitutionTotalRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPTJ04400000_FOREIGN_INSTITUTION_TOTAL, request, KisFhptj04400000ForeignInstitutionTotalResponse.class);
    }

    public KisFhppg04650101ProgramTradeByStockResponse inquireProgramTradeByStock(KisFhppg04650101ProgramTradeByStockRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPPG04650101_PROGRAM_TRADE_BY_STOCK, request, KisFhppg04650101ProgramTradeByStockResponse.class);
    }

    public KisFhppg04650201ProgramTradeByStockDailyResponse inquireProgramTradeByStockDaily(KisFhppg04650201ProgramTradeByStockDailyRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPPG04650201_PROGRAM_TRADE_BY_STOCK_DAILY, request, KisFhppg04650201ProgramTradeByStockDailyResponse.class);
    }

    public KisFhppg04600101CompProgramTradeTodayResponse inquireCompProgramTradeToday(KisFhppg04600101CompProgramTradeTodayRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPPG04600101_COMP_PROGRAM_TRADE_TODAY, request, KisFhppg04600101CompProgramTradeTodayResponse.class);
    }

    public KisFhppg04600001CompProgramTradeDailyResponse inquireCompProgramTradeDaily(KisFhppg04600001CompProgramTradeDailyRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPPG04600001_COMP_PROGRAM_TRADE_DAILY, request, KisFhppg04600001CompProgramTradeDailyResponse.class);
    }

    public KisHhppg046600C1InvestorProgramTradeTodayResponse inquireInvestorProgramTradeToday(KisHhppg046600C1InvestorProgramTradeTodayRequest request) {
        return kisRestApiClient.get(KisEndpoint.HHPPG046600_C1_INVESTOR_PROGRAM_TRADE_TODAY, request, KisHhppg046600C1InvestorProgramTradeTodayResponse.class);
    }

    public KisFhpst04830000DailyShortSaleResponse inquireDailyShortSale(KisFhpst04830000DailyShortSaleRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST04830000_DAILY_SHORT_SALE, request, KisFhpst04830000DailyShortSaleResponse.class);
    }

    public KisFhpst04760000DailyCreditBalanceResponse inquireDailyCreditBalance(KisFhpst04760000DailyCreditBalanceRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST04760000_DAILY_CREDIT_BALANCE, request, KisFhpst04760000DailyCreditBalanceResponse.class);
    }

    public KisHhpst074500C0DailyLoanTransResponse inquireDailyLoanTrans(KisHhpst074500C0DailyLoanTransRequest request) {
        return kisRestApiClient.get(KisEndpoint.HHPST074500_C0_DAILY_LOAN_TRANS, request, KisHhpst074500C0DailyLoanTransResponse.class);
    }

    public KisCtpf1002rSearchStockInfoResponse searchStockInfo(KisCtpf1002rSearchStockInfoRequest request) {
        return kisRestApiClient.get(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, request, KisCtpf1002rSearchStockInfoResponse.class);
    }

}
