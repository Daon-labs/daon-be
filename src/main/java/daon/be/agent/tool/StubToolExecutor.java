package daon.be.agent.tool;

import daon.be.agent.planner.model.AnalysisTarget;
import daon.be.agent.tool.model.ToolExecutorContext;
import daon.be.agent.tool.model.ToolResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Primary
@Slf4j
@Component
public class StubToolExecutor implements ToolExecutor {

    @Override
    public List<ToolResult> execute(ToolExecutorContext context) {
        AnalysisTarget target = context.analysisTarget();

        log.info(
                "Stub tool execution. targetIndex={}, objective={}, stockRefs={}",
                context.analysisTargetIndex(),
                target.objective(),
                target.stockRefs()
        );

        return target.stockRefs()
                .stream()
                .map(stockRef -> createStubResult(context, target, stockRef))
                .toList();
    }

    private ToolResult createStubResult(
            ToolExecutorContext context,
            AnalysisTarget target,
            String stockRef
    ) {
        return new ToolResult(
                "1",
                "stubTool",
                stockRef,
                target.timeRef(),
                target.objective(),
                Map.of(
                        "stockRef", stockRef,
                        "timeRef", target.timeRef(),
                        "objective", target.objective()
                ),
                """
                [%s]에 대한 임시 tool 결과입니다.
                분석 목표: %s
                필요한 근거: %s
                """.formatted(
                        stockRef,
                        target.objective(),
                        target.timeRef()
                ),
                null,
                OffsetDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );
    }
}
