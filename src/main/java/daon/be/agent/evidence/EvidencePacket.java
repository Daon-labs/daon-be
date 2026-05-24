package daon.be.agent.evidence;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.tool.model.ToolResult;

import java.time.OffsetDateTime;
import java.util.List;

public record EvidencePacket(
        AgentPlan agentPlan,
        List<ToolResult> toolResults,
        int iteration,
        OffsetDateTime createdAt
) {
}
