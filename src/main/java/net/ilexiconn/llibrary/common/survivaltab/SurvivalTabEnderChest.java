package net.ilexiconn.llibrary.common.survivaltab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SurvivalTabEnderChest implements ISurvivalTab
{
    public ItemStack stack = new ItemStack(Blocks.ender_chest);

    public String getTabName()
    {
        return "Ender Chest";
    }

    public ItemStack getTabIcon()
    {
        return stack;
    }

    public void openContainer(Minecraft mc, EntityPlayer player)
    {
        player.displayGUIChest(player.getInventoryEnderChest());
    }

    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiChest.class;
    }
}
