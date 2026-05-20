package daon.be.agent.tool.model;

/**
 * 주식 분석 tool 목록
 */
public enum AnalysisToolType {
    GET_CURRENT_MARKET_SNAPSHOT,    // 현재가, 거래량, 종목상태
    GET_INTRADAY_PRICE_FLOW,        // 당일/특정일 분봉
    GET_HISTORICAL_PRICE_TREND,     // 일/주/월/년봉, 장기추세
    GET_MARKET_AND_INDUSTRY_CONTENT, // 시장/업종 지수, 업종 분봉/일봉
    GET_EVENT_CANDIDATES,           // VI, 뉴스/공시 제공, 이벤트 후보
    GET_SUPPLY_DEMAND_CONTENT,      // 외국인/기관/프로그램/투자자 수급
    GET_FUNDAMENTAL_CONTEXT,        // 재무비율, 재무제표, 투자의견, 추정실적
    COMPARE_STOCKS                  // 복수 종목 비교
}
