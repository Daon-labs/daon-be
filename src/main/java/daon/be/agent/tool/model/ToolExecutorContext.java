package daon.be.agent.tool.model;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AnalysisTarget;
import lombok.Builder;

import java.util.List;

@Builder
public record ToolExecutorContext(
        AgentPlan agentPlan,
        AnalysisTarget analysisTarget,
        int analysisTargetIndex,    // 추후 target id로 변경
        List<ChatResponse> previousResults,
        int iteration
) {
}
