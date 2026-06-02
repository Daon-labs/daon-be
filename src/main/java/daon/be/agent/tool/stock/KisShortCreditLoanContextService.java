package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04760000DailyCreditBalanceRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04760000DailyCreditBalanceResponse;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04830000DailyShortSaleRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisFhpst04830000DailyShortSaleResponse;
import daon.be.agent.data.source.kis.quotation.risk.KisHhpst074500C0DailyLoanTransRequest;
import daon.be.agent.data.source.kis.quotation.risk.KisHhpst074500C0DailyLoanTransResponse;
import daon.be.agent.data.source.kis.ranking.KisRankingApiClient;
import daon.be.agent.data.source.kis.ranking.risk.KisFhkst17010000CreditBalanceRankRequest;
import daon.be.agent.data.source.kis.ranking.risk.KisFhkst17010000CreditBalanceRankResponse;
import daon.be.agent.data.source.kis.ranking.risk.KisFhpst04820000ShortSaleRankRequest;
import daon.be.agent.data.source.kis.ranking.risk.KisFhpst04820000ShortSaleRankResponse;
import daon.be.agent.tool.stock.dto.ShortCreditLoanContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class KisShortCreditLoanContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String TOOL_NAME = "GET_SHORT_CREDIT_LOAN_CONTEXT";
    private static final String DEFAULT_MARKET = "J";
    private static final String ALL_MARKET_CODE = "0000";
    private static final String ZERO = "0";
    private static final String SHORT_SALE_RANK_SCREEN_CODE = "20482";
    private static final String CREDIT_BALANCE_SCREEN_CODE = "11701";
    private static final String CREDIT_BALANCE_DAILY_SCREEN_CODE = "20476";
    private static final String DAILY_PERIOD_CODE = "D";

    private final KisQuotationApiClient quotationApiClient;
    private final KisRankingApiClient rankingApiClient;

    public ShortCreditLoanContextDto getShortCreditLoanContext(String stockCode, String startDate, String endDate, String market) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        String normalizedMarket = defaultIfBlank(market, DEFAULT_MARKET);
        String normalizedStartDate = normalizeDate(startDate, LocalDate.now(SEOUL).minusDays(10).format(DATE_FORMAT));
        String normalizedEndDate = normalizeDate(endDate, LocalDate.now(SEOUL).format(DATE_FORMAT));
        OffsetDateTime requestedAt = now();
        List<ShortCreditLoanContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();

        OffsetDateTime shortSaleCalledAt = now();
        KisFhpst04830000DailyShortSaleResponse shortSaleResponse = quotationApiClient.inquireDailyShortSale(
                new KisFhpst04830000DailyShortSaleRequest(normalizedEndDate, normalizedMarket, normalizedStockCode, normalizedStartDate)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST04830000_DAILY_SHORT_SALE, shortSaleCalledAt, shortSaleResponse.rtCd(), shortSaleResponse.msg1()));

        OffsetDateTime creditBalanceCalledAt = now();
        KisFhpst04760000DailyCreditBalanceResponse creditBalanceResponse = quotationApiClient.inquireDailyCreditBalance(
                new KisFhpst04760000DailyCreditBalanceRequest(normalizedMarket, CREDIT_BALANCE_DAILY_SCREEN_CODE, normalizedStockCode, normalizedStartDate)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST04760000_DAILY_CREDIT_BALANCE, creditBalanceCalledAt, creditBalanceResponse.rtCd(), creditBalanceResponse.msg1()));

        OffsetDateTime loanTransCalledAt = now();
        KisHhpst074500C0DailyLoanTransResponse loanTransResponse = quotationApiClient.inquireDailyLoanTrans(
                new KisHhpst074500C0DailyLoanTransRequest("1", normalizedStockCode, normalizedStartDate, normalizedEndDate, "")
        );
        apiCalls.add(apiCall(KisEndpoint.HHPST074500_C0_DAILY_LOAN_TRANS, loanTransCalledAt, loanTransResponse.rtCd(), loanTransResponse.msg1()));

        OffsetDateTime shortSaleRankCalledAt = now();
        KisFhpst04820000ShortSaleRankResponse shortSaleRankResponse = rankingApiClient.inquireShortSaleRank(
                new KisFhpst04820000ShortSaleRankRequest(ZERO, normalizedMarket, SHORT_SALE_RANK_SCREEN_CODE, ALL_MARKET_CODE, DAILY_PERIOD_CODE, ZERO, ZERO, ZERO, ZERO, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHPST04820000_SHORT_SALE_RANK, shortSaleRankCalledAt, shortSaleRankResponse.rtCd(), shortSaleRankResponse.msg1()));

        OffsetDateTime creditBalanceRankCalledAt = now();
        KisFhkst17010000CreditBalanceRankResponse creditBalanceRankResponse = rankingApiClient.inquireCreditBalanceRank(
                new KisFhkst17010000CreditBalanceRankRequest(CREDIT_BALANCE_SCREEN_CODE, ALL_MARKET_CODE, ZERO, normalizedMarket, ZERO)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST17010000_CREDIT_BALANCE_RANK, creditBalanceRankCalledAt, creditBalanceRankResponse.rtCd(), creditBalanceRankResponse.msg1()));

        validateSuccess(KisEndpoint.FHPST04830000_DAILY_SHORT_SALE, shortSaleResponse.rtCd(), shortSaleResponse.msg1());
        validateSuccess(KisEndpoint.FHPST04760000_DAILY_CREDIT_BALANCE, creditBalanceResponse.rtCd(), creditBalanceResponse.msg1());
        validateSuccess(KisEndpoint.HHPST074500_C0_DAILY_LOAN_TRANS, loanTransResponse.rtCd(), loanTransResponse.msg1());
        validateSuccess(KisEndpoint.FHPST04820000_SHORT_SALE_RANK, shortSaleRankResponse.rtCd(), shortSaleRankResponse.msg1());
        validateSuccess(KisEndpoint.FHKST17010000_CREDIT_BALANCE_RANK, creditBalanceRankResponse.rtCd(), creditBalanceRankResponse.msg1());

        List<ShortCreditLoanContextDto.ShortSaleDailyDto> shortSaleTrend = shortSaleTrend(shortSaleResponse.output2());
        List<ShortCreditLoanContextDto.CreditBalanceDailyDto> creditBalanceTrend = creditBalanceTrend(creditBalanceResponse.output());
        List<ShortCreditLoanContextDto.LoanTransactionDailyDto> loanTransactionTrend = loanTransactionTrend(loanTransResponse.output1());
        List<String> cautions = List.of(
                "공매도, 신용잔고, 대차거래는 원인 확정 근거가 아니라 수급 리스크 후보입니다.",
                "공매도 증가가 가격 하락의 확정 원인은 아닙니다.",
                "대차잔고 증가는 실제 공매도 체결과 다릅니다."
        );

        return new ShortCreditLoanContextDto(
                new ShortCreditLoanContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        ShortCreditLoanContextDto.DataFreshness.DAILY_RISK_AND_RANKING,
                        cautions
                ),
                new ShortCreditLoanContextDto.StockIdentityDto(normalizedStockCode, normalizedMarket),
                shortSaleTrend,
                creditBalanceTrend,
                loanTransactionTrend,
                shortSaleRankCandidates(shortSaleRankResponse.output()),
                creditBalanceRankCandidates(creditBalanceRankResponse.output2()),
                riskSummary(shortSaleTrend, creditBalanceTrend, loanTransactionTrend),
                cautions
        );
    }

    private List<ShortCreditLoanContextDto.ShortSaleDailyDto> shortSaleTrend(
            List<KisFhpst04830000DailyShortSaleResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ShortCreditLoanContextDto.ShortSaleDailyDto(
                        output.stckBsopDate(),
                        decimal(output.stckClpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        number(output.sstsCntgQty()),
                        decimal(output.sstsVolRlim()),
                        decimal(output.sstsTrPbmn()),
                        decimal(output.sstsTrPbmnRlim())
                ))
                .toList();
    }

    private List<ShortCreditLoanContextDto.CreditBalanceDailyDto> creditBalanceTrend(
            List<KisFhpst04760000DailyCreditBalanceResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ShortCreditLoanContextDto.CreditBalanceDailyDto(
                        output.dealDate(),
                        output.stlmDate(),
                        decimal(output.stckPrpr()),
                        number(output.wholLoanNewStcn()),
                        number(output.wholLoanRdmpStcn()),
                        number(output.wholLoanRmndStcn()),
                        decimal(output.wholLoanRmndAmt()),
                        decimal(output.wholLoanRmndRate()),
                        number(output.wholStlnRmndStcn()),
                        decimal(output.wholStlnRmndAmt()),
                        decimal(output.wholStlnRmndRate())
                ))
                .toList();
    }

    private List<ShortCreditLoanContextDto.LoanTransactionDailyDto> loanTransactionTrend(
            List<KisHhpst074500C0DailyLoanTransResponse.Output1> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new ShortCreditLoanContextDto.LoanTransactionDailyDto(
                        output.bsopDate(),
                        decimal(output.stckPrpr()),
                        number(output.newStcn()),
                        number(output.rdmpStcn()),
                        number(output.prdyRmndVrss()),
                        number(output.rmndStcn()),
                        decimal(output.rmndAmt())
                ))
                .toList();
    }

    private List<ShortCreditLoanContextDto.RankedStockDto> shortSaleRankCandidates(
            List<KisFhpst04820000ShortSaleRankResponse.Output> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        AtomicInteger rank = new AtomicInteger(1);
        return outputs.stream()
                .map(output -> new ShortCreditLoanContextDto.RankedStockDto(
                        rank.getAndIncrement(),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        decimal(output.sstsVolRlim()),
                        "SHORT_SALE_VOLUME_RATIO"
                ))
                .toList();
    }

    private List<ShortCreditLoanContextDto.RankedStockDto> creditBalanceRankCandidates(
            List<KisFhkst17010000CreditBalanceRankResponse.Output2> outputs
    ) {
        if (outputs == null) {
            return List.of();
        }
        AtomicInteger rank = new AtomicInteger(1);
        return outputs.stream()
                .map(output -> new ShortCreditLoanContextDto.RankedStockDto(
                        rank.getAndIncrement(),
                        output.mkscShrnIscd(),
                        output.htsKorIsnm(),
                        decimal(output.stckPrpr()),
                        decimal(output.prdyVrss()),
                        output.prdyVrssSign(),
                        decimal(output.prdyCtrt()),
                        number(output.acmlVol()),
                        decimal(output.ndayVrssLoanRmndInrt()),
                        "CREDIT_BALANCE_INCREASE_RATE"
                ))
                .toList();
    }

    private ShortCreditLoanContextDto.RiskSummaryDto riskSummary(
            List<ShortCreditLoanContextDto.ShortSaleDailyDto> shortSaleTrend,
            List<ShortCreditLoanContextDto.CreditBalanceDailyDto> creditBalanceTrend,
            List<ShortCreditLoanContextDto.LoanTransactionDailyDto> loanTransactionTrend
    ) {
        ShortCreditLoanContextDto.ShortSaleDailyDto shortSale = shortSaleTrend.isEmpty() ? null : shortSaleTrend.getFirst();
        ShortCreditLoanContextDto.CreditBalanceDailyDto creditBalance = creditBalanceTrend.isEmpty() ? null : creditBalanceTrend.getFirst();
        ShortCreditLoanContextDto.LoanTransactionDailyDto loanTransaction = loanTransactionTrend.isEmpty() ? null : loanTransactionTrend.getFirst();
        return new ShortCreditLoanContextDto.RiskSummaryDto(
                shortSale == null ? null : shortSale.shortSaleQuantity(),
                shortSale == null ? null : shortSale.shortSaleVolumeRatio(),
                creditBalance == null ? null : creditBalance.loanBalanceQuantity(),
                creditBalance == null ? null : creditBalance.loanBalanceRate(),
                loanTransaction == null ? null : loanTransaction.loanBalanceQuantity(),
                loanTransaction == null ? null : loanTransaction.loanBalanceChange()
        );
    }

    private ShortCreditLoanContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new ShortCreditLoanContextDto.KisApiCallSummary(
                endpoint.apiKoreanName(),
                endpoint.trId(),
                endpoint.url(),
                calledAt,
                success,
                success ? null : msg1
        );
    }

    private void validateSuccess(KisEndpoint endpoint, String rtCd, String msg1) {
        if (!KisResponseValidator.isSuccess(rtCd)) {
            throw new IllegalStateException(endpoint.apiKoreanName() + " KIS 응답 실패: " + msg1);
        }
    }

    private BigDecimal decimal(String value) {
        if (isBlank(value)) {
            return null;
        }
        return new BigDecimal(value.trim().replace(",", ""));
    }

    private Long number(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Long.parseLong(value.trim().replace(",", ""));
    }

    private String normalizeRequired(String value, String name) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + "는 필수입니다.");
        }
        return value.trim();
    }

    private String normalizeDate(String value, String defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        return value.trim().replace("-", "");
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
