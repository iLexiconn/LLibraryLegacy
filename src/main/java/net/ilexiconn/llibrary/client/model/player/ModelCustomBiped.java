package net.ilexiconn.llibrary.client.model.player;

import java.util.ArrayList;
import java.util.List;

import net.ilexiconn.llibrary.client.render.IModelExtention;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class ModelCustomBiped extends ModelBiped
{
	public ModelCustomBiped()
	{
		super();

		List<IModelExtention> modelExtentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);

		if(modelExtentions != null)
		{
			for (IModelExtention extention : modelExtentions)
			{
				extention.initialize(this);
			}
		}
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6)
	{
		RenderHelper.modelBipedMain = this;

		List<IModelExtention> modelExtentions = RenderHelper.getModelExtentionsFor(ModelBiped.class);

		if(modelExtentions == null)
		{
			modelExtentions = new ArrayList<IModelExtention>();
		}

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(entity, this, f1, f2, f3, f4, f5, f6);
			extention.preRender(entity, this, f6);
		}

		super.render(entity, f1, f2, f3, f4, f5, f6);

		for (IModelExtention extention : modelExtentions)
		{
			extention.setRotationAngles(entity, this, f1, f2, f3, f4, f5, f6);
			extention.postRender(entity, this, f6);
		}
	}
}
