package test.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.survivaltab.ISurvivalTab;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;

public class SurvivalTabAnvil implements ISurvivalTab
{
    public String getTabName()
    {
        return "Anvil";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.anvil);
    }

    //EntityPlayerMP
    
    public void openContainerGui(EntityPlayer player)
    {
        player.displayGUIAnvil((int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends Container> getContainerClass()
    {
        return ContainerRepair.class;
    }
    
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiRepair.class;
    }
}
