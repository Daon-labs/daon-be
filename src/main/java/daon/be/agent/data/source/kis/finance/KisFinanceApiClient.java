package daon.be.agent.data.source.kis.finance;

import daon.be.agent.data.source.kis.client.KisRestApiClient;
import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.finance.expectation.*;
import daon.be.agent.data.source.kis.finance.ratio.*;
import daon.be.agent.data.source.kis.finance.statement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KisFinanceApiClient {

    private final KisRestApiClient kisRestApiClient;

    public KisFhkst66430300FinancialRatioResponse inquireFinancialRatio(KisFhkst66430300FinancialRatioRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST66430300_FINANCIAL_RATIO, request, KisFhkst66430300FinancialRatioResponse.class);
    }

    public KisFhkst66430600StabilityRatioResponse inquireStabilityRatio(KisFhkst66430600StabilityRatioRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST66430600_STABILITY_RATIO, request, KisFhkst66430600StabilityRatioResponse.class);
    }

    public KisFhkst66430200IncomeStatementResponse inquireIncomeStatement(KisFhkst66430200IncomeStatementRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST66430200_INCOME_STATEMENT, request, KisFhkst66430200IncomeStatementResponse.class);
    }

    public KisFhkst66430100BalanceSheetResponse inquireBalanceSheet(KisFhkst66430100BalanceSheetRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST66430100_BALANCE_SHEET, request, KisFhkst66430100BalanceSheetResponse.class);
    }

    public KisFhkst663300C0InvestOpinionResponse inquireInvestOpinion(KisFhkst663300C0InvestOpinionRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST663300_C0_INVEST_OPINION, request, KisFhkst663300C0InvestOpinionResponse.class);
    }

    public KisHhkst668300C0EstimatePerformResponse inquireEstimatePerform(KisHhkst668300C0EstimatePerformRequest request) {
        return kisRestApiClient.get(KisEndpoint.HHKST668300_C0_ESTIMATE_PERFORM, request, KisHhkst668300C0EstimatePerformResponse.class);
    }
}
