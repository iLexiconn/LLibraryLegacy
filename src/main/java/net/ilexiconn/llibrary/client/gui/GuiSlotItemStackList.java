package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * @author FiskFile
 * @since 0.1.0
 */
public class GuiSlotItemStackList extends GuiScrollingList {
    private GuiPickItem parent;

    public GuiSlotItemStackList(GuiPickItem parent, int listWidth) {
        super(Minecraft.getMinecraft(), listWidth, parent.height, 55, parent.height - 45, 20, 18);
        this.parent = parent;
    }

    @Override
    protected int getSize() {
        return parent.itemsFiltered.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        parent.selectItemIndex(index);
    }

    @Override
    protected boolean isSelected(int index) {
        return parent.itemIndexSelected(index);
    }

    @Override
    protected void drawBackground() {
        parent.drawDefaultBackground();
    }

    @Override
    protected int getContentHeight() {
        return (getSize()) * 18 + 1;
    }

    @Override
    protected void drawSlot(int listIndex, int x, int y, int par4, Tessellator tessellator) {
        if (listIndex < parent.itemsFiltered.size()) {
            ItemStack itemstack = parent.itemsFiltered.get(listIndex);

            if (itemstack != null) {
                Minecraft.getMinecraft().fontRenderer.drawString(Minecraft.getMinecraft().fontRenderer.trimStringToWidth(StatCollector.translateToLocal(itemstack.getDisplayName()), listWidth - 10), left + 22, y + 3, 0xFFFFFF);
                parent.drawItemStack(left + 1, y - 1, itemstack);
            }
        }
    }
}