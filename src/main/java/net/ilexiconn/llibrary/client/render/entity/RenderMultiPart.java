package net.ilexiconn.llibrary.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.ilexiconn.llibrary.common.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic class for debugging entity classes with {@link net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart}.
 *
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.entity.multipart.EntityPart
 * @see net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public abstract class RenderMultiPart<T extends EntityLiving> extends RenderLiving<T> {
    public RenderMultiPart(ModelBase model, float shadow) {
        super(Minecraft.getMinecraft().getRenderManager(), model, shadow);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float rotationYaw, float partialTicks) {
        super.doRender(entity, x, y, z, rotationYaw, partialTicks);
        doRender((IEntityMultiPart) entity, x, y, z, rotationYaw, partialTicks);
    }

    public void doRender(IEntityMultiPart entity, double x, double y, double z, float rotationYaw, float partialTicks) {
        if (renderManager.isDebugBoundingBox()) {
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            for (EntityPart e : entity.getParts()) {
                RenderGlobal.drawSelectionBoundingBox(e.getBoundingBox().offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ));
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
        }
    }
}
