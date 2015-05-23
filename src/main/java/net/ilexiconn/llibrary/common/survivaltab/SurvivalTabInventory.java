package net.ilexiconn.llibrary.common.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class SurvivalTabInventory implements ISurvivalTab
{
    public ItemStack stack = new ItemStack(Items.diamond_sword);

    public String getTabName()
    {
        return "container.inventory";
    }

    public ItemStack getTabIcon()
    {
        return stack;
    }

    public void openContainer(Minecraft mc, EntityPlayer player)
    {
        mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
        mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiInventory.class;
    }
}