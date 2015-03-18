package test.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.survivaltab.SurvivalTab;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class SurvivalTabTest extends SurvivalTab
{
    public String getTabName()
    {
        return "test";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Items.coal);
    }

    public GuiContainer getGuiContainer(EntityPlayer player)
    {
        return new GuiRepair(player.inventory, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    public Class<? extends GuiContainer> getGuiContainerClass()
    {
        return GuiRepair.class;
    }
}
