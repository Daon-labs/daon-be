package daon.be.kakao.service;

import daon.be.agent.dto.AgentChatRequest;
import daon.be.agent.dto.AgentChatResponse;
import daon.be.agent.service.AgentChatService;
import daon.be.kakao.dto.KakaoSkillRequest;
import daon.be.kakao.dto.KakaoSkillResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoWebhookService {
    private final AgentChatService agentChatService;

    public KakaoSkillResponse handle(KakaoSkillRequest request) {
        OffsetDateTime requestedAt = OffsetDateTime.now();

        String message = request.userRequest().utterance().trim();

        String externalUserId = request.userRequest().user().id();

        log.info(
                "Kakao webhook received. externalUserId={}, message={}",
                externalUserId,
                message
        );

        AgentChatRequest agentRequest = new AgentChatRequest(
                externalUserId,
                message,
                requestedAt
        );

        AgentChatResponse agentResponse = agentChatService.chat(agentRequest);

        log.info(
                "Kakao webhook responded. externalUserId={}, agentResponse={}",
                externalUserId,
                agentResponse.answer()
        );

        return KakaoSkillResponse.simpleText(agentResponse.answer());
    }
}
