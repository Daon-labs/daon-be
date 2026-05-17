package daon.be.agent.service;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import org.springframework.stereotype.Service;

@Service
public class AgentChatService {

    public AgentChatResponse chat(AgentChatRequest agentChatRequest) {
        return new AgentChatResponse("Agent 기능 미구현 상태");
    }
}
