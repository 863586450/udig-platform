package x;

import net.refractions.udig.project.ui.internal.tool.display.ToolCategory;
import net.refractions.udig.project.ui.internal.tool.display.ToolManager;
import net.refractions.udig.project.ui.internal.tool.display.ToolProxy;
import net.refractions.udig.project.ui.tool.IToolManager;

public class CustomToolManager extends ToolManager implements IToolManager {
	@Override
	protected boolean filterTool(String categoryId, ToolProxy proxy,
			Class<? extends ToolCategory> categoryType) {
		boolean defaultInfoTool = categoryId != null && proxy.getId() != null 
			&& categoryId.equals("net.refractions.udig.tool.category.info") 
			&& proxy.getId().startsWith("net.refractions.udig");
		
		// if default info tool then exclude tool from tool manager
		return defaultInfoTool;
	}
}