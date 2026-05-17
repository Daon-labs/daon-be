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

        if (request == null
                || request.userRequest() == null
                || request.userRequest().user() == null
                || request.userRequest().utterance() == null
                || request.userRequest().user().id() == null) {
            log.warn("Invalid Kakao webhook request. reason=missing_required_field");
            return KakaoSkillResponse.simpleText("요청 형식이 올바르지 않습니다.");
        }

        String message = request.userRequest().utterance().trim();
        String externalUserId = request.userRequest().user().id();

        if (message.isBlank()) {
            return KakaoSkillResponse.simpleText("질문을 입력해 주세요.");
        }

        if (externalUserId.isBlank()) {
            log.warn("Invalid Kakao webhook request. reason=blank_user_id");
            return KakaoSkillResponse.simpleText("요청 형식이 올바르지 않습니다.");
        }

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

        if (agentResponse == null || agentResponse.answer() == null || agentResponse.answer().isBlank()) {
            log.error("Empty agent response. externalUserId={}, usermessage={}", externalUserId, message);
            return KakaoSkillResponse.simpleText("답변 생성 중 에러가 발생했습니다.");
        }

        log.info(
                "Kakao webhook responded. externalUserId={}, agentResponse={}",
                externalUserId,
                agentResponse.answer()
        );

        return KakaoSkillResponse.simpleText(agentResponse.answer());
    }
}
