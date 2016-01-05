package net.ilexiconn.llibrary.client.toast;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.gui.GuiToast;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Toast builder. Use this to send toast messages to the player. Can only be used on the CLIENT side.
 *
 * @author iLexiconn
 * @since 0.5.0
 */
@SideOnly(Side.CLIENT)
public class Toast {
    private static List<Toast> toastList = Lists.newArrayList();

    private GuiToast gui;
    private int posX;
    private int posY;
    private int duration;
    private int lastDuration;

    private Toast(int x, int y, int d) {
        posX = x;
        posY = y;
        duration = d;
        lastDuration = d;
    }

    /**
     * Initialize your toast. You can save the result to a field if you want to show the message multiple times.
     *
     * @param text The text to display. Every string is rendered on a new line.
     * @return The toast instance.
     */
    public static Toast makeText(String... text) {
        Toast toast = new Toast(10, 10, 60);
        int stringWidth = 0;
        for (String s : text) {
            stringWidth = Math.max(stringWidth, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        }
        toast.gui = new GuiToast(toast, stringWidth + 10, text);
        return toast;
    }

    /**
     * Set the toast's position. This can be set at any time. The default is x10, y10.
     *
     * @param x The x position.
     * @param y The y position.
     * @return The updated toast instance.
     */
    public Toast setPosition(int x, int y) {
        posX = x;
        posY = y;
        return this;
    }

    /**
     * Set the toast's duration. This can be set at any time. The default is d60 (3 seconds).
     *
     * @param d The duration.
     * @return The updated toast instance.
     */
    public Toast setDuration(int d) {
        duration = d;
        lastDuration = d;
        return this;
    }

    /**
     * Display the toast. The duration will be reset if the toast was already shown.
     *
     * @return The updated toast instance.
     */
    public Toast show() {
        duration = lastDuration;
        toastList.add(this);
        return this;
    }

    /**
     * Get the toast's x position.
     *
     * @return The toast's x position.
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Get the toast's y position.
     *
     * @return The toast's y position.
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Get the duration left.
     *
     * @return The duration left.
     */
    public int getDuration() {
        return duration;
    }

    /* =========================================== FOR INTERNAL USE ONLY =========================================== */

    /**
     * For internal use only.
     */
    public static List<Toast> getToastList() {
        return toastList;
    }

    /**
     * For internal use only.
     */
    public int tick() {
        return duration--;
    }

    /**
     * For internal use only.
     */
    public GuiToast getGui() {
        return gui;
    }
}
