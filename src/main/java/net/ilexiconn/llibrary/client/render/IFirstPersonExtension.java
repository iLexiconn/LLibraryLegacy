package net.ilexiconn.llibrary.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for rendering extra models to first-person.
 *
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public interface IFirstPersonExtension extends IExtension
{
    void preRenderFirstPerson(EntityPlayer entity, ModelBiped model);

    void postRenderFirstPerson(EntityPlayer entity, ModelBiped model);
}
