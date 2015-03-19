package net.ilexiconn.llibrary.client.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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
            
            int yTexPos = selected ? 3 : 32;
            int ySize = selected ? 25 : 32;
            int yPos = yPosition + (selected ? 3 : 0);

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

    public abstract String getTabName();

    public abstract ItemStack getTabIcon();

    public abstract GuiContainer getGuiContainer(EntityPlayer player);

    public abstract Class<? extends GuiContainer> getGuiContainerClass();
}