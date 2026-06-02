package daon.be.agent.tool.stock;

import daon.be.agent.tool.stock.dto.StockNowContextDto;
import daon.be.agent.tool.stock.dto.IntradayMoveContextDto;
import daon.be.agent.tool.stock.dto.EventTimelineContextDto;
import daon.be.agent.tool.stock.dto.MarketIndustryContextDto;
import daon.be.agent.tool.stock.dto.PriceTrendContextDto;
import daon.be.agent.tool.stock.dto.SupplyDemandContextDto;
import daon.be.agent.tool.stock.dto.FundamentalContextDto;
import daon.be.agent.tool.stock.dto.CompareStocksContextDto;
import daon.be.agent.tool.stock.dto.MarketMoverCandidatesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KISTools {

    private final KisStockNowContextService stockNowContextService;
    private final KisIntradayMoveContextService intradayMoveContextService;
    private final KisEventTimelineContextService eventTimelineContextService;
    private final KisMarketIndustryContextService marketIndustryContextService;
    private final KisPriceTrendContextService priceTrendContextService;
    private final KisSupplyDemandContextService supplyDemandContextService;
    private final KisFundamentalContextService fundamentalContextService;
    private final KisCompareStocksContextService compareStocksContextService;
    private final KisMarketMoverCandidatesService marketMoverCandidatesService;

    @Tool(description = "국내주식 단일 종목의 현재가, 거래량, 당일 가격 범위, 호가 잔량, 거래 상태, 기본 밸류에이션 스냅샷을 조회합니다. KIS raw API명이나 원문 필드는 노출하지 않고 분석용 정규화 DTO를 반환합니다.")
    public StockNowContextDto getStockNowContext(String stockCode) {
        return stockNowContextService.getStockNowContext(stockCode);
    }

    @Tool(description = "국내주식 단일 종목의 당일 또는 특정 거래일 장중 분봉 흐름, 조회 구간 변곡점, 당일 시간대별 체결 요약을 조회합니다. tradingDate가 비어 있으면 당일 분봉과 당일시간대별체결을 함께 조회하고, YYYYMMDD 또는 YYYY-MM-DD가 있으면 과거 일별분봉을 조회합니다.")
    public IntradayMoveContextDto getIntradayMoveContext(String stockCode, String tradingDate, String inputHour) {
        return intradayMoveContextService.getIntradayMoveContext(stockCode, tradingDate, inputHour);
    }

    @Tool(description = "국내주식 단일 종목의 VI 발동 이력, 종목 관련 뉴스/공시 제목, 상하한가 포착 여부를 조회합니다. 뉴스/공시는 원인 확정 근거가 아니라 동시간대 이벤트 후보로만 해석해야 합니다.")
    public EventTimelineContextDto getEventTimelineContext(String stockCode, String tradingDate, String inputHour) {
        return eventTimelineContextService.getEventTimelineContext(stockCode, tradingDate, inputHour);
    }

    @Tool(description = "국내주식 시장 또는 업종의 현재 지수, 업종 분봉, 업종 일봉, 시장별 투자자 장중/일별 수급을 조회합니다. 개별 종목 움직임이 시장/업종 동조인지 판단할 보조 근거로 사용합니다.")
    public MarketIndustryContextDto getMarketIndustryContext(String marketCode, String industryCode, String inputHour, String startDate) {
        return marketIndustryContextService.getMarketIndustryContext(marketCode, industryCode, inputHour, startDate);
    }

    @Tool(description = "국내주식 단일 종목의 일/주/월/년 단위 기간별 가격 추세, 조회 구간 수익률, 고저점, 52주 고저점 대비 현재 위치를 조회합니다. 장중 원인 분석보다 중장기 가격 맥락 확인에 사용합니다.")
    public PriceTrendContextDto getPriceTrendContext(String stockCode, String startDate, String endDate, String periodType) {
        return priceTrendContextService.getPriceTrendContext(stockCode, startDate, endDate, periodType);
    }

    @Tool(description = "국내주식 단일 종목의 종목별 일별 확정 수급, 장중 외인기관 가집계, 국내기관/외국인 매매종목 가집계, 시장별 투자자 일별 수급을 조회합니다. 추정 수급과 확정 수급은 분리해서 해석해야 합니다.")
    public SupplyDemandContextDto getSupplyDemandContext(String stockCode, String startDate, String marketIndexCode) {
        return supplyDemandContextService.getSupplyDemandContext(stockCode, startDate, marketIndexCode);
    }

    @Tool(description = "국내주식 단일 종목의 종목 기본정보, 재무비율, 손익계산서, 대차대조표, 안정성비율을 조회합니다. 장기 투자 관점의 재무 체력과 데이터 품질 주의사항을 함께 반환합니다.")
    public FundamentalContextDto getFundamentalContext(String stockCode) {
        return fundamentalContextService.getFundamentalContext(stockCode);
    }

    @Tool(description = "국내주식 복수 종목을 동일 기준으로 비교합니다. focus는 PRICE, FUNDAMENTAL, SUPPLY_DEMAND, PRICE_FUNDAMENTAL, ALL 중 하나로 지정하며, 선택된 하위 도메인 툴만 fan-out 해 병합합니다.")
    public CompareStocksContextDto getCompareStocksContext(String stockCodes, String startDate, String endDate, String focus) {
        return compareStocksContextService.getCompareStocksContext(stockCodes, startDate, endDate, focus);
    }

    @Tool(description = "국내주식 시장에서 거래량, 등락률, 체결강도, 대량체결, HTS 조회상위, 신고/신저 근접, 상하한가 포착 순위에 나타난 이상 흐름 후보를 통합 조회합니다. 결과는 원인 확정이 아니라 상세 분석 대상 후보입니다.")
    public MarketMoverCandidatesDto getMarketMoverCandidates(String market) {
        return marketMoverCandidatesService.getMarketMoverCandidates(market);
    }
}
