package net.ilexiconn.llibrary.client.model.player;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.client.render.IModelExtension;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.List;

public final class ModelCustomBiped extends ModelBiped
{
	public ModelCustomBiped()
	{
		List<IModelExtension> extentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);
		if (extentions != null) for (IModelExtension extention : extentions) extention.init(this);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
	{
		if (Minecraft.getMinecraft().thePlayer == entity)
		{
			RenderHelper.modelBipedMain = this;
		}
		
		List<IModelExtension> modelExtentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);

		if (modelExtentions == null) modelExtentions = Lists.newArrayList();

		setRotationAngles(limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);

		for (IModelExtension extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.preRender(entity, this, partialTicks);
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

		for (IModelExtension extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.postRender(entity, this, partialTicks);
		}
	}
}
