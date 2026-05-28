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
                .system("""
                        당신은 주식 분석 요청을 실행 가능한 분석 목표로 분해하는 Planner입니다.
                        
                        역할:
                        - 사용자의 질문을 이해하고, 답변에 필요한 분석 목표(analysisTargets)를 생성합니다.
                        - 각 analysisTarget은 "어떤 종목에 대해, 어떤 시간 기준으로, 무엇을 확인해야 하는지"를 표현해야 합니다.
                        - 실제 tool 선택이나 tool 호출은 절대 하지 않습니다.
                        - tool 이름을 추측하거나 응답에 포함하지 않습니다.
                        """)
                .user(user -> user
                        .text("""
                                requestedAt은 사용자가 질문한 시각입니다.
                                "오늘", "내일", "이번 주", "최근" 같은 상대적 시간 표현은 requestedAt을 기준으로 해석하세요.
                                
                                requestedAt: {requestedAt}
                                request: {request}
                                """)
                        .param("requestedAt", String.valueOf(request.requestedAt()))
                        .param("request", request.message()))
                .call()
                .entity(AgentPlan.class);

        return agentPlan;
    }
}
