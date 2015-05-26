package net.ilexiconn.llibrary.common.color;

/**
 * @author      iLexiconn
 * @since       0.1.0
 */
public enum EnumChatColor
{
    BLACK("black"),
    DARK_BLUE("dark_blue"),
    DARK_GREEN("dark_green"),
    DARK_AQUA("dark_aqua"),
    DARK_RED("dark_red"),
    GOLD("gold"),
    GRAY("gray"),
    DARK_GRAY("dark_gray"),
    BLUE("blue"),
    GREEN("green"),
    AQUA("aqua"),
    RED("red"),
    LIGHT_PURPLE("light_purple"),
    YELLOW("yellow"),
    WHITE("white");

    public String colorCode;

    EnumChatColor(String c)
    {
        colorCode = c;
    }
}
