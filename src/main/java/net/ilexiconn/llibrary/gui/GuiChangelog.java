package net.ilexiconn.llibrary.gui;

import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiChangelog extends GuiScreen
{
	private final ModUpdateContainer mod;
	private final String version;
	private final String[] changelog;
		
	private int verticalScroll = 0;
	private int horizontalScroll = 0;
	
	public GuiChangelog(ModUpdateContainer mod, String version, String[] changelog)
	{
		super();
		this.mod = mod;
		this.version = version;
		this.changelog = changelog;
	}
	
	public void init()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 60, 200, 20, "Done"));
	}
	
	protected void actionPerformed(GuiButton button) 
	{
		switch (button.id)
		{
			//Case Shit: Do Shit FFS!
		}
	}
	
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
	}
	
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	public void onGuiClosed() 
	{
		
	}
	
	public int getMouseWheel()
	{
		int i = Mouse.getDWheel();
		
		if (i != 0)
		{
			if (i > 1)
			{
				i = 1;
			}
			if (i < -1)
			{
				i = -1;
			}
			
			verticalScroll += !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -(i * 10) : 0;
			horizontalScroll += Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -(i * 10) : 0;
			
			i = 0;
		}
		return i;
	}
	
	public void drawScreen(int par1, int par2, float par3)
	{
		this.drawDefaultBackground();
		super.drawScreen(par1, par2, par3);
		int i = verticalScroll;
		int j = horizontalScroll;
		this.getMouseWheel();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0, 0, 0, 0.2F);
        this.drawTexturedModalRect(j + width / 2 - 201, i + height / 2 - 110, 0, 0, 405, 33);
        this.drawTexturedModalRect(j + width / 2 - 201, i + height / 2 - 72, 0, 0, 405, 400);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.drawCenteredString(this.fontRendererObj, "(" + mod.modid + ") " + mod.name + ": " + version, j + width / 2, i + height / 2 - 97, 0xffffff);
		
		for (int k = 0; k < changelog.length; ++k)
		{
			this.drawString(this.fontRendererObj, changelog[k], j + width / 2 - 190, i + height / 2 - 65 + k * 10, 0xffffff);
		}
		
		
		this.drawString(this.fontRendererObj, "Press 'ESC' to exit.", 5, 5, 0xffffff);
		this.drawString(this.fontRendererObj, "Mouse Wheel to scroll up/down.", width - 220, height - 15, 0xffffff);
		this.drawString(this.fontRendererObj, "Mouse Wheel + 'SHIFT' to scroll left/right.", width - 220, height - 25, 0xffffff);
	}
}