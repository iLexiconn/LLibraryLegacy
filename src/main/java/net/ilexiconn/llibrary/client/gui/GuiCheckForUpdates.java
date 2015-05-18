package net.ilexiconn.llibrary.client.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.util.List;

import net.ilexiconn.llibrary.common.command.CommandLLibrary;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.ilexiconn.llibrary.common.update.UpdateHelper;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class GuiCheckForUpdates extends GuiScreen
{
	public List<ModUpdateContainer> outdatedMods;
	private GuiSlotModUpdateContainerList modList;
	private int selectedIndex;
    private int listWidth;
    
	private int loadingTimer;
	private boolean failed = false;
	
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 38, I18n.format("gui.done")));
		
		if (loadingTimer >= 50)
		{
			for (ModUpdateContainer mod : outdatedMods)
	    	{
				int i = 20 + 32;
	            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.name) + i);
	            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.modid) + i);
	            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.version) + i);
	        }
	    	
	        listWidth = Math.min(listWidth, 200);
	        modList = new GuiSlotModUpdateContainerList(this, listWidth);
			modList.registerScrollButtons(buttonList, 7, 8);
			buttonList.add(new GuiButton(1, 20, height - 38, listWidth, 20, I18n.format("gui.llibrary.updatecheck.update")));
		}
	}
	
	protected void actionPerformed(GuiButton button)
	{
		int id = button.id;
		
		if (id == 0)
		{
			mc.displayGuiScreen(new GuiMainMenu());
		}
		else if (id == 1)
		{
			if (selectedIndex < outdatedMods.size())
			{
				ModUpdateContainer mod = outdatedMods.get(selectedIndex);
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

	            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
	            {
	                try
	                {
	                    desktop.browse(mod.website.toURI());
	                }
	                catch (Exception e)
	                {
	                    e.printStackTrace();
	                }
	            }
			}
		}
	}
	
	public void updateScreen()
	{		
		++loadingTimer;
		
		if (loadingTimer == 50)
		{
			try
			{
				outdatedMods = VersionHandler.searchForOutdatedModsInefficiently();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				outdatedMods = Lists.newArrayList();
				failed = true;
			}
//			outdatedMods = Lists.newArrayList();
//			
//			for (int i = 0; i < 10; ++i)
//			{
//				outdatedMods.add(UpdateHelper.getModContainerById("llibrary"));
//			}
			
			initGui();
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		int i = width / 2;
		int j = height / 2;
		
		if (modList != null)
		{
			modList.drawScreen(mouseX, mouseY, partialTicks);
		}
		

		
		if (outdatedMods == null)
		{
			int k = (loadingTimer % 50) / 12;
			String s = (k > 0 ? "." : "") + " " + (k > 1 ? "." : "") + " " + (k > 2 ? "." : "");
			drawScaledString(I18n.format("gui.llibrary.updatecheck.search") + " " + s, i, j - 20, 0xffffff, 1.5F);
		}
		else if (outdatedMods.isEmpty())
		{
			if (failed)
			{
				drawCenteredString(fontRendererObj, EnumChatFormatting.RED + I18n.format("gui.llibrary.updatecheck.fail"), i, j - 30, 0xffffff);
			}
			else
			{
				drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line1"), i, j - 40, 0xffffff, 1.5F);
				drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line2"), i, j - 20, 0xffffff, 1.0F);
			}
		}
		else
		{
			if (selectedIndex < outdatedMods.size())
			{
				ModUpdateContainer mod = outdatedMods.get(selectedIndex);
				String[] changelog = null;
				
		        try
		        {
		            changelog = ChangelogHandler.getChangelog(mod, mod.latestVersion);
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
		        
		        int k = Math.max(modList.getLeft() + listWidth + 20 - width / 2 + 201, 1);
		        int l = modList.getTop() - height / 2 + 110;
		        GuiChangelog.drawChangelog(this, getFontRenderer(), changelog, k, l, mod.latestVersion, mod);
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void drawScaledString(String text, int x, int y, int color, float scale)
	{
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);
		drawCenteredString(fontRendererObj, text, (int)(x / scale), (int)(y / scale), color);
		GL11.glPopMatrix();
	}
	
	public void selectItemIndex(int var1)
	{
		selectedIndex = var1;
	}

	public boolean itemIndexSelected(int var1)
	{
		return selectedIndex == var1;
	}

	public FontRenderer getFontRenderer()
	{
		return fontRendererObj;
	}

	public Minecraft getMinecraftInstance()
	{
		return Minecraft.getMinecraft();
	}
}