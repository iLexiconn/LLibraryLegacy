package net.ilexiconn.llibrary.client.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.GuiScrollingList;

public class GuiSlotModUpdateContainerList extends GuiScrollingList
{
	private GuiCheckForUpdates parent;
	private ResourceLocation[] cachedLogo;
	private Dimension[] cachedLogoDimensions;
	private int offset;

	public GuiSlotModUpdateContainerList(GuiCheckForUpdates parent, int listWidth)
	{
		super(parent.getMinecraftInstance(), listWidth, parent.height, 20, parent.height - 45, 20, 34);
		this.parent = parent;
		this.cachedLogo = new ResourceLocation[getSize()];
		this.cachedLogoDimensions = new Dimension[getSize()];
	}

	@Override
	protected int getSize()
	{
		return parent.outdatedMods.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2)
	{
		parent.selectItemIndex(var1);
	}

	@Override
	protected boolean isSelected(int var1)
	{
		return parent.itemIndexSelected(var1);
	}

	@Override
	protected void drawBackground()
	{
		parent.drawDefaultBackground();
	}

	@Override
	protected int getContentHeight()
	{
		return (getSize()) * 34 + 1;
	}
	
	public int getLeft()
	{
		return left;
	}
	
	public int getTop()
	{
		return top;
	}

	@Override
	protected void drawSlot(int listIndex, int x, int y, int par4, Tessellator tesselator)
	{
		if (listIndex < parent.outdatedMods.size())
		{
			ModUpdateContainer mod = parent.outdatedMods.get(listIndex);
			
			if (mod != null)
			{
				int i = 4 + 32;
				parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mod.name, listWidth - 10), left + i, y + 2, 0xFFFFFF);
				parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mod.modid, listWidth - 10), left + i, y + 12, 0xCCCCCC);
				parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mod.version, listWidth - 10), left + i, y + 22, 0xCCCCCC);
				
				
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft mc = parent.getMinecraftInstance();
                TextureManager tm = mc.getTextureManager();
                
                if (cachedLogo[listIndex] == null)
				{
				    BufferedImage logo = mod.thumbnail;
					
				    if (logo != null)
				    {
				    	cachedLogo[listIndex] = tm.getDynamicTextureLocation("mod_thumbnail", new DynamicTexture(logo));
				    	cachedLogoDimensions[listIndex] = new Dimension(logo.getWidth(), logo.getHeight());
				    }
				}
                else
				{
				    mc.renderEngine.bindTexture(cachedLogo[listIndex]);
				    double scaleX = cachedLogoDimensions[listIndex].width / 32.0;
				    double scaleY = cachedLogoDimensions[listIndex].height / 32.0;
				    double scale = 1.0;
				    
				    if (scaleX > 1 || scaleY > 1)
				    {
				        scale = 1.0 / Math.max(scaleX, scaleY);
				    }
				    
				    cachedLogoDimensions[listIndex].width *= scale;
				    cachedLogoDimensions[listIndex].height *= scale;
				    int top = y - 1;
				    int offset = 21;
				    Tessellator tess = Tessellator.instance;
				    tess.startDrawingQuads();
				    tess.addVertexWithUV(offset, top + cachedLogoDimensions[listIndex].height, 0, 0, 1);
				    tess.addVertexWithUV(offset + cachedLogoDimensions[listIndex].width, top + cachedLogoDimensions[listIndex].height, 0, 1, 1);
				    tess.addVertexWithUV(offset + cachedLogoDimensions[listIndex].width, top, 0, 1, 0);
				    tess.addVertexWithUV(offset, top, 0, 0, 0);
				    tess.draw();
				}
			}
		}
	}
}