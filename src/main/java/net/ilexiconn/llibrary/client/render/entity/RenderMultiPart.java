package net.ilexiconn.llibrary.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

/**
 * Basic class for debugging entity classes with MultiParts
 *
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public abstract class RenderMultiPart extends RenderLiving
{
    public RenderMultiPart(ModelBase model, float shadow)
    {
        super(model, shadow);
    }

    public void doRender(EntityLiving entity, double x, double y, double z, float rotationYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, rotationYaw, partialTicks);
        doRender((IEntityMultiPart) entity, x, y, z, rotationYaw, partialTicks);
    }

    public void doRender(IEntityMultiPart entity, double x, double y, double z, float rotationYaw, float partialTicks)
    {
        if (RenderManager.debugBoundingBox)
        {
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            for (EntityPart e : entity.getParts())
                RenderGlobal.drawOutlinedBoundingBox(e.boundingBox.copy().offset(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ), 0xffffff);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
        }
    }
}
