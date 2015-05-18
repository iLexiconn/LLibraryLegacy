package net.ilexiconn.llibrary.client.gui;

import java.util.Iterator;
import java.util.List;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import scala.actors.threadpool.Arrays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonCheckForUpdates extends GuiButton
{
	protected static final ResourceLocation buttonTextures = new ResourceLocation("llibrary:textures/gui/widgets.png");
	private boolean isHoveringOverButton;
	private int hoverTimer;
	public int screenWidth;
	public int screenHeight;
	
    public GuiButtonCheckForUpdates(int id, int x, int y)
    {
        super(id, x, y, 20, 20, "");
    }
    
    public void update()
    {
    	if (isHoveringOverButton)
    	{
    		++hoverTimer;
    	}
    	else
    	{
    		hoverTimer = 0;
    	}
	}
    
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean flag = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
    	
        if (visible)
        {
        	isHoveringOverButton = flag;
            mc.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int k = 0;

            if (flag)
            {
                k += width;
            }

            drawTexturedModalRect(xPosition, yPosition, k, 0, width, height);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            if (hoverTimer >= 20)
    		{
    			drawHoveringText(Arrays.asList(new String[] {I18n.format("gui.llibrary.updatecheck.desc")}), mouseX, mouseY, mc.fontRenderer);
    		}
        }
    }
    
    protected void drawHoveringText(List text, int x, int y, FontRenderer font)
    {
        if (!text.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = text.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (text.size() > 1)
            {
                i1 += 2 + (text.size() - 1) * 10;
            }

            if (j2 + k > screenWidth)
            {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > screenHeight)
            {
                k2 = screenHeight - i1 - 6;
            }
            
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
            
            for (int i2 = 0; i2 < text.size(); ++i2)
            {
                String s1 = (String)text.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0)
                {
                    k2 += 2;
                }

                k2 += 10;
            }
            
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}