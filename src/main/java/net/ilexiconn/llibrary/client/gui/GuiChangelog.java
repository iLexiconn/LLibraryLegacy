package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChangelog extends GuiScreen
{
    public int verticalScroll = 0;
    public int horizontalScroll = 0;
    private final ModUpdateContainer mod;
    private final String version;
    private final String[] changelog;

    public GuiChangelog(ModUpdateContainer mod, String version, String[] changelog)
    {
        super();
        this.mod = mod;
        this.version = version;
        this.changelog = changelog;
    }

    public void init()
    {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 60, 200, 20, "Done"));
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {

        }
    }

    protected void keyTyped(char character, int par2)
    {
        super.keyTyped(character, par2);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void onGuiClosed()
    {

    }

    public int getMouseWheel()
    {
        int speed = Mouse.getDWheel();

        if (speed != 0)
        {
            if (speed > 1)
            {
                speed = 1;
            }
            if (speed < -1)
            {
                speed = -1;
            }

            verticalScroll += !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -(speed * 10) : 0;
            horizontalScroll += Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -(speed * 10) : 0;

            if (verticalScroll > 225)
            {
                verticalScroll = 225;
            }
            else if (verticalScroll < -80)
            {
                verticalScroll = -80;
            }

            if (horizontalScroll > 150)
            {
                horizontalScroll = 150;
            }
            else if (horizontalScroll < -150)
            {
                horizontalScroll = -150;
            }

            speed = 0;
        }

        return speed;
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        getMouseWheel();
        drawChangelog(this, fontRendererObj, changelog, horizontalScroll, verticalScroll, version, mod);
        
        drawString(fontRendererObj, "Press 'ESC' to exit.", 5, 5, 0xffffff);
        drawString(fontRendererObj, "Mouse Wheel to scroll up/down.", width - 220, height - 15, 0xffffff);
        drawString(fontRendererObj, "Mouse Wheel + 'SHIFT' to scroll left/right.", width - 220, height - 25, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }

	public static void drawChangelog(GuiScreen parent, FontRenderer fontRenderer, String[] changelog, int offsetX, int offsetY, String version, ModUpdateContainer mod)
	{
		int i = offsetY;
        int j = offsetX;
        int xSize = 0;
        int ySize = 0;

        for (int k = 0; k < changelog.length; ++k)
        {
            if (changelog[k] != null)
            {
                xSize = fontRenderer.getStringWidth(changelog[k]) > xSize ? fontRenderer.getStringWidth(changelog[k]) : xSize;
                ySize = (k + 1) * 10 > ySize ? (k + 1) * 10 : ySize;
            }
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0, 0, 0, 0.35F);
        parent.drawTexturedModalRect(j + parent.width / 2 - 201, i + parent.height / 2 - 110, 0, 0, 405, 33);
        parent.drawTexturedModalRect(j + parent.width / 2 - 201, i + parent.height / 2 - 72, 0, 0, xSize > 405 ? xSize + 20 : 405, ySize + 15);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        parent.drawCenteredString(fontRenderer, "(" + mod.modid + ") " + mod.name + ": " + version, j + parent.width / 2, i + parent.height / 2 - 97, 0xffffff);

        for (int k = 0; k < changelog.length; ++k)
        {
            if (changelog[k] != null)
            {
            	parent.drawString(fontRenderer, changelog[k], j + parent.width / 2 - 190, i + parent.height / 2 - 65 + k * 10, 0xffffff);
            }
        }
	}
}