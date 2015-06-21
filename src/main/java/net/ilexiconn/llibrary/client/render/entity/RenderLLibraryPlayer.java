package net.ilexiconn.llibrary.client.render.entity;

import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.render.RenderHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public final class RenderLLibraryPlayer extends RenderPlayer
{
    public RenderLLibraryPlayer()
    {
        super(Minecraft.getMinecraft().getRenderManager());
        mainModel = new ModelLLibraryBiped();
    }
}