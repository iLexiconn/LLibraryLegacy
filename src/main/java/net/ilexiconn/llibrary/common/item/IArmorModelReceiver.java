package net.ilexiconn.llibrary.common.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for custom modelled armor items.
 *
 * @author iLexiconn
 */
public interface IArmorModelReceiver
{
    /**
     * Get the instance of the scaled model.
     * Pass 0 = Boots, Chestplate, Helmet (scale 1f)
     * Pass 1 = Leggings (scale 0.5f)
     *
     * @param pass the current render pass
     * @return the armor model
     */
    @SideOnly(Side.CLIENT)
    ModelBiped getArmorModel(int pass);

    /**
     * Get the string path to the texture.
     *
     * @return the texture path
     */
    String getModelTextureName();
}
