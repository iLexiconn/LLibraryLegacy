package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;
import net.ilexiconn.llibrary.common.survivaltab.ICustomSurvivalTabTexture;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

/**
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.survivaltab.TabHelper
 * @since 0.2.0
 */
@SideOnly(Side.CLIENT)
public class GuiSurvivalTab extends GuiButton
{
    private ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
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
            yPosition = survivalTabContainer.isTabInFirstRow() ? mc.currentScreen.height / 2 - ((GuiContainer) mc.currentScreen).ySize / 2 - 28 : selected ? mc.currentScreen.height / 2 + 83 : mc.currentScreen.height / 2 + 79;

            int yTexPos = survivalTabContainer.isTabInFirstRow() ? selected ? 0 : 32 : selected ? 66 : 96;
            int xTexPos = id == 2 || id == 8 ? 0 : 28;
            int ySize = survivalTabContainer.isTabInFirstRow() ? selected ? 28 : 32 : selected ? 26 : 32;

            if (survivalTabContainer.getSurvivalTab() instanceof ICustomSurvivalTabTexture)
                mc.renderEngine.bindTexture(((ICustomSurvivalTabTexture) survivalTabContainer.getSurvivalTab()).getTabTexture());
            else
                mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(xPosition, yPosition, xTexPos, yTexPos, 28, ySize);

            if (!survivalTabContainer.isTabInFirstRow() && selected)
                yPosition -= 3;

            RenderHelper.enableGUIStandardItemLighting();
            zLevel = 100f;
            mc.getRenderItem().zLevel = 100f;
            GL11.glEnable(2896);
            GL11.glEnable(32826);
            mc.getRenderItem().func_180450_b(stackIcon, xPosition + 6, yPosition + 8);
            mc.getRenderItem().func_175030_a(mc.fontRendererObj, stackIcon, xPosition + 6, yPosition + 8);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            mc.getRenderItem().zLevel = 0f;
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
            else
                return false;
        }
        else
            return false;
    }

    public void drawHoveringText(String text, int mouseX, int mouseY)
    {
        drawHoveringText(Collections.singletonList(text), mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj);
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

                if (width > topWidth)
                    topWidth = width;
            }

            int renderX = mouseX + 12;
            int renderY = mouseY - 12;
            int i1 = 8;

            if (text.size() > 1)
                i1 += 2 + (text.size() - 1) * 10;
            if (renderX + topWidth > width)
                renderX -= 28 + topWidth;

            zLevel = 300f;
            Minecraft.getMinecraft().getRenderItem().zLevel = 300f;

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
                font.drawString(s1, renderX, renderY, -1);

                if (letterIndex == 0)
                    renderY += 2;

                renderY += 10;
            }

            zLevel = 0f;
            Minecraft.getMinecraft().getRenderItem().zLevel = 0f;
        }
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {

    }
}