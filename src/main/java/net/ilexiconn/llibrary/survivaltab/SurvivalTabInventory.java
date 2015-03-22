package net.ilexiconn.llibrary.survivaltab;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.config.ConfigHelper;
import net.ilexiconn.llibrary.config.LLibraryConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.math.NumberUtils;

public class SurvivalTabInventory implements ISurvivalTab
{
    public String getTabName()
    {
        return "container.inventory";
    }

    public ItemStack getTabIcon()
    {
        String[] array = LLibraryConfigHandler.survivalInventoryItem.split(":");
        if (array.length < 2) return resetDefaultStack();
        ItemStack stack = GameRegistry.findItemStack(array[0], array[1], 1);
        if (stack == null) return resetDefaultStack();
        if (array.length == 3) stack.setItemDamage(NumberUtils.toInt(array[2]));

        return stack;
    }

    public ItemStack resetDefaultStack()
    {
        ConfigHelper.setProperty("llibrary", Configuration.CATEGORY_GENERAL, "survivalInventoryItem", "minecraft:book", Property.Type.STRING);
        return new ItemStack(Items.book);
    }

    @SideOnly(Side.CLIENT)
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
