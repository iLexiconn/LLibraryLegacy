package net.ilexiconn.llibrary.common.biome;

import net.minecraft.world.biome.BiomeGenBase;

/**
 * @author gegy1000
 * @since 0.3.0
 */
public class BiomeHelper {
    /**
     * @param oldBiomeId the id of the biome to replace
     * @param newBiome   the biome to replace the current one with
     * @since 0.3.0
     */
    public static void overrideBiome(int oldBiomeId, BiomeGenBase newBiome) {
        BiomeGenBase.getBiomeGenArray()[oldBiomeId] = newBiome;
    }

    /**
     * @param oldBiome the biome to replace
     * @param newBiome the biome to replace the current one with
     * @since 0.3.0
     */
    public static void overrideBiome(BiomeGenBase oldBiome, BiomeGenBase newBiome) {
        overrideBiome(oldBiome.biomeID, newBiome);
    }

    /**
     * Creates a unique biome id, not used by any other biome.
     *
     * @since 0.3.0
     */
    public static int getUniqueBiomeId() {
        BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();

        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] == null) {
                return i;
            }
        }

        return -1;
    }
}
