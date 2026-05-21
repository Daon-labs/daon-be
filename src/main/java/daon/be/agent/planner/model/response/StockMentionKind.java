package daon.be.agent.planner.model.response;

/**
 * <p>사용자가 종목을 언급한 방식</p>
 * <p>StockMention의 kind 필드에서 사용</p>
 *
 *  <pre><code>
 * "stockMentions": [
 *     {
 *       "rawText": "삼성전자",
 *       "kind": "NAME"
 *     }
 *   ],
 *   </code></pre>
 *
 */
public enum StockMentionKind {
    NAME,
    CODE,
    ALIAS,
    UNKNOWN
}
