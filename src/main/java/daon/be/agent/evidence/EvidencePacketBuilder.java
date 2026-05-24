package daon.be.agent.evidence;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.tool.model.ToolResult;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class EvidencePacketBuilder {
    public EvidencePacket build(
            AgentPlan agentPlan,
            List<ToolResult> toolResults,
            int iteration
    ) {
        return new EvidencePacket(
                agentPlan,
                toolResults,
                iteration,
                OffsetDateTime.now(ZoneId.of("Asia/Seoul"))
        );
    }
}
