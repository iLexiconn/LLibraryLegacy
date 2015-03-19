package net.ilexiconn.llibrary.survivaltab;

import net.minecraft.item.ItemStack;

/**
 * Interface for custom survival tabs, register with TabRegistry.registerSurvivalTab().
 *
 * @author iLexiconn
 */
public interface ISurvivalTab
{
    /**
     * @return unlocalized name string
     */
    public String getTabName();

    /**
     * @return itemstack with item or block to be displayed
     */
    public ItemStack getTabIcon();

    /**
     * Called when the survival tab is clicked.
     */
    public void openContainerGui();
}
