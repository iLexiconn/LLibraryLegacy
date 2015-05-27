package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;

/**
 * @see net.ilexiconn.llibrary.client.gui.GuiHelper
 * @author FiskFile
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiLLibraryMainMenu extends GuiOverride
{
    private GuiButtonCheckForUpdates buttonCheckForUpdates;

    public void initGui()
    {
        super.initGui();
        buttonList.add(buttonCheckForUpdates = new GuiButtonCheckForUpdates(85, width / 2 - 124, height / 4 + 48));
    }

    public void updateScreen()
    {
        buttonCheckForUpdates.update();
        buttonCheckForUpdates.screenWidth = width;
        buttonCheckForUpdates.screenHeight = height;
    }

    public void actionPerformed(GuiButton button)
    {
        int id = button.id;

        if (id == 85)
        {
            mc.displayGuiScreen(new GuiCheckForUpdates());
        }
    }
}