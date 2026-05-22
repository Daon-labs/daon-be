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

        String userPrompt = """
                requestedAt: %s
                request: %s
                
                requestedAt은 사용자가 질문한 시각입니다.
                "오늘", "내일", "이번 주", "최근" 같은 상대적 시간 표현은 requestedAt을 기준으로 해석하세요.
                """.formatted(
                        request.requestedAt(),
                        request.message()
        );

        // Request/Response in Structured Output
        AgentPlan agentPlan = chatClient.prompt()
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .user(userPrompt)
                .call()
                .entity(AgentPlan.class);

        return agentPlan;
    }
}
