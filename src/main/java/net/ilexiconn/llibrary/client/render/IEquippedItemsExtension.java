package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

/**
 * Interface for overriding {@link net.minecraft.client.renderer.entity.RenderPlayer#renderEquippedItems(net.minecraft.client.entity.AbstractClientPlayer, float)}.
 *
 * @author iLexiconn
 * @see net.minecraft.client.renderer.entity.RenderPlayer
 * @see net.minecraft.client.renderer.entity.RenderPlayer#renderEquippedItems(net.minecraft.client.entity.AbstractClientPlayer, float)
 * @since 0.2.0
 */
public interface IEquippedItemsExtension extends IExtension
{
    /**
     * @return true if continue rendering
     * @since 0.2.0
     */
    boolean preRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);

    void postRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);
}
