package daon.be.agent.tool.stock;

import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.common.KisResponseValidator;
import daon.be.agent.data.source.kis.finance.KisFinanceApiClient;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430300FinancialRatioRequest;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430300FinancialRatioResponse;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430600StabilityRatioRequest;
import daon.be.agent.data.source.kis.finance.ratio.KisFhkst66430600StabilityRatioResponse;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430100BalanceSheetRequest;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430100BalanceSheetResponse;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430200IncomeStatementRequest;
import daon.be.agent.data.source.kis.finance.statement.KisFhkst66430200IncomeStatementResponse;
import daon.be.agent.data.source.kis.quotation.KisQuotationApiClient;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoRequest;
import daon.be.agent.data.source.kis.quotation.stockinfo.KisCtpf1002rSearchStockInfoResponse;
import daon.be.agent.tool.stock.dto.FundamentalContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KisFundamentalContextService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final String TOOL_NAME = "GET_FUNDAMENTAL_CONTEXT";
    private static final String STOCK_MARKET_CODE = "J";
    private static final String STOCK_PRODUCT_TYPE_CODE = "300";
    private static final String DEFAULT_DIVISION_CODE = "0";
    private static final String ABNORMAL_99_99 = "99.99";

    private final KisQuotationApiClient quotationApiClient;
    private final KisFinanceApiClient financeApiClient;

    public FundamentalContextDto getFundamentalContext(String stockCode) {
        String normalizedStockCode = normalizeRequired(stockCode, "stockCode");
        OffsetDateTime requestedAt = now();
        List<FundamentalContextDto.KisApiCallSummary> apiCalls = new ArrayList<>();
        List<String> dataQualityNotes = new ArrayList<>();

        OffsetDateTime stockInfoCalledAt = now();
        KisCtpf1002rSearchStockInfoResponse stockInfoResponse = quotationApiClient.searchStockInfo(
                new KisCtpf1002rSearchStockInfoRequest(STOCK_PRODUCT_TYPE_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoCalledAt, stockInfoResponse.rtCd(), stockInfoResponse.msg1()));

        OffsetDateTime financialRatioCalledAt = now();
        KisFhkst66430300FinancialRatioResponse financialRatioResponse = financeApiClient.inquireFinancialRatio(
                new KisFhkst66430300FinancialRatioRequest(DEFAULT_DIVISION_CODE, STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST66430300_FINANCIAL_RATIO, financialRatioCalledAt, financialRatioResponse.rtCd(), financialRatioResponse.msg1()));

        OffsetDateTime incomeStatementCalledAt = now();
        KisFhkst66430200IncomeStatementResponse incomeStatementResponse = financeApiClient.inquireIncomeStatement(
                new KisFhkst66430200IncomeStatementRequest(DEFAULT_DIVISION_CODE, STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST66430200_INCOME_STATEMENT, incomeStatementCalledAt, incomeStatementResponse.rtCd(), incomeStatementResponse.msg1()));

        OffsetDateTime balanceSheetCalledAt = now();
        KisFhkst66430100BalanceSheetResponse balanceSheetResponse = financeApiClient.inquireBalanceSheet(
                new KisFhkst66430100BalanceSheetRequest(DEFAULT_DIVISION_CODE, STOCK_MARKET_CODE, normalizedStockCode)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST66430100_BALANCE_SHEET, balanceSheetCalledAt, balanceSheetResponse.rtCd(), balanceSheetResponse.msg1()));

        OffsetDateTime stabilityRatioCalledAt = now();
        KisFhkst66430600StabilityRatioResponse stabilityRatioResponse = financeApiClient.inquireStabilityRatio(
                new KisFhkst66430600StabilityRatioRequest(normalizedStockCode, DEFAULT_DIVISION_CODE, STOCK_MARKET_CODE)
        );
        apiCalls.add(apiCall(KisEndpoint.FHKST66430600_STABILITY_RATIO, stabilityRatioCalledAt, stabilityRatioResponse.rtCd(), stabilityRatioResponse.msg1()));

        validateSuccess(KisEndpoint.CTPF1002R_SEARCH_STOCK_INFO, stockInfoResponse.rtCd(), stockInfoResponse.msg1());
        validateSuccess(KisEndpoint.FHKST66430300_FINANCIAL_RATIO, financialRatioResponse.rtCd(), financialRatioResponse.msg1());
        validateSuccess(KisEndpoint.FHKST66430200_INCOME_STATEMENT, incomeStatementResponse.rtCd(), incomeStatementResponse.msg1());
        validateSuccess(KisEndpoint.FHKST66430100_BALANCE_SHEET, balanceSheetResponse.rtCd(), balanceSheetResponse.msg1());
        validateSuccess(KisEndpoint.FHKST66430600_STABILITY_RATIO, stabilityRatioResponse.rtCd(), stabilityRatioResponse.msg1());

        List<FundamentalContextDto.FinancialRatioPeriodDto> financialRatios = financialRatios(financialRatioResponse.output(), dataQualityNotes);
        List<FundamentalContextDto.IncomeStatementPeriodDto> incomeStatements = incomeStatements(incomeStatementResponse.output(), dataQualityNotes);
        List<FundamentalContextDto.BalanceSheetPeriodDto> balanceSheets = balanceSheets(balanceSheetResponse.output(), dataQualityNotes);
        List<FundamentalContextDto.StabilityRatioPeriodDto> stabilityRatios = stabilityRatios(stabilityRatioResponse.output(), dataQualityNotes);

        return new FundamentalContextDto(
                new FundamentalContextDto.ToolEvidenceMeta(
                        TOOL_NAME,
                        requestedAt,
                        now(),
                        List.copyOf(apiCalls),
                        FundamentalContextDto.DataFreshness.DAILY_OR_PERIODIC_FINANCIAL,
                        List.of("재무/안정성 데이터는 실시간 가격 데이터가 아니라 결산·공시 기준 주기성 데이터입니다.")
                ),
                identity(normalizedStockCode, stockInfoResponse.output()),
                financialRatios,
                incomeStatements,
                balanceSheets,
                stabilityRatios,
                summary(financialRatios, incomeStatements, balanceSheets, stabilityRatios),
                List.copyOf(dataQualityNotes)
        );
    }

    private FundamentalContextDto.StockIdentityDto identity(
            String stockCode,
            KisCtpf1002rSearchStockInfoResponse.Output stockInfo
    ) {
        return new FundamentalContextDto.StockIdentityDto(
                firstNonBlank(stockInfo == null ? null : stockInfo.pdno(), stockCode),
                stockInfo == null ? null : firstNonBlank(stockInfo.prdtName(), stockInfo.prdtAbrvName()),
                stockInfo == null ? null : stockInfo.mketIdCd(),
                stockInfo == null ? null : stockInfo.excgDvsnCd(),
                stockInfo == null ? null : firstNonBlank(stockInfo.idxBztpSclsCd(), stockInfo.stdIdstClsfCd()),
                stockInfo == null ? null : firstNonBlank(stockInfo.idxBztpSclsCdName(), stockInfo.stdIdstClsfCdName()),
                stockInfo == null ? null : number(stockInfo.lstgStqt()),
                stockInfo == null ? null : decimal(stockInfo.lstgCptlAmt()),
                stockInfo == null ? null : decimal(stockInfo.papr()),
                stockInfo == null ? null : firstNonBlank(stockInfo.sctsMketLstgDt(), stockInfo.kosdaqMketLstgDt(), stockInfo.frbdMketLstgDt()),
                stockInfo != null && ynToBoolean(stockInfo.trStopYn()),
                stockInfo != null && ynToBoolean(stockInfo.admnItemYn()),
                stockInfo != null && ynToBoolean(stockInfo.cpttTradTrPsblYn())
        );
    }

    private List<FundamentalContextDto.FinancialRatioPeriodDto> financialRatios(
            List<KisFhkst66430300FinancialRatioResponse.Output> outputs,
            List<String> dataQualityNotes
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new FundamentalContextDto.FinancialRatioPeriodDto(
                        output.stacYymm(),
                        decimal(output.grs(), output.stacYymm(), "매출액 증가율", dataQualityNotes),
                        decimal(output.bsopPrfiInrt(), output.stacYymm(), "영업 이익 증가율", dataQualityNotes),
                        decimal(output.ntinInrt(), output.stacYymm(), "순이익 증가율", dataQualityNotes),
                        decimal(output.roeVal(), output.stacYymm(), "ROE", dataQualityNotes),
                        decimal(output.eps(), output.stacYymm(), "EPS", dataQualityNotes),
                        decimal(output.sps(), output.stacYymm(), "주당매출액", dataQualityNotes),
                        decimal(output.bps(), output.stacYymm(), "BPS", dataQualityNotes),
                        decimal(output.rsrvRate(), output.stacYymm(), "유보 비율", dataQualityNotes),
                        decimal(output.lbltRate(), output.stacYymm(), "부채 비율", dataQualityNotes)
                ))
                .toList();
    }

    private List<FundamentalContextDto.IncomeStatementPeriodDto> incomeStatements(
            List<KisFhkst66430200IncomeStatementResponse.Output> outputs,
            List<String> dataQualityNotes
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new FundamentalContextDto.IncomeStatementPeriodDto(
                        output.stacYymm(),
                        decimal(output.saleAccount(), output.stacYymm(), "매출액", dataQualityNotes),
                        decimal(output.saleCost(), output.stacYymm(), "매출 원가", dataQualityNotes),
                        decimal(output.saleTotlPrfi(), output.stacYymm(), "매출 총 이익", dataQualityNotes),
                        decimal(output.deprCost(), output.stacYymm(), "감가상각비", dataQualityNotes),
                        decimal(output.sellMang(), output.stacYymm(), "판매 및 관리비", dataQualityNotes),
                        decimal(output.bsopPrti(), output.stacYymm(), "영업 이익", dataQualityNotes),
                        decimal(output.bsopNonErnn(), output.stacYymm(), "영업 외 수익", dataQualityNotes),
                        decimal(output.bsopNonExpn(), output.stacYymm(), "영업 외 비용", dataQualityNotes),
                        decimal(output.opPrfi(), output.stacYymm(), "경상 이익", dataQualityNotes),
                        decimal(output.specPrfi(), output.stacYymm(), "특별 이익", dataQualityNotes),
                        decimal(output.specLoss(), output.stacYymm(), "특별 손실", dataQualityNotes),
                        decimal(output.thtrNtin(), output.stacYymm(), "당기순이익", dataQualityNotes)
                ))
                .toList();
    }

    private List<FundamentalContextDto.BalanceSheetPeriodDto> balanceSheets(
            List<KisFhkst66430100BalanceSheetResponse.Output> outputs,
            List<String> dataQualityNotes
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new FundamentalContextDto.BalanceSheetPeriodDto(
                        output.stacYymm(),
                        decimal(output.cras(), output.stacYymm(), "유동자산", dataQualityNotes),
                        decimal(output.fxas(), output.stacYymm(), "고정자산", dataQualityNotes),
                        decimal(output.totalAset(), output.stacYymm(), "자산총계", dataQualityNotes),
                        decimal(output.flowLblt(), output.stacYymm(), "유동부채", dataQualityNotes),
                        decimal(output.fixLblt(), output.stacYymm(), "고정부채", dataQualityNotes),
                        decimal(output.totalLblt(), output.stacYymm(), "부채총계", dataQualityNotes),
                        decimal(output.cpfn(), output.stacYymm(), "자본금", dataQualityNotes),
                        decimal(output.cfpSurp(), output.stacYymm(), "자본 잉여금", dataQualityNotes),
                        decimal(output.prfiSurp(), output.stacYymm(), "이익 잉여금", dataQualityNotes),
                        decimal(output.totalCptl(), output.stacYymm(), "자본총계", dataQualityNotes)
                ))
                .toList();
    }

    private List<FundamentalContextDto.StabilityRatioPeriodDto> stabilityRatios(
            List<KisFhkst66430600StabilityRatioResponse.Output> outputs,
            List<String> dataQualityNotes
    ) {
        if (outputs == null) {
            return List.of();
        }
        return outputs.stream()
                .map(output -> new FundamentalContextDto.StabilityRatioPeriodDto(
                        output.stacYymm(),
                        decimal(output.lbltRate(), output.stacYymm(), "부채 비율", dataQualityNotes),
                        decimal(output.bramDepn(), output.stacYymm(), "차입금 의존도", dataQualityNotes),
                        decimal(output.crntRate(), output.stacYymm(), "유동 비율", dataQualityNotes),
                        decimal(output.quckRate(), output.stacYymm(), "당좌 비율", dataQualityNotes)
                ))
                .toList();
    }

    private FundamentalContextDto.FundamentalSummaryDto summary(
            List<FundamentalContextDto.FinancialRatioPeriodDto> financialRatios,
            List<FundamentalContextDto.IncomeStatementPeriodDto> incomeStatements,
            List<FundamentalContextDto.BalanceSheetPeriodDto> balanceSheets,
            List<FundamentalContextDto.StabilityRatioPeriodDto> stabilityRatios
    ) {
        FundamentalContextDto.FinancialRatioPeriodDto ratio = financialRatios.isEmpty() ? null : financialRatios.getFirst();
        FundamentalContextDto.IncomeStatementPeriodDto income = incomeStatements.isEmpty() ? null : incomeStatements.getFirst();
        FundamentalContextDto.BalanceSheetPeriodDto balance = balanceSheets.isEmpty() ? null : balanceSheets.getFirst();
        FundamentalContextDto.StabilityRatioPeriodDto stability = stabilityRatios.isEmpty() ? null : stabilityRatios.getFirst();
        return new FundamentalContextDto.FundamentalSummaryDto(
                firstNonBlank(
                        ratio == null ? null : ratio.period(),
                        income == null ? null : income.period(),
                        balance == null ? null : balance.period(),
                        stability == null ? null : stability.period()
                ),
                ratio == null ? null : ratio.eps(),
                ratio == null ? null : ratio.bps(),
                ratio == null ? null : ratio.roe(),
                firstNonNull(stability == null ? null : stability.debtRatio(), ratio == null ? null : ratio.debtRatio()),
                income == null ? null : income.operatingProfit(),
                income == null ? null : income.netIncome(),
                balance == null ? null : balance.totalAssets(),
                balance == null ? null : balance.totalEquity()
        );
    }

    private FundamentalContextDto.KisApiCallSummary apiCall(KisEndpoint endpoint, OffsetDateTime calledAt, String rtCd, String msg1) {
        boolean success = KisResponseValidator.isSuccess(rtCd);
        return new FundamentalContextDto.KisApiCallSummary(
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

    private BigDecimal decimal(String value, String period, String label, List<String> dataQualityNotes) {
        if (isBlank(value)) {
            return null;
        }
        String normalized = value.trim().replace(",", "");
        if (ABNORMAL_99_99.equals(normalized)) {
            dataQualityNotes.add(period + " " + label + "은 KIS 비정상 표시값 99.99로 판단해 null 처리했습니다.");
            return null;
        }
        return new BigDecimal(normalized);
    }

    private Long number(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Long.parseLong(value.trim().replace(",", ""));
    }

    private boolean ynToBoolean(String value) {
        return "Y".equalsIgnoreCase(value);
    }

    private <T> T firstNonNull(T first, T second) {
        return first != null ? first : second;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private String normalizeRequired(String value, String name) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + "는 필수입니다.");
        }
        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now(SEOUL);
    }
}
