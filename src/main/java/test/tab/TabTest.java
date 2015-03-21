package test.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.gui.GuiPickItem;
import net.ilexiconn.llibrary.survivaltab.ISurvivalTab;
import net.ilexiconn.llibrary.survivaltab.SurvivalTabInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import test.gui.GuiPlaceholder;

public class TabTest implements ISurvivalTab
{
    public String getTabName()
    {
        return "Test";
    }

    public ItemStack getTabIcon()
    {
        return new ItemStack(Blocks.command_block);
    }

    @SideOnly(Side.CLIENT)
    public void openContainerGui(EntityPlayer player)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiPickItem("Test")
        {
            public void onClickEntry(ItemStack itemstack, EntityPlayer player)
            {
                SurvivalTabInventory.stack = itemstack;
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(player));
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainerGuiClass()
    {
        return GuiPlaceholder.class;
    }
}
