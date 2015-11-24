package net.ilexiconn.llibrary.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Render helper class for basic render operations and the IModelExtension.
 *
 * @author iLexiconn
 * @author Gegy1000
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class RenderHelper {
    /**
     * @since 0.2.0
     */
    public static void setColorFromInt(int color) {
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        GL11.glColor4f(r, g, b, 1f);
    }
}
