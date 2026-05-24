package daon.be.agent.planner.model;

import java.util.List;

/**
 * <p>사용자 질문에서 분석 목표 설정</p>
 *
 *  <pre><code>
 *       {
 *         "stockRefs": ["삼성전자", "SK하이닉스"],
 *         "timeRef": "오늘",
 *         "objective": "하락 시점 전후 VI, 뉴스/공시 제목 후보를 확인해야 합니다.",
 *       }
 *   </code></pre>
 *
 */
public record AnalysisTarget(
        List<String> stockRefs,
        String timeRef,
        String objective    // target이 달성해야 하는 목표
) {
}
