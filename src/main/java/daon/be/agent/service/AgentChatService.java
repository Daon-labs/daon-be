package daon.be.agent.service;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import daon.be.agent.evidence.EvidencePacket;
import daon.be.agent.planner.AgentPlanner;
import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AgentPlanningRequest;
import daon.be.agent.planner.model.AnalysisTarget;
import daon.be.agent.tool.ToolExecutor;
import daon.be.agent.tool.model.ToolExecutorContext;
import daon.be.agent.tool.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentChatService {

    private final ChatClient chatClient;
    private final AgentPlanner agentPlanner;
    private final ToolExecutor toolExecutor;
    private final AnswerGenerator answerGenerator;

    public AgentChatResponse chatWithoutPlanning(AgentChatRequest agentChatRequest) {

        String chatResponseText = chatClient.prompt()
                .system("You are a helpful AI assistant. Always answer in Korean.")
                .user(agentChatRequest.message())
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();

        log.info("AI 응답: {}", chatResponseText);

        return new AgentChatResponse(chatResponseText);
    }

    /**
     * <p>사용자 요청을 AgentChatRequest 형식으로 받음
     * <p>AgentChatRequest 로 변환된 사용자 요청을 AgentPlanningRequest 으로 변환하여 Planner에게 전달
     * <p>최종 LLM 응답을 AgentChatResponse 형식으로 반환
     */
    public AgentChatResponse chat(AgentChatRequest chatRequest){

        // 사용자 정보와 message를 담은 chatRequest를 planner 입력 형식(AgentPlannerRequest)으로 변화
        AgentPlanningRequest planningRequest = AgentPlanningRequest
                .builder()
                .message(chatRequest.message())
                .userId(chatRequest.userId())
                .requestedAt(chatRequest.requestedAt())
                .build();

        // Planner 호출
        AgentPlan agentPlan = agentPlanner.plan(planningRequest);

        log.info("agentPlan: {}", agentPlan);

        // isSupported == false 라면 사용자에게 unsupportedReasons 반환
        if(!agentPlan.isSupported()){
            return new AgentChatResponse(agentPlan.unsupportedReason());
        }

        // needsClarification == true 라면 사용자에게 clarificationQuestion 을 반환
        if(agentPlan.needsClarification()){
            return new AgentChatResponse(agentPlan.clarificationQuestion());
        }

        List<ChatResponse> chatResponses = new ArrayList<>();

        // agentPlan 의 모든 analysisTargets 에 대해 각각 tool 실행
        for (int i = 0; i < agentPlan.analysisTargets().size(); i++) {
            AnalysisTarget target = agentPlan.analysisTargets().get(i);

            ToolExecutorContext context = ToolExecutorContext.builder()
                    .agentPlan(agentPlan)
                    .analysisTarget(target)
                    .analysisTargetIndex(i)
                    .previousResults(chatResponses)
                    .iteration(0)
                    .build();

            chatResponses.add(toolExecutor.execute(context));
        }

        EvidencePacket evidencePacket = EvidencePacket.of(agentPlan, chatResponses, 0);

        log.info("evidencePacket: {}", evidencePacket);

        String chatResponseText = answerGenerator.generate(evidencePacket);

        return new AgentChatResponse(chatResponseText);
    }

}
