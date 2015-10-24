package net.ilexiconn.llibrary.api;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Survival tab builder. Use this to add tabs for the survival inventory.
 *
 * @author      iLexiconn
 * @since       0.5.0
 */
public class SurvivalTab
{
    private static List<SurvivalTab> survivalTabList = Lists.newArrayList();
    private static int page;

    private int index;
    private String label;
    private ItemStack icon;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiContainer> container;
    @SideOnly(Side.CLIENT)
    private ResourceLocation texture;

    private SurvivalTab(int i, String l)
    {
        LLibrary.logger.info("Creating survival tab with index " + i);
        index = i;
        label = l;
    }

    /**
     * Initialize your survival tab. You can save the result to a field if you want to edit the survival tab ever again.
     *
     * @param l     The unlocalized survival tab label.
     * @return      The survival tab instance.
     */
    public static SurvivalTab create(String l)
    {
        SurvivalTab survivalTab = new SurvivalTab(survivalTabList.size(), l);
        survivalTabList.add(survivalTab);
        return survivalTab;
    }

    /**
     * Set the survival tab's icon. This can be set at any time.
     *
     * @param i     The new icon.
     * @return      The updated survival tab instance.
     */
    public SurvivalTab setIcon(ItemStack i)
    {
        icon = i;
        return this;
    }

    /**
     * Set the container for this survival tab. This value is only used to check if LLibrary can show the survival tabs in your gui. Can only be used on the CLIENT side. To open containers, use {@link ClickEvent} (called on both CLIENT and SERVER side).
     *
     * @param c     The container to display the tabs.
     * @return      The updated survival tab instance.
     */
    @SideOnly(Side.CLIENT)
    public SurvivalTab setContainer(Class<? extends GuiContainer> c)
    {
        container = c;
        return this;
    }

    /**
     * Set a custom texture for the survival tab. If null, the default texture will be used. This can be set at any time. Can only be used on the CLIENT side.
     *
     * @param t     The new texture.
     * @return      The updated survival tab instance.
     */
    @SideOnly(Side.CLIENT)
    public SurvivalTab setTexture(String t)
    {
        texture = new ResourceLocation(t);
        return this;
    }

    public int getIndex()
    {
        return index;
    }

    public int getColumn()
    {
        if (index > 11) return ((index - 12) % 10) % 5;
        else return index % 6;
    }

    public boolean isInFirstRow()
    {
        if (index > 11) return ((index - 12) % 10) < 5;
        else return index < 6;
    }

    public int getPage()
    {
        if (index > 11) return ((index - 12) / 10) + 1;
        else return 0;
    }

    public String getLabel()
    {
        return label;
    }

    public ItemStack getIcon()
    {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getContainer()
    {
        return container;
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexture()
    {
        return texture;
    }

    /**
     * Subscribe to this event to open guis for the player. This event gets fired on both CLIENT and SERVER.
     */
    public static class ClickEvent extends Event
    {
        private SurvivalTab survivalTab;
        private EntityPlayer entityPlayer;

        public ClickEvent(SurvivalTab t, EntityPlayer p)
        {
            survivalTab = t;
            entityPlayer = p;
        }

        public SurvivalTab getSurvivalTab()
        {
            return survivalTab;
        }

        public EntityPlayer getEntityPlayer()
        {
            return entityPlayer;
        }
    }

    /* =========================================== FOR INTERNAL USE ONLY =========================================== */

    /**
     * For internal use only.
     */
    @Deprecated
    public static List<SurvivalTab> getSurvivalTabList()
    {
        return survivalTabList;
    }

    /**
     * For internal use only.
     */
    @Deprecated
    public static int getCurrentPage()
    {
        return page;
    }

    /**
     * For internal use only.
     */
    @Deprecated
    public static void setCurrentPage(int p)
    {
        page = p;
    }
}
