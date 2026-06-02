package daon.be.agent.data.source.kis.ranking;

import daon.be.agent.data.source.kis.client.KisRestApiClient;
import daon.be.agent.data.source.kis.common.KisEndpoint;
import daon.be.agent.data.source.kis.ranking.afterhours.*;
import daon.be.agent.data.source.kis.ranking.attention.*;
import daon.be.agent.data.source.kis.ranking.fluctuation.*;
import daon.be.agent.data.source.kis.ranking.marketcap.*;
import daon.be.agent.data.source.kis.ranking.risk.*;
import daon.be.agent.data.source.kis.ranking.tradepower.*;
import daon.be.agent.data.source.kis.ranking.trend.*;
import daon.be.agent.data.source.kis.ranking.valuation.*;
import daon.be.agent.data.source.kis.ranking.volume.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KisRankingApiClient {

    private final KisRestApiClient kisRestApiClient;

    public KisFhpst01710000VolumeRankResponse inquireVolumeRank(KisFhpst01710000VolumeRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01710000_VOLUME_RANK, request, KisFhpst01710000VolumeRankResponse.class);
    }

    public KisFhpst01700000FluctuationRankResponse inquireFluctuationRank(KisFhpst01700000FluctuationRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01700000_FLUCTUATION_RANK, request, KisFhpst01700000FluctuationRankResponse.class);
    }

    public KisFhpst01680000VolumePowerResponse inquireVolumePower(KisFhpst01680000VolumePowerRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01680000_VOLUME_POWER, request, KisFhpst01680000VolumePowerResponse.class);
    }

    public KisFhkst190900C0BulkTransNumResponse inquireBulkTransNum(KisFhkst190900C0BulkTransNumRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST190900_C0_BULK_TRANS_NUM, request, KisFhkst190900C0BulkTransNumResponse.class);
    }

    public KisHhmcm000100C0HtsTopViewResponse inquireHtsTopView(KisHhmcm000100C0HtsTopViewRequest request) {
        return kisRestApiClient.get(KisEndpoint.HHMCM000100_C0_HTS_TOP_VIEW, request, KisHhmcm000100C0HtsTopViewResponse.class);
    }

    public KisFhpst01740000MarketCapResponse inquireMarketCap(KisFhpst01740000MarketCapRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01740000_MARKET_CAP, request, KisFhpst01740000MarketCapResponse.class);
    }

    public KisFhpst01870000NearNewHighLowResponse inquireNearNewHighLow(KisFhpst01870000NearNewHighLowRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01870000_NEAR_NEW_HIGH_LOW, request, KisFhpst01870000NearNewHighLowResponse.class);
    }

    public KisFhpst04820000ShortSaleRankResponse inquireShortSaleRank(KisFhpst04820000ShortSaleRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST04820000_SHORT_SALE_RANK, request, KisFhpst04820000ShortSaleRankResponse.class);
    }

    public KisFhkst17010000CreditBalanceRankResponse inquireCreditBalanceRank(KisFhkst17010000CreditBalanceRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHKST17010000_CREDIT_BALANCE_RANK, request, KisFhkst17010000CreditBalanceRankResponse.class);
    }

    public KisFhpst01750000FinanceRatioRankResponse inquireFinanceRatioRank(KisFhpst01750000FinanceRatioRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01750000_FINANCE_RATIO_RANK, request, KisFhpst01750000FinanceRatioRankResponse.class);
    }

    public KisFhpst01790000MarketValueRankResponse inquireMarketValueRank(KisFhpst01790000MarketValueRankRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST01790000_MARKET_VALUE_RANK, request, KisFhpst01790000MarketValueRankResponse.class);
    }

    public KisFhpst02350000OvertimeVolumeResponse inquireOvertimeVolume(KisFhpst02350000OvertimeVolumeRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST02350000_OVERTIME_VOLUME, request, KisFhpst02350000OvertimeVolumeResponse.class);
    }

    public KisFhpst02340000OvertimeFluctuationResponse inquireOvertimeFluctuation(KisFhpst02340000OvertimeFluctuationRequest request) {
        return kisRestApiClient.get(KisEndpoint.FHPST02340000_OVERTIME_FLUCTUATION, request, KisFhpst02340000OvertimeFluctuationResponse.class);
    }
}
