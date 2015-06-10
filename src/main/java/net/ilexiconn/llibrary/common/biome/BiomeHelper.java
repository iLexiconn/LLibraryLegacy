package net.ilexiconn.llibrary.common.biome;

import net.minecraft.world.biome.BiomeGenBase;

/**
 * @author gegy1000
 * @since 0.3.0
 */
public class BiomeHelper
{
    /**
     * Creates a unique biome id, not used by any other biome.
     */
    public static int getUniqueBiomeId()
    {
        BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
        
        for (int i = 0; i < biomes.length; i++)
        {
            if(biomes[i] == null)
            {
                return i;
            }
        }
        
        return -1;
    }
}
