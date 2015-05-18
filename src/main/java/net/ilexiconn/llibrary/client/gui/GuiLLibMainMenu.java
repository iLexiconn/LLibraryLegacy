package net.ilexiconn.llibrary.client.gui;

import java.util.List;

import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.minecraft.client.gui.GuiButton;

import com.google.common.collect.Lists;

public class GuiLLibMainMenu extends GuiOverride
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