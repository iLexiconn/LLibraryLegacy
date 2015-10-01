package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
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
    private static Map<GuiOverride, Class<? extends GuiScreen>> overrideMap = Maps.newHashMap();
    private static List<GuiToast> toasts = Lists.newArrayList();

    /**
     * A method for adding {@link net.ilexiconn.llibrary.client.gui.GuiOverride} to an existing {@link net.minecraft.client.gui.GuiScreen} or {@link net.minecraft.client.gui.inventory.GuiContainer} {@link net.ilexiconn.llibrary.client.gui.GuiOverride} classes may get added twice.
     * 
     * @see #getOverridesForGui(java.lang.Class)
     * @see net.ilexiconn.llibrary.client.gui.GuiOverride
     * @since 0.1.0
     */
    public static void addOverride(Class<? extends GuiScreen> clazz, GuiOverride gui)
    {
        overrideMap.put(gui, clazz);
    }

    /**
     * Display a toast notification with the given text.
     * 
     * @since 0.3.0
     */
    public static void createToast(int x, int y, String... text)
    {
        int stringWidth = 0;
        for (String s : text)
            stringWidth = Math.max(stringWidth, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
        toasts.add(new GuiToast(x, y, stringWidth + 10, stringWidth * 3, text));
    }

    public static List<GuiToast> getToasts()
    {
        return toasts;
    }

    /**
     * Get a list of all the overrides of a specific GUI class.
     * 
     * @return the list with {@link net.ilexiconn.llibrary.client.gui.GuiOverride} instances
     * @see #addOverride(java.lang.Class, net.ilexiconn.llibrary.client.gui.GuiOverride)
     * @see net.ilexiconn.llibrary.client.gui.GuiOverride
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
     * @return the list with {@link net.ilexiconn.llibrary.client.gui.GuiOverride} instances
     * @see #addOverride(java.lang.Class, net.ilexiconn.llibrary.client.gui.GuiOverride)
     * @see net.ilexiconn.llibrary.client.gui.GuiOverride
     * @since 0.1.0
     */
    public static Map<GuiOverride, Class<? extends GuiScreen>> getOverrides()
    {
        return overrideMap;
    }
}