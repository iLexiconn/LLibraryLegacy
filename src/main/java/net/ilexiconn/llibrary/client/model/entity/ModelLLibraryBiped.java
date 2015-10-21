package net.ilexiconn.llibrary.client.model.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.event.InitializePlayerModelEvent;
import net.ilexiconn.llibrary.common.event.RenderPlayerModelEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

/**
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.render.RenderHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public final class ModelLLibraryBiped extends ModelBiped
{
    public ModelLLibraryBiped()
    {
        MinecraftForge.EVENT_BUS.post(new InitializePlayerModelEvent(this));
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks)
    {
        setRotationAngles(limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity);

        if (MinecraftForge.EVENT_BUS.post(new RenderPlayerModelEvent.Pre(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity))) return;

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

        MinecraftForge.EVENT_BUS.post(new RenderPlayerModelEvent.Post(this, limbSwing, limbSwingAmount, rotationFloat, rotationYaw, rotationPitch, partialTicks, entity));
    }
}
