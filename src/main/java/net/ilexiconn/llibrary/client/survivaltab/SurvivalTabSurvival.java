package net.ilexiconn.llibrary.client.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class SurvivalTabSurvival extends SurvivalTab
{
    public String getTabName()
    {
        return "Survival";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.crafting_table);
    }

    public GuiContainer getGuiContainer(EntityPlayer player)
    {
        return new GuiInventory(player);
    }

    public Class<? extends GuiContainer> getGuiContainerClass()
    {
        return GuiInventory.class;
    }
}
