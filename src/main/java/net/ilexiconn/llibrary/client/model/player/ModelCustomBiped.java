package net.ilexiconn.llibrary.client.model.player;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import java.util.List;

public final class ModelCustomBiped extends ModelBiped
{
	public ModelCustomBiped()
	{
		List<IModelExtention> extentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);
		if (extentions != null) for (IModelExtention extention : extentions) extention.init(this);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
	{
		RenderHelper.modelBipedMain = this;
		List<IModelExtention> modelExtentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);

		if (modelExtentions == null) modelExtentions = Lists.newArrayList();

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.preRender(entity, this, partialTicks);
		}

		super.render(entity, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks);

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);
			extention.postRender(entity, this, partialTicks);
		}
	}
}
