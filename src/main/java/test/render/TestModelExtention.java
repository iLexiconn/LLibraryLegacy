package test.render;

import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TestModelExtention implements IModelExtention
{
	public void init(ModelBase model)
	{
	}

	public void setRotationAngles(ModelBase model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
	{
		if (model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			modelBiped.bipedHead.rotationPointY -= 8;
		}
	}

	public void preRender(Entity entity, ModelBase model, float partialTicks)
	{
	}

	public void postRender(Entity entity, ModelBase model, float partialTicks)
	{
	}
}
