package net.ilexiconn.llibrary.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.event.RenderFirstPersonEvent;
import net.ilexiconn.llibrary.common.event.RenderStuckArrowEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

/**
 * @author iLexiconn
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.render.RenderHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public final class RenderLLibraryPlayer extends RenderPlayer
{
    public Minecraft mc = Minecraft.getMinecraft();

    public RenderLLibraryPlayer()
    {
        setRenderManager(RenderManager.instance);
        mainModel = new ModelLLibraryBiped();
        modelBipedMain = (ModelBiped) mainModel;
    }

    public void renderFirstPersonArm(EntityPlayer player)
    {
        if (!MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Pre(player, this, modelBipedMain)))
            super.renderFirstPersonArm(player);
        MinecraftForge.EVENT_BUS.post(new RenderFirstPersonEvent.Post(player, this, modelBipedMain));
    }

    public void renderArrowsStuckInEntity(EntityLivingBase entity, float partialTicks)
    {
        if (!MinecraftForge.EVENT_BUS.post(new RenderStuckArrowEvent.Pre(entity, this, partialTicks)))
            super.renderArrowsStuckInEntity(entity, partialTicks);
        MinecraftForge.EVENT_BUS.post(new RenderStuckArrowEvent.Post(entity, this, partialTicks));
    }

    public void renderLivingAt(AbstractClientPlayer player, double x, double y, double z)
    {
        if (player.isEntityAlive() && player.isPlayerSleeping())
            applyTranslation(player, x + (double) player.field_71079_bU, y + (double) player.field_71082_cx, z + (double) player.field_71089_bV);
        else applyTranslation(player, x, y, z);
    }

    public void applyTranslation(AbstractClientPlayer player, double x, double y, double z)
    {
        float scale = EntityHelper.getScale(player);

        if (player == mc.thePlayer)
        {
            GL11.glTranslatef(0, (1.62f) * (scale - 1), 0);
        }

        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(0, player.yOffset - (player == mc.thePlayer ? 1.62f : 0), 0);
        GL11.glTranslatef((float) x, (float) y, (float) z);
    }
}
