package daon.be.agent.planner.model.response;

import daon.be.agent.tool.model.AnalysisToolType;

/**
 * <p>Planner LLM이 제안하는 tool 호출 후보</p>
 *
 *  <pre><code>
 *  "toolRequests": [
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
 *   </code></pre>
 *
 */
public record DomainToolRequest(
        AnalysisToolType tool,
        DomainToolTarget target,
        String reason,
        ToolPriority priority
) {
}
