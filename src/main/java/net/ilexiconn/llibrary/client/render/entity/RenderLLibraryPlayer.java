package net.ilexiconn.llibrary.client.render.entity;

import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.ilexiconn.llibrary.client.render.entity.layer.LayerLLibraryArrow;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.event.RenderFirstPersonEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author iLexiconn
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.render.RenderHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public final class RenderLLibraryPlayer extends RenderPlayer {
    public Minecraft mc = Minecraft.getMinecraft();

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
    
    @Override
    public void renderRightArm(AbstractClientPlayer player) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Pre(player, this, ((ModelLLibraryBiped) mainModel)))) {
            super.renderRightArm(player);
        }
        MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Post(player, this, ((ModelLLibraryBiped) mainModel)));
    }

    @Override
    public void renderLivingAt(AbstractClientPlayer player, double x, double y, double z) {
        if (player.isEntityAlive() && player.isPlayerSleeping()) {
            applyTranslation(player, x + player.renderOffsetX, y + player.renderOffsetY, z + player.renderOffsetZ);
        } else {
            applyTranslation(player, x, y, z);
        }
    }

    public void applyTranslation(AbstractClientPlayer player, double x, double y, double z) {
        float scale = EntityHelper.getScale(player);

        if (player == mc.thePlayer) {
            GL11.glTranslatef(0, (1.62f) * (scale - 1), 0);
        }

        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(0, (float) (player.getYOffset() - (player == mc.thePlayer ? 1.62f : 0)), 0);
        GL11.glTranslatef((float) x, (float) y, (float) z);
    }
}