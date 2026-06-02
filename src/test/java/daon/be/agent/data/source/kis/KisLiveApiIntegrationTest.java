package daon.be.agent.data.source.kis;

import daon.be.agent.data.source.kis.finance.KisFinanceApiClient;
import daon.be.agent.data.source.kis.finance.expectation.*;
import daon.be.agent.data.source.kis.finance.ratio.*;
import daon.be.agent.data.source.kis.finance.statement.*;
import daon.be.agent.data.source.kis.oauth.KisTokenService;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
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
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.afterhours.*;
import daon.be.agent.data.source.kis.ranking.attention.*;
import daon.be.agent.data.source.kis.ranking.fluctuation.*;
import daon.be.agent.data.source.kis.ranking.marketcap.*;
import daon.be.agent.data.source.kis.ranking.risk.*;
import daon.be.agent.data.source.kis.ranking.tradepower.*;
import daon.be.agent.data.source.kis.ranking.trend.*;
import daon.be.agent.data.source.kis.ranking.valuation.*;
import daon.be.agent.data.source.kis.ranking.volume.*;
import daon.be.agent.data.source.kis.websocket.approval.KisWebsocketApprovalClient;
import daon.be.agent.data.source.kis.websocket.approval.KisWebsocketApprovalResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // JUnit 테스트 클래스의 인스턴스를 하나만 만들어서 샅애 공유
@Execution(ExecutionMode.SAME_THREAD)       // 테스트를 같은 스레드에서 순차적 실행
@EnabledIfEnvironmentVariable(named = "KIS_APPKEY", matches = ".+")     // 환경변수가 없어도 테스트가 실패하지 않고 스킵
@EnabledIfEnvironmentVariable(named = "KIS_APPSECRET", matches = ".+")
class KisLiveApiIntegrationTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String STOCK_MARKET = "J";
    private static final String INDEX_MARKET = "U";
    private static final String STOCK_CODE = "005930";
    private static final String KOSPI_INDEX_CODE = "0001";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";

    @Autowired
    private KisTokenService kisTokenService;

    @Autowired
    private KisQuotationApiClient quotationApiClient;

    @Autowired
    private KisFinanceApiClient financeApiClient;

    @Autowired
    private KisRankingApiClient rankingApiClient;

    @Autowired
    private KisWebsocketApprovalClient websocketApprovalClient;

    @Test
    void tokenApiReturnsAccessToken() {
        String accessToken = kisTokenService.getAccessToken();

        assertThat(accessToken).isNotBlank();
    }

    @Test
    void websocketApprovalApiReturnsApprovalKey() {
        KisWebsocketApprovalResponse response = websocketApprovalClient.issueApprovalKey();

        assertThat(response).isNotNull();
        assertThat(response.approvalKey()).isNotBlank();
    }

    @TestFactory
    Stream<DynamicTest> restApisReturnSuccessResponse() {
        return liveApiCases().stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase.name(), () -> {
                    try {
                        Object response = testCase.call().call();
                        assertKisSuccess(testCase.name(), response);
                    } finally {
                        throttle();
                    }
                }));
    }

    private List<LiveApiCase> liveApiCases() {
        String today = today();
        String startDate = daysAgo(10);
        String yesterday = daysAgo(1);

        List<LiveApiCase> cases = new ArrayList<>();

        cases.add(api("FHKST01010100 주식현재가 시세", () -> quotationApiClient.inquirePrice(
                new KisFhkst01010100InquirePriceRequest(STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHKST01010200 주식현재가 호가/예상체결", () -> quotationApiClient.inquireAskingPrice(
                new KisFhkst01010200AskingPriceRequest(STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHPST01060000 주식현재가 당일시간대별체결", () -> quotationApiClient.inquireTimeItemConclusion(
                new KisFhpst01060000TimeItemConclusionRequest(STOCK_MARKET, STOCK_CODE, "090000"))));
        cases.add(api("FHKST11300006 관심종목 멀티종목 시세조회", () -> quotationApiClient.inquireIntstockMultprice(
                multpriceRequest())));
        cases.add(api("FHKST03010200 주식당일분봉조회", () -> quotationApiClient.inquireTodayMinuteChart(
                new KisFhkst03010200TodayMinuteChartRequest(STOCK_MARKET, STOCK_CODE, "093000", "N", ""))));
        cases.add(api("FHKST03010230 주식일별분봉조회", () -> quotationApiClient.inquireDailyMinuteChart(
                new KisFhkst03010230DailyMinuteChartRequest(STOCK_MARKET, STOCK_CODE, "153000", yesterday, "N", "N"))));
        cases.add(api("FHKST03010100 국내주식기간별시세", () -> quotationApiClient.inquireDailyItemChartPrice(
                new KisFhkst03010100DailyItemChartPriceRequest(STOCK_MARKET, STOCK_CODE, startDate, today, "D", "0"))));
        cases.add(api("FHPUP02100000 국내업종 현재지수", () -> quotationApiClient.inquireIndexPrice(
                new KisFhpup02100000IndexPriceRequest(INDEX_MARKET, KOSPI_INDEX_CODE))));
        cases.add(api("FHKUP03500200 업종 분봉조회", () -> quotationApiClient.inquireIndexMinuteChart(
                new KisFhkup03500200IndexMinuteChartRequest(INDEX_MARKET, "", KOSPI_INDEX_CODE, "093000", "N"))));
        cases.add(api("FHPUP02120000 국내업종 일자별지수", () -> quotationApiClient.inquireIndexDailyPrice(
                new KisFhpup02120000IndexDailyPriceRequest("D", INDEX_MARKET, KOSPI_INDEX_CODE, startDate))));
        cases.add(api("CTCA0903R 국내휴장일조회", () -> quotationApiClient.checkHoliday(
                new KisCtca0903rCheckHolidayRequest(today, "", ""))));
        cases.add(api("FHPST01390000 변동성완화장치 현황", () -> quotationApiClient.inquireViStatus(
                new KisFhpst01390000ViStatusRequest(ZERO, "20139", STOCK_MARKET, "", ZERO, today, ZERO, ZERO))));
        cases.add(api("FHKST01011800 종합 시황/공시 제목", () -> quotationApiClient.inquireNewsTitle(
                new KisFhkst01011800NewsTitleRequest("", STOCK_MARKET, STOCK_CODE, "", today, "000000", ZERO, ""))));
        cases.add(api("FHKST130000C0 국내주식 상하한가 포착", () -> quotationApiClient.captureUpLowPrice(
                new KisFhkst130000C0CaptureUpLowPriceRequest(STOCK_MARKET, "11300", ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPTJ04030000 시장별 투자자매매동향 시세", () -> quotationApiClient.inquireInvestorTimeByMarket(
                new KisFhptj04030000InvestorTimeByMarketRequest(KOSPI_INDEX_CODE, ALL_MARKET_CODE))));
        cases.add(api("FHPTJ04040000 시장별 투자자매매동향 일별", () -> quotationApiClient.inquireInvestorDailyByMarket(
                new KisFhptj04040000InvestorDailyByMarketRequest(INDEX_MARKET, KOSPI_INDEX_CODE, startDate, "KSP", startDate, KOSPI_INDEX_CODE))));
        cases.add(api("FHPTJ04160001 종목별 투자자매매동향 일별", () -> quotationApiClient.inquireInvestorTradeByStockDaily(
                new KisFhptj04160001InvestorTradeByStockDailyRequest(STOCK_MARKET, STOCK_CODE, startDate, "0", ""))));
        cases.add(api("HHPTJ04160200 종목별 외인기관 추정가집계", () -> quotationApiClient.inquireInvestorTrendEstimate(
                new KisHhptj04160200InvestorTrendEstimateRequest(STOCK_CODE))));
        cases.add(api("FHPTJ04400000 국내기관/외국인 매매종목가집계", () -> quotationApiClient.inquireForeignInstitutionTotal(
                new KisFhptj04400000ForeignInstitutionTotalRequest(STOCK_MARKET, "16449", ALL_MARKET_CODE, ZERO, ZERO, ""))));
        cases.add(api("FHPPG04650101 종목별 프로그램매매추이 체결", () -> quotationApiClient.inquireProgramTradeByStock(
                new KisFhppg04650101ProgramTradeByStockRequest(STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHPPG04650201 종목별 프로그램매매추이 일별", () -> quotationApiClient.inquireProgramTradeByStockDaily(
                new KisFhppg04650201ProgramTradeByStockDailyRequest(STOCK_MARKET, STOCK_CODE, startDate))));
        cases.add(api("FHPPG04600101 프로그램매매 종합현황 시간", () -> quotationApiClient.inquireCompProgramTradeToday(
                new KisFhppg04600101CompProgramTradeTodayRequest(STOCK_MARKET, "K", ZERO, ALL_MARKET_CODE, STOCK_MARKET, "090000"))));
        cases.add(api("FHPPG04600001 프로그램매매 종합현황 일별", () -> quotationApiClient.inquireCompProgramTradeDaily(
                new KisFhppg04600001CompProgramTradeDailyRequest(STOCK_MARKET, "K", startDate, today))));
        cases.add(api("HHPPG046600C1 프로그램매매 투자자매매동향 당일", () -> quotationApiClient.inquireInvestorProgramTradeToday(
                new KisHhppg046600C1InvestorProgramTradeTodayRequest(STOCK_MARKET, "1"))));
        cases.add(api("FHPST04830000 국내주식 공매도 일별추이", () -> quotationApiClient.inquireDailyShortSale(
                new KisFhpst04830000DailyShortSaleRequest(today, STOCK_MARKET, STOCK_CODE, startDate))));
        cases.add(api("FHPST04760000 국내주식 신용잔고 일별추이", () -> quotationApiClient.inquireDailyCreditBalance(
                new KisFhpst04760000DailyCreditBalanceRequest(STOCK_MARKET, "20476", STOCK_CODE, startDate))));
        cases.add(api("HHPST074500C0 종목별 일별 대차거래추이", () -> quotationApiClient.inquireDailyLoanTrans(
                new KisHhpst074500C0DailyLoanTransRequest("1", STOCK_CODE, startDate, today, ""))));
        cases.add(api("CTPF1002R 주식기본조회", () -> quotationApiClient.searchStockInfo(
                new KisCtpf1002rSearchStockInfoRequest("300", STOCK_CODE))));

        cases.add(api("FHKST66430300 국내주식 재무비율", () -> financeApiClient.inquireFinancialRatio(
                new KisFhkst66430300FinancialRatioRequest(ZERO, STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHKST66430600 국내주식 안정성비율", () -> financeApiClient.inquireStabilityRatio(
                new KisFhkst66430600StabilityRatioRequest(STOCK_CODE, ZERO, STOCK_MARKET))));
        cases.add(api("FHKST66430200 국내주식 손익계산서", () -> financeApiClient.inquireIncomeStatement(
                new KisFhkst66430200IncomeStatementRequest(ZERO, STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHKST66430100 국내주식 대차대조표", () -> financeApiClient.inquireBalanceSheet(
                new KisFhkst66430100BalanceSheetRequest(ZERO, STOCK_MARKET, STOCK_CODE))));
        cases.add(api("FHKST663300C0 국내주식 종목투자의견", () -> financeApiClient.inquireInvestOpinion(
                new KisFhkst663300C0InvestOpinionRequest(STOCK_MARKET, "16633", STOCK_CODE, startDate, today))));
        cases.add(api("HHKST668300C0 국내주식 종목추정실적", () -> financeApiClient.inquireEstimatePerform(
                new KisHhkst668300C0EstimatePerformRequest(STOCK_CODE))));

        cases.add(api("FHPST01710000 거래량순위", () -> rankingApiClient.inquireVolumeRank(
                new KisFhpst01710000VolumeRankRequest(STOCK_MARKET, "20171", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST01700000 국내주식 등락률 순위", () -> rankingApiClient.inquireFluctuationRank(
                new KisFhpst01700000FluctuationRankRequest(ZERO, STOCK_MARKET, "20170", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST01680000 국내주식 체결강도 상위", () -> rankingApiClient.inquireVolumePower(
                new KisFhpst01680000VolumePowerRequest(ZERO, STOCK_MARKET, "20168", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHKST190900C0 국내주식 대량체결건수 상위", () -> rankingApiClient.inquireBulkTransNum(
                new KisFhkst190900C0BulkTransNumRequest(ZERO, STOCK_MARKET, "11909", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO))));
        cases.add(api("HHMCM000100C0 HTS조회상위20종목", () -> rankingApiClient.inquireHtsTopView(
                new KisHhmcm000100C0HtsTopViewRequest())));
        cases.add(api("FHPST01740000 국내주식 시가총액 상위", () -> rankingApiClient.inquireMarketCap(
                new KisFhpst01740000MarketCapRequest(ZERO, STOCK_MARKET, "20174", ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST01870000 국내주식 신고/신저근접종목 상위", () -> rankingApiClient.inquireNearNewHighLow(
                new KisFhpst01870000NearNewHighLowRequest(ZERO, STOCK_MARKET, "20187", ZERO, ZERO, ZERO, ZERO, ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST04820000 국내주식 공매도 상위종목", () -> rankingApiClient.inquireShortSaleRank(
                new KisFhpst04820000ShortSaleRankRequest(ZERO, STOCK_MARKET, "20482", ALL_MARKET_CODE, "D", ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHKST17010000 국내주식 신용잔고 상위", () -> rankingApiClient.inquireCreditBalanceRank(
                new KisFhkst17010000CreditBalanceRankRequest("11701", ALL_MARKET_CODE, ZERO, STOCK_MARKET, ZERO))));
        cases.add(api("FHPST01750000 국내주식 재무비율 순위", () -> rankingApiClient.inquireFinanceRatioRank(
                new KisFhpst01750000FinanceRatioRankRequest(ZERO, STOCK_MARKET, "20175", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST01790000 국내주식 시장가치 순위", () -> rankingApiClient.inquireMarketValueRank(
                new KisFhpst01790000MarketValueRankRequest(ZERO, STOCK_MARKET, "20179", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST02350000 국내주식 시간외거래량순위", () -> rankingApiClient.inquireOvertimeVolume(
                new KisFhpst02350000OvertimeVolumeRequest(STOCK_MARKET, "20235", ALL_MARKET_CODE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO))));
        cases.add(api("FHPST02340000 국내주식 시간외등락율순위", () -> rankingApiClient.inquireOvertimeFluctuation(
                new KisFhpst02340000OvertimeFluctuationRequest(STOCK_MARKET, "", "20234", ALL_MARKET_CODE, "2", "", "", "", "", ""))));

        return cases;
    }

    private KisFhkst11300006IntstockMultpriceRequest multpriceRequest() {
        return new KisFhkst11300006IntstockMultpriceRequest(
                STOCK_MARKET, STOCK_CODE,
                STOCK_MARKET, "000660",
                STOCK_MARKET, "035420",
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null
        );
    }

    private void assertKisSuccess(String apiName, Object response) throws ReflectiveOperationException {
        assertThat(response).as(apiName + " response").isNotNull();

        Method rtCdMethod = response.getClass().getMethod("rtCd");
        Method msgCdMethod = response.getClass().getMethod("msgCd");
        Method msg1Method = response.getClass().getMethod("msg1");

        String rtCd = (String) rtCdMethod.invoke(response);
        String msgCd = (String) msgCdMethod.invoke(response);
        String msg1 = (String) msg1Method.invoke(response);

        assertThat(rtCd)
                .as("%s KIS response: rt_cd=%s, msg_cd=%s, msg1=%s", apiName, rtCd, msgCd, msg1)
                .isEqualTo("0");
    }

    private LiveApiCase api(String name, Callable<Object> call) {
        return new LiveApiCase(name, call);
    }

    private String today() {
        return LocalDate.now(SEOUL).format(DATE_FORMATTER);
    }

    private String daysAgo(int days) {
        return LocalDate.now(SEOUL).minusDays(days).format(DATE_FORMATTER);
    }

    private void throttle() throws InterruptedException {
        Thread.sleep(250);
    }

    private record LiveApiCase(String name, Callable<Object> call) {
    }
}
