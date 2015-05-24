package net.ilexiconn.llibrary.client.render;

import net.minecraft.entity.EntityLivingBase;

/**
 * Interface for overriding renderArrowsStuckInEntity().
 *
 * @author iLexiconn
 */
public interface IArrowStuckExtension extends IExtension
{
    void preRenderArrowsStuckInEntity(EntityLivingBase entity, float partialTicks);

    void postRenderArrowsStuckInEntity(EntityLivingBase entity, float partialTicks);
}
