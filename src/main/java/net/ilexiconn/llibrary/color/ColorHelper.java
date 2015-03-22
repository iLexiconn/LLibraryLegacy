package net.ilexiconn.llibrary.color;

/**
 * Color helper for Minecraft guis.
 *
 * @author RafaMv
 */
public class ColorHelper
{
    /**
     * Searches a color based on alpha, red, blue, and green parameters.
     * <p/>
     * Note: For performance sake, the return value of this function SHOULD be cached.
     * Two calls with the same values SHOULD return the same value.
     */
    public static int getColorInt(int red_wanted, int blue_wanted, int green_wanted, int alpha_wanted)
    {
        int color = 0;
        int red = 0;
        int blue = 0;
        int green = 0;
        int alpha = 0;

        while (alpha != alpha_wanted || red != red_wanted || green != green_wanted || blue != blue_wanted)
        {
            alpha = color >> 24 & 255;
            red = color >> 16 & 255;
            blue = color >> 8 & 255;
            green = color & 255;

            if (alpha < alpha_wanted) color += 50000;
            else if (red < red_wanted) color += 500;
            else if (blue < blue_wanted) color += 25;
            else color += 1;
        }

        return color;
    }
}
