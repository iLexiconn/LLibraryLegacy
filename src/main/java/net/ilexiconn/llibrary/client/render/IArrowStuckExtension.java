package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for overriding {@link net.minecraft.client.renderer.entity.RenderPlayer#renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase, float)}.
 * 
 * @author iLexiconn
 * @see net.minecraft.client.renderer.entity.RenderPlayer
 * @see net.minecraft.client.renderer.entity.RenderPlayer#renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase, float)
 * @since 0.2.0
 */
public interface IArrowStuckExtension extends IExtension
{
    /**
     * @return true if continue rendering
     * @since 0.2.0
     */
    boolean preRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);

    void postRenderArrowsStuckInEntity(EntityLivingBase entity, RenderPlayer renderPlayer, float partialTicks);
}
