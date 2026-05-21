package daon.be.agent.planner;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AgentPlanningRequest;
import org.springframework.stereotype.Component;

@Component
public class SimpleAgentPlanner implements AgentPlanner {

    @Override
    public AgentPlan plan(AgentPlanningRequest request) {
        return null;
    }
}
