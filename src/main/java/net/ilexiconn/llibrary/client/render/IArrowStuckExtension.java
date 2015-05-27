package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for overriding {@link net.minecraft.client.renderer.entity.RenderPlayer#renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase, float)}.
 * 
 * @see net.minecraft.client.renderer.entity.RenderPlayer
 * @see net.minecraft.client.renderer.entity.RenderPlayer#renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase, float)
 * @author iLexiconn
 * @since 0.2.0
 */
public interface IArrowStuckExtension extends IExtension
{
    /**
     * @since 0.2.0
     * @return true if continue rendering
     */
    boolean preRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);

    void postRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);
}
