가package daon.be.agent.tool.model;

import java.util.Map;

public record ToolResult(
        String id,
//        String targetId,  // 어떤 agent.analysisTarget 을 처리하기위해 요청되었는지

        String toolName,    // 호출한 tool 이름
        String purpose,     // 해당 도구를 호출한 목적

        String stockRef,    // 분석할 종목
        String timeRef,     // 분석 기준 시각

        Map<String, Object> data,
        String evidenceText,        // purpose 대로 data를 분석/요약한 결과 - 최종답변 근거로 사용

        String errorMessage,
        String executedAt    // tool 호출 시각

) {
}
