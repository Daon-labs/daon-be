package daon.be.agent.planner;

import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AgentPlanningRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Primary
@Component
public class LlmAgentPlanner implements AgentPlanner {

    private final ChatClient chatClient;

    @Override
    public AgentPlan plan(AgentPlanningRequest request) {

        // Request/Response in Structured Output
        AgentPlan agentPlan = chatClient.prompt()
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .user(request.message())
                .call()
                .entity(AgentPlan.class);

        return agentPlan;
    }
}
