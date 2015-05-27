package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for overriding {@link net.minecraft.client.renderer.entity.RenderPlayer#renderFirstPersonArm(net.minecraft.entity.player.EntityPlayer)}.
 * 
 * @see net.minecraft.client.renderer.entity.RenderPlayer
 * @see net.minecraft.client.renderer.entity.RenderPlayer#renderFirstPersonArm(net.minecraft.entity.player.EntityPlayer)
 * @author iLexiconn
 * @since 0.2.0
 */
@SideOnly(Side.CLIENT)
public interface IFirstPersonExtension extends IExtension
{
    /**
     * @since 0.2.0
     * @return true if continue rendering
     */
    boolean preRenderFirstPerson(EntityPlayer entity, RenderPlayer renderPlayer, ModelBiped model);

    void postRenderFirstPerson(EntityPlayer entity, RenderPlayer renderPlayer, ModelBiped model);
}
