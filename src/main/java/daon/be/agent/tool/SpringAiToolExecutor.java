package daon.be.agent.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import daon.be.agent.planner.model.AgentPlan;
import daon.be.agent.planner.model.AnalysisTarget;
import daon.be.agent.tool.model.ToolExecutorContext;
import daon.be.agent.tool.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringAiToolExecutor implements ToolExecutor {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public ChatResponse execute(ToolExecutorContext context) {

        AgentPlan plan = context.agentPlan();
        AnalysisTarget target = context.analysisTarget();
        int analysisTargetIndex = context.analysisTargetIndex();
        List<ChatResponse> previousChatResponses = context.previousResults();

        // 현재 분석해야 할 타겟 정보 추출
        String currentTargetJson;

        try {
            currentTargetJson = objectMapper.writeValueAsString(Map.of(
                    "종목", target.stockRef(),
                    "시점", target.timeRef(),
                    "분석목표", target.objective()
            ));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("현재 분석 타겟 직렬화에 실패했습니다.", e);
        }

        // 이전 분석 결과들을 하나의 텍스트로 취합
        String previousResultsContext = previousChatResponses.stream()
                .map(ChatResponse ::response)
                .collect(Collectors.joining("\n\n--- 이전 분석 결과 ---\n\n"));


        // ChatClient 호출
        int totalTargets = context.agentPlan().analysisTargets().size();
        int currentStep = analysisTargetIndex + 1;
        String prevResult = previousResultsContext.isEmpty() ? "이전 분석 결과가 없습니다." : previousResultsContext;

        String result = chatClient.prompt()
                .system("""
                        당신은 주식 분석 답변을 생성하는 AI입니다.
                        항상 한국어로 답변하세요.
                        투자 조언처럼 단정하지 말고, 관찰 가능한 데이터 중심으로 설명하세요.
                        """)
                .user(user -> user
                        .text("""
                                [전체 분석 맥락]
                                현재 분석은 총 {total}개의 목표 중 {step}번째 진행 중입니다.
                                
                                [이전 단계 분석 결과]
                                {previousResults}
                                
                                [현재 분석 타겟 및 목표]
                                {currentTarget}
                                
                                [수행할 작업]
                                위의 '현재 분석 타겟 및 목표'와 '이전 단계 분석 결과'를 바탕으로, 제공된 데이터를 분석하여 보고서 형태로 답변을 생성해 주세요.
                                절대 주관적인 예측이나 추천을 하지 말고, 데이터에 기반한 사실만 기술하세요.
                                """)
                        .param("total", String.valueOf(totalTargets))
                        .param("step", String.valueOf(currentStep))
                        .param("previousResults", prevResult)
                        .param("currentTarget", currentTargetJson)
                )
                .call()
                .content();

        ChatResponse chatResponse = ChatResponse.builder()
                .response(result)
                .build();

        return chatResponse;
    }
}

