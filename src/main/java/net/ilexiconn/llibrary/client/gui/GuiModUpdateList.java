package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FiskFile
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.update.UpdateHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiModUpdateList extends GuiScrollingList {
    private Field right;

    private GuiModUpdates parent;
    private ArrayList<JsonModUpdate> mods;
    private Map<Integer, ResourceLocation> cachedLogo;
    private Map<Integer, Dimension> cachedLogoDimensions;

    public GuiModUpdateList(GuiModUpdates parent, ArrayList<JsonModUpdate> mods, int listWidth) {
        super(parent.mc, listWidth, parent.height, 32, parent.height - 77 + 4, 10, 35);
        this.parent = parent;
        this.mods = mods;
        cachedLogo = new HashMap<Integer, ResourceLocation>();
        cachedLogoDimensions = new HashMap<Integer, Dimension>();

        try {
            right = getClass().getSuperclass().getDeclaredField("right");
            right.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected int getSize() {
        return mods.size();
    }

    protected void elementClicked(int index, boolean doubleClick) {
        this.parent.selectModIndex(index);
    }

    protected boolean isSelected(int index) {
        return this.parent.modIndexSelected(index);
    }

    protected void drawBackground() {
        this.parent.drawDefaultBackground();
    }

    protected int getContentHeight() {
        return (this.getSize()) * 35 + 1;
    }

    protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
        Minecraft minecraft = Minecraft.getMinecraft();
        JsonModUpdate mc = mods.get(idx);
        String name = StringUtils.stripControlCodes(mc.name);
        String version = StringUtils.stripControlCodes(mc.getUpdateVersion().getVersionString());
        String type = org.apache.commons.lang3.StringUtils.capitalize(mc.updateType.name().toLowerCase());
        FontRenderer font = minecraft.fontRenderer;

        font.drawString(font.trimStringToWidth(name, listWidth - 10), this.left + 36, top + 2, 0xFFFFFF);
        font.drawString(font.trimStringToWidth(version, listWidth - 10), this.left + 36, top + 12, 0xCCCCCC);
        font.drawString(font.trimStringToWidth(type, listWidth - 10), this.left + 36, top + 22, mc.updateType.color);

        if (!cachedLogo.containsKey(idx)) {
            BufferedImage logo = mc.thumbnail;

            if (logo != null) {
                cachedLogo.put(idx, minecraft.getTextureManager().getDynamicTextureLocation("mod_thumbnail", new DynamicTexture(logo)));
                cachedLogoDimensions.put(idx, new Dimension(logo.getWidth(), logo.getHeight()));
            }
        } else {
            GL11.glColor4f(1f, 1f, 1f, 1f);
            minecraft.renderEngine.bindTexture(cachedLogo.get(idx));
            double scaleX = cachedLogoDimensions.get(idx).width / 32.0;
            double scaleY = cachedLogoDimensions.get(idx).height / 32.0;
            double scale = 1.0;

            if (scaleX > 1 || scaleY > 1) {
                scale = 1.0 / Math.max(scaleX, scaleY);
            }

            double iwidth = cachedLogoDimensions.get(idx).width * scale;
            double iheight = cachedLogoDimensions.get(idx).height * scale;
            int offset = 12;
            tess.startDrawingQuads();
            tess.addVertexWithUV(offset, top + iheight, 0, 0, 1);
            tess.addVertexWithUV(offset + iwidth, top + iheight, 0, 1, 1);
            tess.addVertexWithUV(offset + iwidth, top, 0, 1, 0);
            tess.addVertexWithUV(offset, top, 0, 0, 0);
            tess.draw();
        }
    }

    public ArrayList<JsonModUpdate> getMods() {
        return mods;
    }

    public int getListWidth() {
        return listWidth;
    }

    public int getRight() {
        try {
            return (Integer) right.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getBottom() {
        return bottom;
    }
}