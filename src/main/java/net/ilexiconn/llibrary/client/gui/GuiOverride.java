package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author FiskFille
 */
public class GuiOverride extends GuiScreen
{
    public Minecraft mc = Minecraft.getMinecraft();
    public Random rand = new Random();
    public GuiScreen overriddenScreen;

    public List buttonList = new ArrayList();
    private int eventButton;
    private long lastMouseEvent;
    private int field_146298_h;

    public void drawScreen(int mouseX, int mouseY, float partalTicks)
    {
        super.drawScreen(mouseX, mouseY, partalTicks);
//		List buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, overriddenScreen, "buttonList", "field_146292_n");
        List labelList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, overriddenScreen, "labelList", "field_146293_o");
        int k1;

        for (k1 = 0; k1 < buttonList.size(); ++k1)
        {
            ((GuiButton) buttonList.get(k1)).drawButton(mc, mouseX, mouseY);
        }

        for (k1 = 0; k1 < labelList.size(); ++k1)
        {
            ((GuiLabel) labelList.get(k1)).drawLabel(mc, mouseX, mouseY);
        }
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        this.mc = mc;
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        if (!MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Pre(this, this.buttonList)))
        {
            this.buttonList.clear();
            this.initGui();
        }
        MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Post(this, this.buttonList));
    }

    public void actionPerformed(GuiButton button)
    {
    }
}