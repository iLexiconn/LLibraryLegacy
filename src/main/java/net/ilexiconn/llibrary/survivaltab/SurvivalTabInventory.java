package net.ilexiconn.llibrary.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SurvivalTabInventory implements ISurvivalTab
{
    public String getTabName()
    {
        return "container.inventory";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.crafting_table);
    }

    public void openContainerGui(EntityPlayer player)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(player));
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiInventory.class;
    }
}
