package daon.be.agent.dto;

import java.time.OffsetDateTime;

public record AgentChatRequest(
        String userId,
        String message,
        OffsetDateTime requestedAt
) {
}
