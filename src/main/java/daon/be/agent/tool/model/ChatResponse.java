package daon.be.agent.tool.model;

import lombok.Builder;

@Builder
public record ChatResponse(

        String response        // purpose 대로 data를 분석/요약한 결과 - 최종답변 근거로 사용
) {
}
