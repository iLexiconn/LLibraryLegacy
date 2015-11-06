package net.ilexiconn.llibrary.client.render.entity;

import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.ilexiconn.llibrary.client.render.entity.layer.LayerLLibraryArrow;
import net.ilexiconn.llibrary.common.event.RenderFirstPersonEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.render.RenderHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public final class RenderLLibraryPlayer extends RenderPlayer {
    public RenderLLibraryPlayer() {
        super(Minecraft.getMinecraft().getRenderManager());
        mainModel = new ModelLLibraryBiped();
        layerRenderers.clear();
        addLayer(new LayerBipedArmor(this));
        addLayer(new LayerHeldItem(this));
        addLayer(new LayerLLibraryArrow(this));
        addLayer(new LayerDeadmau5Head(this));
        addLayer(new LayerCape(this));
        addLayer(new LayerCustomHead(((ModelLLibraryBiped) mainModel).bipedHead));
    }

    public void func_177138_b(AbstractClientPlayer player) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Pre(player, this, ((ModelLLibraryBiped) mainModel)))) {
            super.func_177138_b(player);
        }
        MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Post(player, this, ((ModelLLibraryBiped) mainModel)));
    }
}