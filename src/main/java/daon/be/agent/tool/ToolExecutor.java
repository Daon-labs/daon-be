package daon.be.agent.tool;

import daon.be.agent.tool.model.ToolExecutorContext;
import daon.be.agent.tool.model.ChatResponse;

public interface ToolExecutor {
    ChatResponse execute(ToolExecutorContext context);
}
