package daon.be.agent.service;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
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

    public AgentChatResponse chat(AgentChatRequest agentChatRequest) {

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
}
