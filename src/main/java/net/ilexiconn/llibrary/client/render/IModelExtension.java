package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for rendering extra models to existing models.
 *
 * @author Gegy1000
 */
@SideOnly(Side.CLIENT)
public interface IModelExtension extends IExtension
{
    /**
     * Method to set the rotation angles for boxes before rendering.
     *
     * @param model
     * @param limbSwing
     * @param limbSwingAmount
     * @param rotationFloat
     * @param rotationYaw
     * @param rotationPitch
     * @param partialTicks
     * @param entity
     */
    void setRotationAngles(ModelBiped model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity);

    /**
     * Render method called before rendering the parent model.
     *
     * @param entity       the parent entity
     * @param model        the parent model
     * @param partialTicks
     */
    void preRender(EntityPlayer entity, ModelBase model, float partialTicks);

    /**
     * Render method called after rendering the parent model.
     *
     * @param entity       the parent entity
     * @param model        the parent model
     * @param partialTicks
     */
    void postRender(EntityPlayer entity, ModelBase model, float partialTicks);
}
