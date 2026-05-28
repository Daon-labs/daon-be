package daon.be.agent.evidence;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.tool.model.ChatResponse;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record EvidencePacket(
        AgentPlan agentPlan,
        List<ChatResponse> chatResponses,
        int iteration,
        OffsetDateTime createdAt
) {
    public static EvidencePacket of(
            AgentPlan agentPlan,
            List<ChatResponse> chatResponses,
            int iteration
    ) {
        return new EvidencePacket(
                agentPlan,
                chatResponses,
                iteration,
                OffsetDateTime.now(ZoneId.of("Asia/Seoul"))
        );
    }
}
