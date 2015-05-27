package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiLLibraryConfig extends GuiScreen
{
    public GuiScreen parent;

    public GuiLLibraryConfig(GuiScreen p)
    {
        parent = p;
    }
}
