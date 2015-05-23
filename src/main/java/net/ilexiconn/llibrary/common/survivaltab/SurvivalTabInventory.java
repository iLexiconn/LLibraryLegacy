package net.ilexiconn.llibrary.common.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SurvivalTabInventory implements ISurvivalTab
{
    public ItemStack stack = new ItemStack(Blocks.crafting_table);

    public String getTabName()
    {
        return "container.inventory";
    }

    public ItemStack getTabIcon()
    {
        return stack;
    }

    public void openContainer(EntityPlayer player)
    {
        //todo
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiInventory.class;
    }
}