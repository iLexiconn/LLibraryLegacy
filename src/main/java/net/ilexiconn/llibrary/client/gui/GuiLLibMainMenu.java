package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiLLibMainMenu extends GuiOverride
{
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(85, width / 2 - 100, 0, 200, 20, "Yay, GUI overrides! :D"));
    }
}