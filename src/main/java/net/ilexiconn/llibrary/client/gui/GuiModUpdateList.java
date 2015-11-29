package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private GuiModUpdates parent;
    private ArrayList<JsonModUpdate> mods;
    private Map<Integer, ResourceLocation> cachedLogo;
    private Map<Integer, Dimension> cachedLogoDimensions;

    public GuiModUpdateList(GuiModUpdates parent, ArrayList<JsonModUpdate> mods, int listWidth) {
        super(parent.mc, listWidth, parent.height, 32, parent.height - 77 + 4, 10, 35, parent.width, parent.height);
        this.parent = parent;
        this.mods = mods;
        cachedLogo = new HashMap<Integer, ResourceLocation>();
        cachedLogoDimensions = new HashMap<Integer, Dimension>();
    }

    @Override
    protected int getSize() {
        return mods.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.parent.selectModIndex(index);
    }

    @Override
    protected boolean isSelected(int index) {
        return this.parent.modIndexSelected(index);
    }

    @Override
    protected void drawBackground() {
        this.parent.drawDefaultBackground();
    }

    @Override
    protected int getContentHeight() {
        return (this.getSize()) * 35 + 1;
    }

    @Override
    protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
        Minecraft minecraft = Minecraft.getMinecraft();
        JsonModUpdate mc = mods.get(idx);
        String name = StringUtils.stripControlCodes(mc.name);
        String version = StringUtils.stripControlCodes(mc.getUpdateVersion().getVersionString());
        String type = org.apache.commons.lang3.StringUtils.capitalize(mc.updateType.name().toLowerCase());
        FontRenderer font = minecraft.fontRendererObj;

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

            float iwidth = (float) (cachedLogoDimensions.get(idx).width * scale);
            float iheight = (float) (cachedLogoDimensions.get(idx).height * scale);
            int offset = 12;
            WorldRenderer renderer = tess.getWorldRenderer();
            renderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
            renderer.func_181662_b(offset, top + iheight, 0).func_181673_a(0, 1).func_181675_d();
            renderer.func_181662_b(offset + iwidth, top + iheight, 0).func_181673_a(1, 1).func_181675_d();
            renderer.func_181662_b(offset + iwidth, top, 0).func_181673_a(1, 0).func_181675_d();
            renderer.func_181662_b(offset, top, 0).func_181673_a(0, 0).func_181675_d();
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
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setSelected(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}