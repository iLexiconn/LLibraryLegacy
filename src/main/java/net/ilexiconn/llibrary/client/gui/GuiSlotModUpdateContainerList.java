package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author FiskFile
 * @see net.ilexiconn.llibrary.common.update.UpdateHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiSlotModUpdateContainerList extends GuiScrollingList
{
    private GuiModUpdates parent;
    private ResourceLocation[] cachedLogo;
    private Dimension[] cachedLogoDimensions;

    public GuiSlotModUpdateContainerList(GuiModUpdates parent, int listWidth)
    {
        super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 48, 10, 35);
        this.parent = parent;
        cachedLogo = new ResourceLocation[getSize()];
        cachedLogoDimensions = new Dimension[getSize()];
    }

    protected int getSize()
    {
        return VersionHandler.getOutdatedMods().size();
    }

    protected void elementClicked(int index, boolean doubleClick)
    {
        parent.selectItemIndex(index);
    }

    protected boolean isSelected(int index)
    {
        return parent.itemIndexSelected(index);
    }

    protected void drawBackground()
    {
        parent.drawDefaultBackground();
    }

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

    protected void drawSlot(int listIndex, int x, int y, int par4, Tessellator tessellator)
    {
        if (listIndex < VersionHandler.getOutdatedMods().size())
        {
            JsonModUpdate mod = VersionHandler.getOutdatedMods().get(listIndex);

            if (mod != null)
            {
                WorldRenderer renderer = tessellator.getWorldRenderer();
                renderer.startDrawingQuads();
                switch (mod.updateType)
                {
                    case ALPHA:
                        renderer.setColorRGBA(255, 0, 0, 255);
                        break;
                    case BETA:
                        renderer.setColorRGBA(0, 0, 255, 255);
                        break;
                    case RELEASE:
                        renderer.setColorRGBA(0, 255, 0, 255);
                        break;
                }
                renderer.addVertex(left + 1, y + getContentHeight() - 3, 0);
                renderer.addVertex(left + listWidth - 8, y + getContentHeight() - 3, 0);
                renderer.addVertex(left + listWidth - 8, y - 1, 0);
                renderer.addVertex(left + 1, y - 1, 0);
                tessellator.draw();

                int i = 4 + 32;
                parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mod.name, listWidth - 10), left + i, y + 2, 0xFFFFFF);
                parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mod.getUpdateVersion().getVersionString(), listWidth - 10), left + i, y + 12, 0xCCCCCC);
                parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(StringUtils.capitalize(mod.updateType.name().toLowerCase()), listWidth - 10), left + i, y + 22, 0xCCCCCC);

                GL11.glColor4f(1f, 1f, 1f, 1f);
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
                    int offset = 12;
                    renderer.startDrawingQuads();
                    renderer.addVertexWithUV(offset, top + cachedLogoDimensions[listIndex].height, 0, 0, 1);
                    renderer.addVertexWithUV(offset + cachedLogoDimensions[listIndex].width, top + cachedLogoDimensions[listIndex].height, 0, 1, 1);
                    renderer.addVertexWithUV(offset + cachedLogoDimensions[listIndex].width, top, 0, 1, 0);
                    renderer.addVertexWithUV(offset, top, 0, 0, 0);
                    tessellator.draw();
                }
            }
        }
    }
}