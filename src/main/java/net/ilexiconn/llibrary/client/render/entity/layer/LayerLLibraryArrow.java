package net.ilexiconn.llibrary.client.render.entity.layer;

import java.util.Random;

import net.ilexiconn.llibrary.common.event.RenderStuckArrowEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerLLibraryArrow implements LayerRenderer<AbstractClientPlayer> {
    public RenderPlayer renderer;

    public LayerLLibraryArrow(RenderPlayer r) {
        renderer = r;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entity, float f8, float f7, float partialTicks, float f5, float f4, float f9, float size) {
        if (MinecraftForge.EVENT_BUS.post(new RenderStuckArrowEvent.Pre(entity, renderer, partialTicks))) {
            return;
        }

        int i = entity.getArrowCountInEntity();

        if (i > 0) {
            EntityArrow entityarrow = new EntityArrow(entity.worldObj, entity.posX, entity.posY, entity.posZ);
            Random random = new Random(entity.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                ModelRenderer modelrenderer = this.renderer.getMainModel().getRandomModelBox(random);
                ModelBox modelbox = modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625F);
                float f14 = random.nextFloat();
                float f15 = random.nextFloat();
                float f16 = random.nextFloat();
                float f10 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f14) / 16.0F;
                float f11 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f15) / 16.0F;
                float f12 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f16) / 16.0F;
                GlStateManager.translate(f10, f11, f12);
                f14 = f14 * 2.0F - 1.0F;
                f15 = f15 * 2.0F - 1.0F;
                f16 = f16 * 2.0F - 1.0F;
                f14 *= -1.0F;
                f15 *= -1.0F;
                f16 *= -1.0F;
                float f13 = MathHelper.sqrt_float(f14 * f14 + f16 * f16);
                entityarrow.prevRotationYaw = entityarrow.rotationYaw = (float) (Math.atan2(f14, f16) * 180.0D / Math.PI);
                entityarrow.prevRotationPitch = entityarrow.rotationPitch = (float) (Math.atan2(f15, f13) * 180.0D / Math.PI);
                double d0 = 0.0D;
                double d1 = 0.0D;
                double d2 = 0.0D;
                this.renderer.getRenderManager().renderEntityWithPosYaw(entityarrow, d0, d1, d2, 0.0F, partialTicks);
                GlStateManager.popMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}