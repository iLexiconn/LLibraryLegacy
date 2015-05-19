package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiLLibraryMainMenu extends GuiOverride
{
	private GuiButtonCheckForUpdates buttonCheckForUpdates;
	
	public void initGui()
	{
		super.initGui();
		buttonList.add(buttonCheckForUpdates = new GuiButtonCheckForUpdates(85, width / 2 - 124, height / 4 + 48));
	}
	
	public void updateScreen()
	{
		buttonCheckForUpdates.update();
		buttonCheckForUpdates.screenWidth = width;
		buttonCheckForUpdates.screenHeight = height;
	}
	
	public void actionPerformed(GuiButton button)
	{
		int id = button.id;
		
		if (id == 85)
		{
			mc.displayGuiScreen(new GuiCheckForUpdates());
		}
	}
}