package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

/**
 * Interface for overriding renderEquippedItems().
 *
 * @author iLexiconn
 */
public interface IEquippedItemsExtension extends IExtension
{
    boolean preRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);

    void postRenderEquippedItems(AbstractClientPlayer player, RenderPlayer renderPlayer, float partialTicks);
}
