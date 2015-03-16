package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public interface IModelExtention
{
	/**
	 * Initialize your extra ModelRenderers here.
	 */
	void initialize(ModelBase model);
	
	/**
	 * Used to set the rotation of ModelRenderers before rendering the actual model.
	 */
	void setRotationAngles(Entity entity, ModelBase model, float f1, float f2, float f3, float f4, float f5, float f6);
	
	/**
	 * Called before rendering the model.
	 */
	void preRender(Entity entity, ModelBase model, float f);

	/**
	 * Called after rendering the model.
	 */
	void postRender(Entity entity, ModelBase model, float f);
}
