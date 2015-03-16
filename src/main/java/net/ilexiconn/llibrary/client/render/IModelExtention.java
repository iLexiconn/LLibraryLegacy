package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public interface IModelExtention
{
	void init(ModelBase model);
	
	void setRotationAngles(ModelBase model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity);
	
	void preRender(Entity entity, ModelBase model, float partialTicks);

	void postRender(Entity entity, ModelBase model, float partialTicks);
}
