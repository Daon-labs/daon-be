package daon.be.agent.tool.stock;

import daon.be.agent.tool.stock.dto.StockNowContextDto;
import daon.be.agent.tool.stock.dto.IntradayMoveContextDto;
import daon.be.agent.tool.stock.dto.EventTimelineContextDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KISTools {

    private final KisStockNowContextService stockNowContextService;
    private final KisIntradayMoveContextService intradayMoveContextService;
    private final KisEventTimelineContextService eventTimelineContextService;

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
}
