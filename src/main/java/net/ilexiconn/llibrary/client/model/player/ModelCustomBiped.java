package net.ilexiconn.llibrary.client.model.player;

import com.google.common.collect.Lists;

import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import java.util.List;

import org.lwjgl.opengl.GL11;

public final class ModelCustomBiped extends ModelBiped
{
	public ModelCustomBiped()
	{
		List<IModelExtention> extentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);
		if (extentions != null) for (IModelExtention extention : extentions) extention.init(this);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
	{
		if(Minecraft.getMinecraft().thePlayer == entity)
		{
			RenderHelper.modelBipedMain = this;
		}
		
		List<IModelExtention> modelExtentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);

		if (modelExtentions == null) modelExtentions = Lists.newArrayList();

		this.setRotationAngles(limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.preRender(entity, this, partialTicks);
		}
		
		if (this.isChild)
		{
			float scale = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / scale, 1.5F / scale, 1.5F / scale);
			GL11.glTranslatef(0.0F, 16.0F * partialTicks, 0.0F);
			this.bipedHead.render(partialTicks);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
			GL11.glTranslatef(0.0F, 24.0F * partialTicks, 0.0F);
			this.bipedBody.render(partialTicks);
			this.bipedRightArm.render(partialTicks);
			this.bipedLeftArm.render(partialTicks);
			this.bipedRightLeg.render(partialTicks);
			this.bipedLeftLeg.render(partialTicks);
			this.bipedHeadwear.render(partialTicks);
			GL11.glPopMatrix();
		}
		else
		{
			this.bipedHead.render(partialTicks);
			this.bipedBody.render(partialTicks);
			this.bipedRightArm.render(partialTicks);
			this.bipedLeftArm.render(partialTicks);
			this.bipedRightLeg.render(partialTicks);
			this.bipedLeftLeg.render(partialTicks);
			this.bipedHeadwear.render(partialTicks);
		}

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.postRender(entity, this, partialTicks);
		}
	}
}
