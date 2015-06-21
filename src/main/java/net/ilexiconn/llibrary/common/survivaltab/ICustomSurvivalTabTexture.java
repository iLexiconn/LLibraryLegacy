package net.ilexiconn.llibrary.common.survivaltab;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for custom survival tabs textures, implement this inside your own {@link net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab} class.
 * 
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab
 * @since 0.2.0
 */
public interface ICustomSurvivalTabTexture
{
    @SideOnly(Side.CLIENT)
    ResourceLocation getTabTexture();
}
