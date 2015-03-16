package test.render;

import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TestModelExtention implements IModelExtention
{
	private ModelRenderer box;
	
	public void init(ModelBase model)
	{
        box = new ModelRenderer(model, 0, 0);
        box.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
        box.setRotationPoint(5.0F, 18F, -5.0F);
	}

	public void setRotationAngles(ModelBase model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
	{
		if (model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			box.rotationPointX = modelBiped.bipedHead.rotationPointX;
            box.rotationPointY = modelBiped.bipedHead.rotationPointY - 8;
			box.rotationPointZ = modelBiped.bipedHead.rotationPointZ;
			box.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
			box.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		}
	}

	public void preRender(Entity entity, ModelBase model, float partialTicks)
	{
		if (model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			modelBiped.bipedHead.showModel = false;
		}
	}

	public void postRender(Entity entity, ModelBase model, float partialTicks)
	{
		if (model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			modelBiped.bipedHead.showModel = true;
		}
		
		box.render(partialTicks);
	}
}
