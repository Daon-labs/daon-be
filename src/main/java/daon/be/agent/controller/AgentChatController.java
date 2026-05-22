package daon.be.agent.controller;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import daon.be.agent.service.AgentChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class AgentChatController {

    private final AgentChatService agentChatService;

    @PostMapping("/api/agent/chatWithoutPlanning")
    public AgentChatResponse chatWithoutPlanningRequest(@RequestBody String message){
        AgentChatRequest agentChatRequest = new AgentChatRequest(
                "test",
                message,
                OffsetDateTime.now(ZoneId.of("Asia/Seoul"))
        );

        return agentChatService.chatWithoutPlanning(agentChatRequest);
    }

    @PostMapping("/api/agent/chat")
    public AgentChatResponse chatRequest(@RequestBody String message){
        AgentChatRequest agentChatRequest = new AgentChatRequest(
                "test",
                message,
                OffsetDateTime.now(ZoneId.of("Asia/Seoul"))
        );

        return agentChatService.chat(agentChatRequest);
    }

}
