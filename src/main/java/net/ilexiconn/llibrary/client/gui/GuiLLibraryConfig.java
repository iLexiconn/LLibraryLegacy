package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
