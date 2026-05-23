package daon.be.agent.planner.model;

import java.util.List;

/**
 * Planner의 전체 출력 결과
 *
 *  <pre><code>
 * {
 *   "isSupported": false,
 *   "unsupportedReason": false,
 *   "needsClarification": false,
 *   "clarificationQuestion": null,
 *    "analysisTargets": [
 *    {
 *      "stockRefs": ["삼성전자", "SK하이닉스"],
 *      "timeRef": "오늘",
 *      "objective": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *    },
 *    {
 *      "stockRefs": ["현대자동차"],
 *      "timeRef": "오늘",
 *      "objective": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *    }
 *   ]
 * }
 * </code></pre>
 */
public record AgentPlan(
    boolean isSupported,
    String unsupportedReason,
    boolean needsClarification,
    String clarificationQuestion,
    List<AnalysisTarget> analysisTargets
) {}
