package test.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.survivaltab.ISurvivalTab;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class SurvivalTabChest implements ISurvivalTab
{
    public String getTabName()
    {
        return "Chest";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.chest);
    }

    public void openContainerGui(EntityPlayer player)
    {
        player.displayGUIChest(player.getInventoryEnderChest());
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiChest.class;
    }

    public Class<? extends Container> getContainerClass()
    {
        return ContainerChest.class;
    }
}
