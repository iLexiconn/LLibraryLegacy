package net.ilexiconn.llibrary.common.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * Interface for custom survival tabs textures, implement this inside your own {@link net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab} class.
 *
 * @see         net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab
 * @author      iLexiconn
 * @since       0.2.0
 */
public interface ICustomSurvivalTabTexture
{
    @SideOnly(Side.CLIENT)
    ResourceLocation getTabTexture();
}
