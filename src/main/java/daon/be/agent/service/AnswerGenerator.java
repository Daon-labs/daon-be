package daon.be.agent.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.evidence.EvidencePacket;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnswerGenerator {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public String generate(EvidencePacket evidencePacket) {
        String evidencePacketJson = toJson(evidencePacket);

        return chatClient.prompt()
                .system("""
                        당신은 주식 분석 답변을 생성하는 AI입니다.
                        항상 한국어로 답변하세요.
                        제공된 EvidencePacket에 있는 근거만 사용하세요.
                        근거가 부족하면 부족하다고 말하세요.
                        투자 조언처럼 단정하지 말고, 관찰 가능한 데이터 중심으로 설명하세요.
                        """)
                .user(user -> user
                        .text("""
                                다음 EvidencePacket을 바탕으로 사용자에게 최종 답변을 작성하세요.
                                
                                EvidencePacket:
                                {evidencePacket}
                                """)
                        .param("evidencePacket", evidencePacketJson)
                )
                .call()
                .content();
    }

    private String toJson(EvidencePacket evidencePacket) {
        try {
            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(evidencePacket);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("EvidencePacket 직렬화 실패", e);
        }
    }
}
