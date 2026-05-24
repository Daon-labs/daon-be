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
        List<ToolResult> previousResults,
        String instruction,         // 해당 target 을 달성하기 위한 실행 지시
        int iteration
) {
}
