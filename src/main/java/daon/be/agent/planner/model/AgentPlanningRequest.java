package daon.be.agent.planner.model;

import lombok.Builder;

import java.time.OffsetDateTime;

/**
 * Planning 담당 LLM 입력으로 넘길 양식
 */
@Builder
public record AgentPlanningRequest(
        String userId,
        String message,
        OffsetDateTime requestedAt
){}
