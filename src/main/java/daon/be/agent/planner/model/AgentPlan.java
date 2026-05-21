package daon.be.agent.planner.model;

import daon.be.agent.planner.model.response.AnalysisFocus;
import daon.be.agent.planner.model.response.DomainToolRequest;
import daon.be.agent.planner.model.response.ScopeAssessment;
import daon.be.agent.planner.model.response.StockMention;

import java.util.List;

/**
 * Planner의 전체 출력 결과
 *
 *  <pre><code>
 *  {
 *   "analysisFocus": [
 *     "PRICE_MOVE_REASON",
 *     "INTRADAY_FLOW",
 *     "EVENT_CANDIDATE_CHECK"
 *   ],
 *   "stockMentions": [
 *     {
 *       "rawText": "삼성전자",
 *       "kind": "NAME"
 *     }
 *   ],
 *   "scope": {
 *     "status": "SUPPORTED",
 *     "reason": null
 *   },
 *   "toolRequests": [
 *     {
 *       "tool": "GET_INTRADAY_PRICE_FLOW",
 *       "target": {
 *         "stockRef": "삼성전자",
 *         "timeRef": "오늘 10시쯤",
 *         "stockRefs": []
 *       },
 *       "reason": "10시 전후 가격 하락 구간과 거래량 변화를 확인해야 합니다.",
 *       "priority": "HIGH"
 *     },
 *     {
 *       "tool": "GET_CURRENT_MARKET_SNAPSHOT",
 *       "target": {
 *         "stockRef": "삼성전자",
 *         "timeRef": "지금",
 *         "stockRefs": []
 *       },
 *       "reason": "현재 하락 상태와 거래량/거래대금 수준을 확인해야 합니다.",
 *       "priority": "MEDIUM"
 *     },
 *     {
 *       "tool": "GET_EVENT_CANDIDATES",
 *       "target": {
 *         "stockRef": "삼성전자",
 *         "timeRef": "오늘 10시쯤",
 *         "stockRefs": []
 *       },
 *       "reason": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *       "priority": "MEDIUM"
 *     }
 *   ],
 *   "needsClarification": false,
 *   "clarificationQuestion": null,
 *   "confidence": 0.88
 * }
 * </code></pre>
 */
public record AgentPlan(
    List<AnalysisFocus> analysisFocus,
    List<StockMention> stockMentions,
    ScopeAssessment scope,
    List<DomainToolRequest> toolRequests,
    boolean needsClarification,
    String clarificationQuestion,
    double confidence
) {}
