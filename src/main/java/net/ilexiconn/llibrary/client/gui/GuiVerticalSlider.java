package net.ilexiconn.llibrary.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author FiskFille
 */
public class GuiVerticalSlider extends GuiScreen
{
	public int x;
	public int y;
	public int width;
	public int height;
	public int maxScroll;
	public int minScroll;
	public boolean dragging = false;
	
	public GuiVerticalSlider(int x, int y, int width, int height, int maxScroll, int minScroll)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxScroll = maxScroll;
		this.minScroll = minScroll;
	}
	
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
	{
		if (dragging)
		{
			y = mouseY - height / 2;
		}
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int button)
	{
		super.mouseClicked(mouseX, mouseY, button);
		
		if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height)
		{
			dragging = true;
		}
	}
	
	protected void mouseMovedOrUp(int mouseX, int mouseY, int event)
	{
		super.mouseMovedOrUp(mouseX, mouseY, event);
		dragging = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (y < minScroll)
		{
			y = minScroll;
		}
		if (y > maxScroll - height)
		{
			y = maxScroll - height;
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0, 0, 0, 0.5F);
		drawTexturedModalRect(x - 2, minScroll - 2, 0, 0, width + 4, maxScroll - minScroll + 4);
		float shade = 0.8F;
		GL11.glColor4f(shade, shade, shade, 1.0F);
		drawTexturedModalRect(x, y, 0, 0, width, height);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}