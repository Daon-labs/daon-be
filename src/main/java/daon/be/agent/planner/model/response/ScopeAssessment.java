package daon.be.agent.planner.model.response;

/**
 * <p>사용자 질문에 대한 응답 지원 가능 여부와 이유 판단</p>
 *
 *  <pre><code>
 *  "scope": {
 *     "status": "SUPPORTED",
 *     "reason": null
 *   },
 *   </code></pre>
 *
 */
public record ScopeAssessment(
        ScopeStatus status,
        String reason
) {
}
