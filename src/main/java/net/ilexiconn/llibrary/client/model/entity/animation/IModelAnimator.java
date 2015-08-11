package net.ilexiconn.llibrary.client.model.entity.animation;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.minecraft.entity.Entity;

/**
 * Interface for animating Tabula models.
 * <p>
 * This can be used for Living animations.
 * 
 * @author Gegy1000
 * @since 0.1.0
 */
public interface IModelAnimator
{
    /**
     * Set the rotation angles for the shapes. Called every tick.
     * 
     * @see net.ilexiconn.llibrary.client.model.tabula.ModelJson
     * @since 0.1.0
     */
    void setRotationAngles(ModelJson model, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity);
}
