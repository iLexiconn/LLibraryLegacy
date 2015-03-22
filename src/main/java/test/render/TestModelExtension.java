package test.render;

import net.ilexiconn.llibrary.client.render.IModelExtension;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class TestModelExtension implements IModelExtension
{
    public void init(ModelBase model)
    {

    }

    public void setRotationAngles(ModelBase model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        if (model instanceof ModelBiped)
        {
            ModelBiped modelBiped = (ModelBiped) model;
            modelBiped.bipedHead.rotationPointY -= 2;
            modelBiped.bipedLeftArm.rotationPointX += 2;
            modelBiped.bipedRightArm.rotationPointX -= 2;
        }
    }

    public void preRender(Entity entity, ModelBase model, float partialTicks)
    {

    }

    public void postRender(Entity entity, ModelBase model, float partialTicks)
    {

    }
}
