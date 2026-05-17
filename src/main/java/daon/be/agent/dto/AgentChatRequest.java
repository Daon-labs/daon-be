package daon.be.agent.dto;

import java.time.OffsetDateTime;

public record AgentChatRequest(
        String externalUserId,
        String message,
        OffsetDateTime requestedAt
) {
}
