package daon.be.agent.planner;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AgentPlanningRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class LlmAgentPlanner implements AgentPlanner {

    @Override
    public AgentPlan plan(AgentPlanningRequest request) {
        return null;
    }
}
