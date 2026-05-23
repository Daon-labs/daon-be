package daon.be.agent.planner.model.response;

import java.util.List;

/**
 * <p>tool 을 요청하는 대상 종목</p>
 *
 *  <pre><code>
 *       "analysisTargets": {
 *         "stockRefs": ["삼성전자", "SK하이닉스"],
 *         "timeRef": "오늘",
 *         "reason": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *       }
 *   </code></pre>
 *
 */
public record AnalysisTarget(
        List<String> stockRefs,
        String timeRef,
        String reason
) {
}
