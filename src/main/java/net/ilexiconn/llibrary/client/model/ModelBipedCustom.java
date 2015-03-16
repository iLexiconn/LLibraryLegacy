package net.ilexiconn.llibrary.client.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBipedCustom extends ModelBiped
{
	private List<ModelRenderer> extraPieces = new ArrayList<ModelRenderer>();
	
	/**
	 * Adds the specified ModelRenderer to the model.
	 */
	public void addBox(ModelRenderer modelRenderer)
	{
		if(!extraPieces.contains(modelRenderer))
		{
			extraPieces.add(modelRenderer);
		}
	}
	
	/**
	 * Add's a child to an existing model piece.
	 */
	public void addChild(ModelRenderer parent, ModelRenderer child)
	{
		parent.addChild(child);
	}
	
	 /**
      * Sets the models various rotation angles then renders the model.
      */
    public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6)
    {
        this.setRotationAngles(f1, f2, f3, f4, f5, f6, entity);

        if (this.isChild)
        {
            float scale = 2.0F;
            
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / scale, 1.5F / scale, 1.5F / scale);
            GL11.glTranslatef(0.0F, 16.0F * f6, 0.0F);
            this.bipedHead.render(f6);
            GL11.glPopMatrix();
            
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
            GL11.glTranslatef(0.0F, 24.0F * f6, 0.0F);
            
            this.bipedBody.render(f6);
            this.bipedRightArm.render(f6);
            this.bipedLeftArm.render(f6);
            this.bipedRightLeg.render(f6);
            this.bipedLeftLeg.render(f6);
            this.bipedHeadwear.render(f6);
            
            GL11.glPopMatrix();
        }
        else
        {
            this.bipedHead.render(f6);
            this.bipedBody.render(f6);
            this.bipedRightArm.render(f6);
            this.bipedLeftArm.render(f6);
            this.bipedRightLeg.render(f6);
            this.bipedLeftLeg.render(f6);
            this.bipedHeadwear.render(f6);
        }
        
        for (ModelRenderer box : extraPieces)
        {
			box.render(f6);
		}
    }
	
    /**
     * Hides the specified ModelRenderer.
     */
	public void hideBox(ModelRenderer renderer)
	{
		renderer.showModel = false;
	}
	
	/**
	 * Shows the specified ModelRenderer.
	 */
	public void showBox(ModelRenderer renderer)
	{
		renderer.showModel = true;
	}
}
