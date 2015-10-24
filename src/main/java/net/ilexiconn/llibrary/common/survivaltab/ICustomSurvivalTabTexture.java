package net.ilexiconn.llibrary.common.survivaltab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * @deprecated Use {@link net.ilexiconn.llibrary.api.SurvivalTab} instead.
 */
@Deprecated
public interface ICustomSurvivalTabTexture
{
    @SideOnly(Side.CLIENT)
    ResourceLocation getTabTexture();
}
