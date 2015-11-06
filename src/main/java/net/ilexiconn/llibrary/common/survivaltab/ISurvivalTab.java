package net.ilexiconn.llibrary.common.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @deprecated Use {@link SurvivalTab} instead.
 */
public interface ISurvivalTab {
    /**
     * @return unlocalized name string
     * @since 0.2.0
     */
    String getTabName();

    /**
     * @return itemstack with item or block to be displayed
     * @since 0.2.0
     */
    ItemStack getTabIcon();

    /**
     * Called when the survival tab is clicked.
     *
     * @param player the player opening the gui
     * @since 0.2.0
     */
    void openContainer(Minecraft mc, EntityPlayer player);

    /**
     * @return class of the container gui.
     * @see net.minecraft.client.gui.inventory.GuiContainer
     * @since 0.2.0
     */
    @SideOnly(Side.CLIENT)
    Class<? extends GuiContainer> getContainerGuiClass();
}