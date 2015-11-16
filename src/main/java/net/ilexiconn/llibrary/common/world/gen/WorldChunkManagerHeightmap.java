package net.ilexiconn.llibrary.common.world.gen;

import com.google.common.collect.Lists;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * @author gegy1000
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class WorldChunkManagerHeightmap extends WorldChunkManager {
    /**
     * The biome list.
     */
    private BiomeCache biomeCache;
    /**
     * A list of biomes that the player can spawn in.
     */
    private List biomesToSpawnIn;
    private String field_180301_f;
    private long seed;

    private WorldHeightmapGenerator generator;

    protected WorldChunkManagerHeightmap(WorldHeightmapGenerator generator) {
        this.biomeCache = new BiomeCache(this);
        this.field_180301_f = "";
        this.biomesToSpawnIn = Lists.newArrayList();
        this.biomesToSpawnIn.addAll(allowedBiomes);
        this.generator = generator;
    }

    public WorldChunkManagerHeightmap(long seed, WorldType worldType, String p_i45744_4_, WorldHeightmapGenerator generator) {
        this(generator);
        this.field_180301_f = p_i45744_4_;
        this.seed = seed;
    }

    public WorldChunkManagerHeightmap(World worldIn, WorldHeightmapGenerator generator) {
        this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions(), generator);
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    public List getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the biome generator
     */
    public BiomeGenBase getBiomeGenerator(BlockPos pos) {
        return this.func_180300_a(pos, null);
    }

    public BiomeGenBase func_180300_a(BlockPos pos, BiomeGenBase biome) {
        return this.biomeCache.func_180284_a(pos.getX(), pos.getZ(), biome);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new float[width * length];
        }

        for (int partX = 0; partX < width; ++partX)
        {
            for (int partZ = 0; partZ < length; ++partZ)
            {
                try
                {
                    float f = (float) BiomeGenBase.getBiomeFromBiomeList(getBiomeAt(partX + x, partZ + z).biomeID, BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;

                    if (f > 1.0F)
                    {
                        f = 1.0F;
                    }

                    listToReuse[partX * partZ] = f;
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                    crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
                    crashreportcategory.addCrashSection("x", Integer.valueOf(x));
                    crashreportcategory.addCrashSection("z", Integer.valueOf(z));
                    crashreportcategory.addCrashSection("w", Integer.valueOf(width));
                    crashreportcategory.addCrashSection("h", Integer.valueOf(length));
                    throw new ReportedException(crashreport);
                }
            }
        }

        return listToReuse;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_) {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * height) {
            biomes = new BiomeGenBase[width * height];
        }

        try
        {
            for (int partX = 0; partX < width; ++partX)
            {
                for (int partZ = 0; partZ < height; ++partZ)
                {
                    biomes[partX * partZ] = getBiomeAt(partX + x, partZ + z);
                }
            }

            return biomes;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("w", Integer.valueOf(width));
            crashreportcategory.addCrashSection("h", Integer.valueOf(height));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth) {
        return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     *
     * @param cacheFlag If false, don't check biomeCache to avoid infinite loop in BiomeCacheBlock
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new BiomeGenBase[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
            BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase1, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        else
        {
            for (int partX = 0; partX < width; ++partX)
            {
                for (int partZ = 0; partZ < length; ++partZ)
                {
                    listToReuse[partX * partZ] = getBiomeAt(x + partX, z + partZ);
                }
            }

            return listToReuse;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int x, int z, int radius, List allowed) {
        IntCache.resetIntCache();
        int l = x - radius >> 2;
        int i1 = z - radius >> 2;
        int j1 = x + radius >> 2;
        int k1 = z + radius >> 2;
        int width = j1 - l + 1;
        int length = k1 - i1 + 1;

        try
        {
            for (int partX = 0; partX < width; ++partX)
            {
                for (int partZ = 0; partZ < length; ++partZ)
                {
                    BiomeGenBase biomegenbase = getBiomeAt(partX + x, partZ + z);

                    if (!allowed.contains(biomegenbase))
                    {
                        return false;
                    }
                }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
            crashreportcategory.addCrashSection("allowed", allowed);
            throw new ReportedException(crashreport);
        }
    }

    public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random) {
        IntCache.resetIntCache();
        int l = x - range >> 2;
        int i1 = z - range >> 2;
        int j1 = x + range >> 2;
        int k1 = z + range >> 2;
        int width = j1 - l + 1;
        int length = k1 - i1 + 1;
        BlockPos blockpos = null;
        int j2 = 0;

        for (int partX = 0; partX < width; ++partX)
        {
            for (int partZ = 0; partZ < length; ++partZ)
            {
                int index = partX * partZ;

                int chunkX = l + index % width << 2;
                int chunkZ = i1 + index / width << 2;
                BiomeGenBase biomegenbase = getBiomeAt(partX + x, partZ + z);

                if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(j2 + 1) == 0))
                {
                    blockpos = new BlockPos(chunkX, 0, chunkZ);
                    ++j2;
                }
            }
        }

        return blockpos;
    }

    private BiomeGenBase getBiomeAt(int x, int z) {
        return generator.getBiomeAt(x, z);
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }
}