package daon.be.agent.planner.model.response;

/**
 * 사용자 질문의 핵심 의도 분류
 *
 *  <pre><code>
 * "analysisFocus": [
 *     "PRICE_MOVE_REASON",
 *     "INTRADAY_FLOW",
 *     "EVENT_CANDIDATE_CHECK"
 *   ]
 *   </code></pre>
 *
 */
public enum AnalysisFocus {
    PRICE_MOVE_REASON,
    INTRADAY_FLOW,
    HISTORICAL_TREND,
    MARKET_AND_INDUSTRY_CONTEXT,
    EVENT_CANDIDATE_CHECK,
    SUPPLY_DEMAND,
    FUNDAMENTAL,
    STOCK_COMPARISON,
    GENERAL_ANALYSIS
}
