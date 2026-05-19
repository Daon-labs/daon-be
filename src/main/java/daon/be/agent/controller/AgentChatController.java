package daon.be.agent.controller;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import daon.be.agent.service.AgentChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class AgentChatController {

    private final AgentChatService agentChatService;

    @GetMapping("/api/agent/chat")
    public AgentChatResponse chatRequest(@RequestBody String message){
        AgentChatRequest agentChatRequest = new AgentChatRequest(
                "test",
                message,
                OffsetDateTime.now()
        );

        return agentChatService.chat(agentChatRequest);
    }
}
