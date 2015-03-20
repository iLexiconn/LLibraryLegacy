package test.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.survivaltab.ISurvivalTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import test.gui.GuiTest;

public class TabTest implements ISurvivalTab
{
    public String getTabName()
    {
        return "Test";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.end_portal);
    }

    @SideOnly(Side.CLIENT)
    public void openContainerGui(EntityPlayer player)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTest(player));
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiTest.class;
    }
}
