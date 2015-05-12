package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@SideOnly(Side.CLIENT)
public class GuiLLibraryConfig extends GuiScreen
{
    public GuiScreen parent;
    public GuiPickItem pickItem;

    public GuiLLibraryConfig(GuiScreen p)
    {
        parent = p;
        pickItem = new GuiPickItem("Survival Inventory Icon")
        {
            public void onClickEntry(ItemStack itemstack, EntityPlayer player)
            {
                ConfigHelper.setProperty("llibrary", Configuration.CATEGORY_GENERAL, "survivalInventoryItem", Item.itemRegistry.getNameForObject(itemstack.getItem()) + ":" + itemstack.getItemDamage(), Property.Type.STRING);
                mc.displayGuiScreen(parent);
            }
        };
    }

    protected void keyTyped(char character, int key)
    {
        pickItem.keyTyped(character, key);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
    {
        pickItem.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
    }

    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
        pickItem.mouseClicked(mouseX, mouseY, button);
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int event)
    {
        pickItem.mouseMovedOrUp(mouseX, mouseY, event);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        pickItem.drawScreen(mouseX, mouseY, partialTicks);
    }
}
