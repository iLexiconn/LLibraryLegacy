package net.ilexiconn.llibrary.common.color;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Color helper for Minecraft guis.
 *
 * @author RafaMv, Gegy1000
 */
public class ColorHelper
{
    private static Map<RGB, Integer> colorCache = Maps.newHashMap();

    /**
     * Searches a color based on alpha, red, blue, and green parameters.
     */
    public static int getColorInt(int r, int g, int b, int a)
    {
        int color = 0;

        Integer cached = colorCache.get(new RGB(r, g, b, a));

        if (cached == null)
        {
            int red = 0;
            int blue = 0;
            int green = 0;
            int alpha = 0;

            while (alpha != a || red != r || green != b || blue != g)
            {
                alpha = color >> 24 & 255;
                red = color >> 16 & 255;
                blue = color >> 8 & 255;
                green = color & 255;

                if (alpha < a) color += 50000;
                else if (red < r) color += 500;
                else if (blue < g) color += 25;
                else color += 1;
            }
        }
        else
        {
            return cached;
        }

        return color;
    }
}
