package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSurvivalTab extends GuiButton
{
    private ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private RenderItem renderItem = new RenderItem();
    private SurvivalTab survivalTabContainer;
    private ItemStack stackIcon;

    public GuiSurvivalTab(int id, SurvivalTab tab)
    {
        super(id, 0, 0, 28, 32, "");
        survivalTabContainer = tab;
        stackIcon = tab.getSurvivalTab().getTabIcon();
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            GL11.glColor4f(1f, 1f, 1f, 1f);

            boolean selected = mc.currentScreen.getClass() != survivalTabContainer.getSurvivalTab().getContainerGuiClass();
            xPosition = (mc.currentScreen.width / 2) - 88 + survivalTabContainer.getTabColumn() * 29;
            yPosition = survivalTabContainer.isTabInFirstRow() ? mc.currentScreen.height / 2 - 112 : selected ? mc.currentScreen.height / 2 + 83 : mc.currentScreen.height / 2 + 79;

            int yTexPos = survivalTabContainer.isTabInFirstRow() ? selected ? 0 : 32 : selected ? 66 : 96;
            int xTexPos = id == 2 || id == 8 ? 0 : 28;
            int ySize = survivalTabContainer.isTabInFirstRow() ? selected ? 29 : 32 : selected ? 26 : 32;

            mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(xPosition, yPosition, xTexPos, yTexPos, 28, ySize);

            if (!survivalTabContainer.isTabInFirstRow() && selected) yPosition -= 3;

            RenderHelper.enableGUIStandardItemLighting();
            zLevel = 100f;
            renderItem.zLevel = 100f;
            GL11.glEnable(2896);
            GL11.glEnable(32826);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stackIcon, xPosition + 6, yPosition + 8);
            renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, stackIcon, xPosition + 6, yPosition + 8);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            renderItem.zLevel = 0f;
            zLevel = 0f;
            RenderHelper.disableStandardItemLighting();

            if (enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
                drawHoveringText(I18n.format(survivalTabContainer.getSurvivalTab().getTabName()), mouseX, mouseY);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
        {
            if (mc.currentScreen.getClass() != survivalTabContainer.getSurvivalTab().getContainerGuiClass())
            {
                LLibrary.networkWrapper.sendToServer(new MessageLLibrarySurvivalTab(survivalTabContainer.getTabIndex()));
                return true;
            }

            return false;
        }

        else return false;
    }

    public void drawHoveringText(String text, int mouseX, int mouseY)
    {
        drawHoveringText(Collections.singletonList(text), mouseX, mouseY, Minecraft.getMinecraft().fontRenderer);
    }

    public void drawHoveringText(List text, int mouseX, int mouseY, FontRenderer font)
    {
        if (!text.isEmpty())
        {
            int topWidth = 0;

            for (Object object : text)
            {
                String s = (String) object;
                int width = font.getStringWidth(s);

                if (width > topWidth) topWidth = width;
            }

            int renderX = mouseX + 12;
            int renderY = mouseY - 12;
            int i1 = 8;

            if (text.size() > 1) i1 += 2 + (text.size() - 1) * 10;
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

            for (int letterIndex = 0; letterIndex < text.size(); ++letterIndex)
            {
                String s1 = (String) text.get(letterIndex);
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