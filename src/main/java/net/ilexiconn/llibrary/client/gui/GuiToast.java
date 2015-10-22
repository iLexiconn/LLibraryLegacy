package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.api.Toast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiToast extends Gui
{
    public Toast toast;
    public int width;
    public int height;
    public List<String> message;

    public GuiToast(Toast g, int w, String... m)
    {
        toast = g;
        width = w;
        message = Arrays.asList(m);
        height = message.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 7;
    }

    public void drawToast()
    {
        if (toast.getDuration() > 0)
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            int opacity = (int) (toast.getDuration() * 256f / 25f);
            if (opacity > 255)
                opacity = 255;

            if (opacity > 0)
            {
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                drawBackdrop(toast.getPosX(), toast.getPosY(), width, height, opacity);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                int color = 0xffffff | (opacity << 24);
                for (int i = 0; i < message.size(); i++)
                {
                    String s = message.get(i);
                    fontRenderer.drawStringWithShadow(s, (toast.getPosX() + width / 2) - (fontRenderer.getStringWidth(s) / 2), toast.getPosY() + 4 + (fontRenderer.FONT_HEIGHT * i), color);
                }
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(0F, 0F, 0F, 1F);
                GL11.glPopMatrix();
            }
        }
    }

    protected void drawBackdrop(int x, int y, int width, int height, int opacity)
    {
        int color = (opacity << 24);
        drawRect(x + 1, y, x + width - 1, y + height, color);
        drawRect(x, y + 1, x + 1, y + height - 1, color);
        drawRect(x + width - 1, y + 1, x + width, y + height - 1, color);

        color = 0x28025c | (opacity << 24);
        drawRect(x + 1, y + 1, x + width - 1, y + 2, color);
        drawRect(x + 1, y + height - 1, x + width - 1, y + height - 2, color);
        drawRect(x + 1, y + 1, x + 2, y + height - 1, color);
        drawRect(x + width - 1, y + 1, x + width - 2, y + height - 1, color);
    }
}