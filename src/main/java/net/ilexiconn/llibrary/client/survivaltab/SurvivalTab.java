package net.ilexiconn.llibrary.client.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class SurvivalTab extends GuiButton
{
    private ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private RenderItem renderItem = new RenderItem();
    private ItemStack stackIcon = getTabIcon();

    public SurvivalTab()
    {
        super(0, 0, 0, 28, 32, "");
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            GL11.glColor4f(1f, 1f, 1f, 1f);
            
            xPosition = 215 + id * 30;
            yPosition = 98;
            
            boolean selected = mc.currentScreen.getClass() != getGuiContainerClass();
            
            int yTexPos = selected ? 2 : 32;
            int ySize = selected ? 26 : 32;
            int yPos = yPosition + (selected ? 2 : 0);

            mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(xPosition, yPos, 28, yTexPos, 28, ySize);

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
                drawHoveringText(getTabName(), mouseX, mouseY);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)
        {
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
            mc.displayGuiScreen(getGuiContainer(mc.thePlayer));
            return true;
        }
        
        else return false;
    }

    public void drawHoveringText(String text, int mouseX, int mouseY)
    {
        drawHoveringText(Arrays.asList(text), mouseX, mouseY, Minecraft.getMinecraft().fontRenderer);
    }

    public void drawHoveringText(List list, int mouseX, int p_146283_3_, FontRenderer font)
    {
        if (!list.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            int k = 0;

            for (Object obj : list)
            {
                String s = (String) obj;
                int l = font.getStringWidth(s);

                if (l > k) k = l;
            }

            int j2 = mouseX + 12;
            int k2 = p_146283_3_ - 12;
            int i1 = 8;

            if (list.size() > 1) i1 += 2 + (list.size() - 1) * 10;
            if (j2 + k > width) j2 -= 28 + k;

            zLevel = 300f;
            renderItem.zLevel = 300f;
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

            for (int string = 0; string < list.size(); ++string)
            {
                String s1 = (String)list.get(string);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (string == 0) k2 += 2;

                k2 += 10;
            }

            zLevel = 0f;
            renderItem.zLevel = 0f;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    public abstract String getTabName();

    public abstract ItemStack getTabIcon();

    public abstract GuiContainer getGuiContainer(EntityPlayer player);

    public abstract Class<? extends GuiContainer> getGuiContainerClass();
}