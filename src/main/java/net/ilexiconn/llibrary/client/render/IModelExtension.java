package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for rendering extra models to {@link net.minecraft.client.model.ModelBiped}.
 * 
 * @author Gegy1000
 * @see net.minecraft.client.model.ModelBiped
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public interface IModelExtension extends IExtension
{
    /**
     * Method to set the rotation angles for boxes before rendering.
     * 
     * @since 0.1.0
     */
    void setRotationAngles(ModelBiped model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity);

    /**
     * Render method called before rendering the parent model.
     * 
     * @param entity
     *            the parent entity
     * @param model
     *            the parent model
     * @since 0.1.0
     */
    void preRender(EntityPlayer entity, ModelBase model, float partialTicks);

    /**
     * Render method called after rendering the parent model.
     * 
     * @param entity
     *            the parent entity
     * @param model
     *            the parent model
     * @since 0.1.0
     */
    void postRender(EntityPlayer entity, ModelBase model, float partialTicks);
}
