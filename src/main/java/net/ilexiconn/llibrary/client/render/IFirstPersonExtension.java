package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for rendering extra models to first-person.
 *
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public interface IFirstPersonExtension extends IExtension
{
    boolean preRenderFirstPerson(EntityPlayer entity, RenderPlayer renderPlayer, ModelBiped model);

    void postRenderFirstPerson(EntityPlayer entity, RenderPlayer renderPlayer, ModelBiped model);
}
