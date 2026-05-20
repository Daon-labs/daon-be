package daon.be.agent.service;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import daon.be.agent.planner.model.AgentPlanningRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentChatService {

    private final OpenAiChatModel chatModel;

    public AgentChatResponse chatWithoutPlanning(AgentChatRequest agentChatRequest) {

        // Message
        SystemMessage systemMessage = new SystemMessage("""
                You are a helpful AI assistant.
                Always answer in Korean.
                """);

        UserMessage userMessage = new UserMessage(agentChatRequest.message());

        // Prompt
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // Request/Response
        ChatResponse chatResponse = chatModel.call(prompt);
        String chatResponseText = null;

        if (chatResponse != null
                && chatResponse.getResult() != null
                && chatResponse.getResult().getOutput() != null) {
            chatResponseText = chatResponse.getResult().getOutput().getText();
        }
        if (chatResponseText == null || chatResponseText.isBlank()) {
            throw new IllegalStateException("AI 응답 텍스트가 비어 있습니다.");
        }


        log.info("AI 응답: {}", chatResponseText);

        return new AgentChatResponse(chatResponseText);
    }

    /**
     * <p>사용자 요청을 AgentChatRequest 형식으로 받음
     * <p>AgentChatRequest 로 변환된 사용자 요청을 AgentPlanningRequest 으로 변환하여 Planner에게 전달
     * <p>최종 LLM 응답을 AgentChatResponse 형식으로 반환
     */
    public AgentChatResponse chat(AgentChatRequest chatRequest){
        AgentPlanningRequest planningRequest = AgentPlanningRequest
                .builder()
                .message(chatRequest.message())
                .userId(chatRequest.userId())
                .requestedAt(chatRequest.requestedAt())
                .build();

        return new AgentChatResponse(null);
    }

}
