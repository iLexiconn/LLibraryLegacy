package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.api.SurvivalTab;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonSurvivalTab extends GuiButton
{
    private ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private RenderItem renderItem = new RenderItem();
    private SurvivalTab survivalTab;

    public GuiButtonSurvivalTab(int id, SurvivalTab tab)
    {
        super(id, 0, 0, 28, 32, "");
        survivalTab = tab;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            GL11.glColor4f(1f, 1f, 1f, 1f);

            boolean selected = mc.currentScreen.getClass() != survivalTab.getContainer();
            xPosition = (mc.currentScreen.width / 2) - 88 + survivalTab.getColumn() * 29;
            yPosition = survivalTab.isInFirstRow() ? mc.currentScreen.height / 2 - ((GuiContainer) mc.currentScreen).ySize / 2 - 28 : selected ? mc.currentScreen.height / 2 + 83 : mc.currentScreen.height / 2 + 79;

            int yTexPos = survivalTab.isInFirstRow() ? selected ? 0 : 32 : selected ? 66 : 96;
            int xTexPos = id == 2 || id == 8 ? 0 : 28;
            int ySize = survivalTab.isInFirstRow() ? selected ? 28 : 32 : selected ? 26 : 32;

            if (mc.thePlayer.getActivePotionEffects().size() > 0) xPosition += 60;

            if (survivalTab.getTexture() != null) mc.renderEngine.bindTexture(survivalTab.getTexture());
            else mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(xPosition, yPosition, xTexPos, yTexPos, 28, ySize);

            if (!survivalTab.isInFirstRow() && selected) yPosition -= 3;

            RenderHelper.enableGUIStandardItemLighting();
            zLevel = 100f;
            renderItem.zLevel = 100f;
            GL11.glEnable(2896);
            GL11.glEnable(32826);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, survivalTab.getIcon(), xPosition + 6, yPosition + 8);
            renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, survivalTab.getIcon(), xPosition + 6, yPosition + 8);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            renderItem.zLevel = 0f;
            zLevel = 0f;
            RenderHelper.disableStandardItemLighting();

            if (enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
                drawHoveringText(Collections.singletonList(I18n.format(survivalTab.getLabel())), mouseX, mouseY);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            if (mc.currentScreen.getClass() != survivalTab.getContainer())
            {
                MinecraftForge.EVENT_BUS.post(new SurvivalTab.ClickEvent(survivalTab, mc.thePlayer));
                LLibrary.networkWrapper.sendToServer(new MessageLLibrarySurvivalTab(survivalTab.getIndex()));
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void drawHoveringText(List<String> list, int mouseX, int mouseY)
    {
        if (!list.isEmpty())
        {
            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            int topWidth = 0;

            for (String line : list)
            {
                int width = font.getStringWidth(line);
                if (width > topWidth) topWidth = width;
            }

            int renderX = mouseX + 12;
            int renderY = mouseY - 12;
            int i1 = 8;

            if (list.size() > 1) i1 += 2 + (list.size() - 1) * 10;
            if (renderX + topWidth > width) renderX -= 28 + topWidth;

            zLevel = 300f;
            renderItem.zLevel = 300f;

            int mainColour = -267386864;

            drawGradientRect(renderX - 3, renderY - 4, renderX + topWidth + 3, renderY - 3, mainColour, mainColour);
            drawGradientRect(renderX - 3, renderY + i1 + 3, renderX + topWidth + 3, renderY + i1 + 4, mainColour, mainColour);
            drawGradientRect(renderX - 3, renderY - 3, renderX + topWidth + 3, renderY + i1 + 3, mainColour, mainColour);
            drawGradientRect(renderX - 4, renderY - 3, renderX - 3, renderY + i1 + 3, mainColour, mainColour);
            drawGradientRect(renderX + topWidth + 3, renderY - 3, renderX + topWidth + 4, renderY + i1 + 3, mainColour, mainColour);

            int borderColour = 1347420415;
            int gradient = (borderColour & 16711422) >> 1 | borderColour & -16777216;

            drawGradientRect(renderX - 3, renderY - 3 + 1, renderX - 3 + 1, renderY + i1 + 3 - 1, borderColour, gradient);
            drawGradientRect(renderX + topWidth + 2, renderY - 3 + 1, renderX + topWidth + 3, renderY + i1 + 3 - 1, borderColour, gradient);
            drawGradientRect(renderX - 3, renderY - 3, renderX + topWidth + 3, renderY - 3 + 1, borderColour, borderColour);
            drawGradientRect(renderX - 3, renderY + i1 + 2, renderX + topWidth + 3, renderY + i1 + 3, gradient, gradient);

            for (int letterIndex = 0; letterIndex < list.size(); ++letterIndex)
            {
                String s1 = list.get(letterIndex);
                font.drawStringWithShadow(s1, renderX, renderY, -1);
                if (letterIndex == 0) renderY += 2;
                renderY += 10;
            }

            zLevel = 0f;
            renderItem.zLevel = 0f;
        }
    }

    public void func_146113_a(SoundHandler soundHandler)
    {

    }
}