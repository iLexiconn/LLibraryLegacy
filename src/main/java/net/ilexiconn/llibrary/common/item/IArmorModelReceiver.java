package net.ilexiconn.llibrary.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

/**
 * Interface for custom modelled armor items.
 *
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.item.ItemModelledArmor
 * @since 0.1.0
 */
public interface IArmorModelReceiver {
    /**
     * Get the instance of the scaled model. Pass 0 = Boots, Chestplate, Helmet (scale 1f) Pass 1 = Leggings (scale 0.5f)
     *
     * @param pass the current render pass
     * @return the armor model
     * @since 0.1.0
     */
    @SideOnly(Side.CLIENT)
    ModelBiped getArmorModel(int pass);

    /**
     * Get the string path to the texture.
     *
     * @return the texture path
     * @since 0.1.0
     */
    String getModelTextureName();
}
