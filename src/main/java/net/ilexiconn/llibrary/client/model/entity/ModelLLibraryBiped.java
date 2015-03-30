package net.ilexiconn.llibrary.client.model.entity;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.IModelExtension;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public final class ModelLLibraryBiped extends ModelBiped
{
    public ModelLLibraryBiped()
    {
        List<IModelExtension> extensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);
        if (extensions != null) for (IModelExtension extension : extensions) extension.init(this);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
    {
        List<IModelExtension> modelExtensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);

        if (modelExtensions == null) modelExtensions = Lists.newArrayList();

        setRotationAngles(limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);

        for (IModelExtension extension : modelExtensions)
        {
            extension.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
            extension.preRender(entity, this, partialTicks);
        }

        if (isChild)
        {
            float scale = 2f;
            GL11.glPushMatrix();
            GL11.glScalef(1.5f / scale, 1.5f / scale, 1.5f / scale);
            GL11.glTranslatef(0f, 16f * partialTicks, 0f);
            bipedHead.render(partialTicks);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1f / scale, 1f / scale, 1f / scale);
            GL11.glTranslatef(0f, 24f * partialTicks, 0f);
            bipedBody.render(partialTicks);
            bipedRightArm.render(partialTicks);
            bipedLeftArm.render(partialTicks);
            bipedRightLeg.render(partialTicks);
            bipedLeftLeg.render(partialTicks);
            bipedHeadwear.render(partialTicks);
            GL11.glPopMatrix();
        }
        else
        {
            bipedHead.render(partialTicks);
            bipedBody.render(partialTicks);
            bipedRightArm.render(partialTicks);
            bipedLeftArm.render(partialTicks);
            bipedRightLeg.render(partialTicks);
            bipedLeftLeg.render(partialTicks);
            bipedHeadwear.render(partialTicks);
        }

        for (IModelExtension extension : modelExtensions)
        {
            extension.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
            extension.postRender(entity, this, partialTicks);
        }
    }
}
