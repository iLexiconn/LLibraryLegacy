package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLLibraryConfig extends GuiScreen
{
    public GuiScreen parent;
//    public GuiPickItem pickItem;

    public GuiLLibraryConfig(GuiScreen p)
    {
        parent = p;
//        pickItem = new GuiPickItem("Survival Inventory Icon")
//        {
//            public void onClickEntry(ItemStack itemstack, EntityPlayer player)
//            {
//                ConfigHelper.setProperty("llibrary", Configuration.CATEGORY_GENERAL, "survivalInventoryItem", Item.itemRegistry.getNameForObject(itemstack.getItem()) + ":" + itemstack.getItemDamage(), Property.Type.STRING);
//                mc.displayGuiScreen(parent);
//            }
//        };
    }

//    protected void keyTyped(char character, int key)
//    {
//        pickItem.keyTyped(character, key);
//    }
//
//    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
//    {
//        pickItem.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
//    }
//
//    protected void mouseClicked(int mouseX, int mouseY, int button)
//    {
//        pickItem.mouseClicked(mouseX, mouseY, button);
//    }
//
//    protected void mouseMovedOrUp(int mouseX, int mouseY, int event)
//    {
//        pickItem.mouseMovedOrUp(mouseX, mouseY, event);
//    }
//
//    public void drawScreen(int mouseX, int mouseY, float partialTicks)
//    {
//        pickItem.drawScreen(mouseX, mouseY, partialTicks);
//    }
}
