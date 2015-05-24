package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for overriding renderArrowsStuckInEntity().
 *
 * @author iLexiconn
 */
public interface IArrowStuckExtension extends IExtension
{
    boolean preRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);

    void postRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);
}
