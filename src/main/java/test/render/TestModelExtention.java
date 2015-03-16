package test.render;

import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TestModelExtention implements IModelExtention
{
	private ModelRenderer box;
	
	@Override
	public void initialize(ModelBase model) 
	{
        this.box = new ModelRenderer(model, 0, 0);
        this.box.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
        this.box.setRotationPoint(5.0F, 18F, -5.0F);
	}

	@Override
	public void setRotationAngles(Entity entity, ModelBase model, float f1, float f2, float f3, float f4, float f5, float f6) 
	{
		if(model instanceof ModelBiped)
		{
			//FAKE HEAD :O
			ModelBiped modelBiped = (ModelBiped) model;
			box.rotationPointX = modelBiped.bipedHead.rotationPointX;
			box.rotationPointY = modelBiped.bipedHead.rotationPointY;
			box.rotationPointZ = modelBiped.bipedHead.rotationPointZ;
			box.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
			box.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		}
	}

	@Override
	public void preRender(Entity entity, ModelBase model, float f) 
	{
		if(model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			modelBiped.bipedHead.showModel = false;
		}
	}

	@Override
	public void postRender(Entity entity, ModelBase model, float f)
	{
		if(model instanceof ModelBiped)
		{
			ModelBiped modelBiped = (ModelBiped) model;
			modelBiped.bipedHead.showModel = true;
		}
		
		box.render(f);
	}
}
