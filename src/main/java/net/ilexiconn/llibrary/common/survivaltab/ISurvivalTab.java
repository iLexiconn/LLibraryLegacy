package net.ilexiconn.llibrary.common.survivaltab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
     * @see GuiContainer
     * @since 0.2.0
     */
    @SideOnly(Side.CLIENT)
    Class<? extends GuiContainer> getContainerGuiClass();
}