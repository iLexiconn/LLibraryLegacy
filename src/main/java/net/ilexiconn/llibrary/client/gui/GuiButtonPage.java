package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonPage extends GuiButton {
    public GuiScreen screen;

    public GuiButtonPage(int id, int x, int y, GuiScreen s) {
        super(id, x, y, 20, 20, id == -1 ? "<" : ">");
        screen = s;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (id == -1) {
                int currentPage = SurvivalTab.getCurrentPage();
                if (currentPage > 0) {
                    SurvivalTab.setCurrentPage(currentPage - 1);
                    initGui();
                }
            } else if (id == -2) {
                int currentPage = SurvivalTab.getCurrentPage();
                if (currentPage < SurvivalTab.getSurvivalTabList().size() / 11) {
                    SurvivalTab.setCurrentPage(currentPage + 1);
                    initGui();
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void initGui() {
        screen.initGui();
        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(screen, screen.buttonList));
    }
}
