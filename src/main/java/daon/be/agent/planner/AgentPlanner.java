package daon.be.agent.planner;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AgentPlanningRequest;

public interface AgentPlanner {
    AgentPlan plan(AgentPlanningRequest request);
}
