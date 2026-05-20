package daon.be.agent.planner.model.response;

import java.util.List;

/**
 * <p>tool 을 요청하는 대상 종목</p>
 * <p>DomainToolRequest target 필드에서 사용</p>
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
 *       "tool": "COMPARE_STOCKS",
 *       "target": {
 *         "stockRef": null,
 *         "timeRef": "오늘",
 *         "stockRefs": ["삼성전자", "SK하이닉스"]
 *       },
 *       "reason": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *       "priority": "MEDIUM"
 *     }
 *   ],
 *   </code></pre>
 *
 */
public record DomainToolTarget(
        String stockRef,
        String timeRef,
        List<String> stockRefs
) {
}
