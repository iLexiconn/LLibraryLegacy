package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.api.Toast;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * Helper class for GUIs.
 * 
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiHelper
{
    /**
     * @deprecated  Use {@link Toast#makeText(String...)} instead.
     * @param x     The x position.
     * @param y     The y position.
     * @param text  The text to display. Every string is rendered on a new line.
     */
    @Deprecated
    public static void createToast(int x, int y, String... text)
    {
        Toast.makeText(text).setPosition(x, y).show();
    }

    /* x */

    private static Map<GuiOverride, Class<? extends GuiScreen>> overrideMap = Maps.newHashMap();
    /**
     * A method for adding {@link GuiOverride} to an existing {@link GuiScreen} or {@link net.minecraft.client.gui.inventory.GuiContainer} {@link GuiOverride} classes may get added twice.
     *
     * @see #getOverridesForGui(Class)
     * @see GuiOverride
     * @since 0.1.0
     */
    public static void addOverride(Class<? extends GuiScreen> clazz, GuiOverride gui)
    {
        overrideMap.put(gui, clazz);
    }
    /**
     * Get a list of all the overrides of a specific GUI class.
     *
     * @return the list with {@link GuiOverride} instances
     * @see #addOverride(Class, GuiOverride)
     * @see GuiOverride
     * @since 0.1.0
     */
    public static List<GuiOverride> getOverridesForGui(Class<? extends GuiScreen> clazz)
    {
        List<GuiOverride> list = Lists.newArrayList();

        for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : overrideMap.entrySet())
        {
            if (e.getValue() == clazz)
            {
                list.add(e.getKey());
            }
        }

        return list;
    }

    /**
     * Get a list of all the overrides of all the GUI class.
     *
     * @return the list with {@link GuiOverride} instances
     * @see #addOverride(Class, GuiOverride)
     * @see GuiOverride
     * @since 0.1.0
     */
    public static Map<GuiOverride, Class<? extends GuiScreen>> getOverrides()
    {
        return overrideMap;
    }
}