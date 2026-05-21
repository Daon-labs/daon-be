package daon.be.agent.planner.model.response;

/**
 * <p>사용자 질문에 대한 응답 지원 가능 여부 판단</p>
 * <p>ScopeAssessment status 필드에서 사용</p>
 *
 *  <pre><code>
 *  "scope": {
 *     "status": "SUPPORTED",
 *     "reason": null
 *   },
 *   </code></pre>
 *
 */
public enum ScopeStatus {
    SUPPORTED,
    UNSUPPORTED
}
