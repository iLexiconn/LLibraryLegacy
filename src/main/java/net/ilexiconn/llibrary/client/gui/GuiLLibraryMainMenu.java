package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
            mc.displayGuiScreen(new GuiPickItem("test")
            {
                public void onSelectEntry(ItemStack itemstack, EntityPlayer player)
                {
                    mc.displayGuiScreen(this);
                }
            });
        }
    }
}