package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;

public class GuiSlotItemStackList extends GuiScrollingList
{
    private GuiPickItem parent;
    private ArrayList<ItemStack> items;

    public GuiSlotItemStackList(GuiPickItem parent, ArrayList<ItemStack> items, int listWidth)
    {
        super(parent.getMinecraftInstance(), listWidth, parent.height, 55, parent.height - 45, 20, 18);
        this.parent = parent;
        this.items = items;
    }

    @Override
    protected int getSize()
    {
        return parent.itemsFiltered.size();
    }

    @Override
    protected void elementClicked(int var1, boolean var2)
    {
        this.parent.selectItemIndex(var1);
    }

    @Override
    protected boolean isSelected(int var1)
    {
        return this.parent.itemIndexSelected(var1);
    }

    @Override
    protected void drawBackground()
    {
        this.parent.drawDefaultBackground();
    }

    @Override
    protected int getContentHeight()
    {
        return (getSize()) * 18 + 1;
    }

    @Override
    protected void drawSlot(int listIndex, int x, int y, int par4, Tessellator tesselator)
    {
        if (listIndex < parent.itemsFiltered.size())
        {
            ItemStack itemstack = parent.itemsFiltered.get(listIndex);

            if (itemstack != null)
            {
                parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(StatCollector.translateToLocal(itemstack.getDisplayName()), listWidth - 10), left + 22, y + 3, 0xFFFFFF);
                parent.drawItemStack(left + 1, y - 1, itemstack);
            }
        }
    }
}