package daon.be.agent.planner.model.response;

/**
 * 사용자가 언급한 종목
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
public record StockMention(
        String rawText,
        StockMentionKind kind
) {
}
