package daon.be.agent.tool;

import daon.be.agent.tool.model.ToolExecutorContext;
import daon.be.agent.tool.model.ToolResult;

import java.util.List;

public interface ToolExecutor {
    List<ToolResult> execute(ToolExecutorContext context);
}
