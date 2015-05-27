package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

/**
 * Interface for overriding {@link net.minecraft.client.renderer.entity.RenderPlayer#renderEquippedItems(net.minecraft.client.entity.AbstractClientPlayer, float)}.
 * 
 * @see net.minecraft.client.renderer.entity.RenderPlayer
 * @see net.minecraft.client.renderer.entity.RenderPlayer#renderEquippedItems(net.minecraft.client.entity.AbstractClientPlayer, float)
 * @author iLexiconn
 * @since 0.2.0
 */
public interface IEquippedItemsExtension extends IExtension
{
    /**
     * @since 0.2.0
     * @return true if continue rendering
     */
    boolean preRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);

    void postRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);
}
