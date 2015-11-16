package net.ilexiconn.llibrary.common.world.gen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import static net.minecraft.world.biome.BiomeGenBase.*;

public class WorldChunkManagerCustom extends WorldChunkManager
{
    public static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(Arrays.asList(forest, plains, taiga, taigaHills, forestHills, jungle, jungleHills));
    private GenLayer genBiomes;
    /** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
    private GenLayer biomeIndexLayer;
    /** The BiomeCache object for this world. */
    private BiomeCache biomeCache;
    /** A list of biomes that the player can spawn in. */
    private List biomesToSpawnIn;

    private WorldHeightmapGenerator generator;

    protected WorldChunkManagerCustom(WorldHeightmapGenerator generator)
    {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = new ArrayList();
        this.biomesToSpawnIn.addAll(allowedBiomes);
        this.generator = generator;
    }

    public WorldChunkManagerCustom(long seed, WorldType worldType, WorldHeightmapGenerator generator)
    {
        this(generator);
        GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldType);
        agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }

    public WorldChunkManagerCustom(World world, WorldHeightmapGenerator generator)
    {
        this(world.getSeed(), world.getWorldInfo().getTerrainType(), generator);
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    public List getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    public BiomeGenBase getBiomeGenAt(int x, int z)
    {
        return this.biomeCache.getBiomeGenAt(x, z);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] rainfall, int x, int z, int width, int length)
    {
        IntCache.resetIntCache();

        if (rainfall == null || rainfall.length < width * length)
        {
            rainfall = new float[width * length];
        }

        for (int partX = 0; partX < width; ++partX)
        {
            for (int partZ = 0; partZ < length; ++partZ)
            {
                try
                {
                    float f = getBiomeAt(x + partX, z + partZ).getIntRainfall() / 65536.0F;

                    if (f > 1.0F)
                    {
                        f = 1.0F;
                    }

                    rainfall[partX * partZ] = f;
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                    crashreportcategory.addCrashSection("biome id", Integer.valueOf(partX));
                    crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(rainfall.length));
                    crashreportcategory.addCrashSection("x", Integer.valueOf(x));
                    crashreportcategory.addCrashSection("z", Integer.valueOf(z));
                    crashreportcategory.addCrashSection("w", Integer.valueOf(width));
                    crashreportcategory.addCrashSection("h", Integer.valueOf(length));
                    throw new ReportedException(crashreport);
                }
            }
        }

        return rainfall;
    }

    private BiomeGenBase getBiomeAt(int x, int z)
    {
        return generator.getBiomeAt(x, z);
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_)
    {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int length)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * length)
        {
            biomes = new BiomeGenBase[width * length];
        }

        try
        {
            for (int partX = 0; partX < width; ++partX)
            {
                for (int partZ = 0; partZ < length; ++partZ)
                {
                    biomes[partX * partZ] = getBiomeAt(x + partX, z + partZ);
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
            crashreportcategory.addCrashSection("h", Integer.valueOf(length));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int z, int width, int depth)
    {
        return this.getBiomeGenAt(biomes, x, z, width, depth, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int z, int width, int length, boolean checkBiomeCache)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * length)
        {
            biomes = new BiomeGenBase[width * length];
        }

        if (checkBiomeCache && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            BiomeGenBase[] cachedBiomes = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(cachedBiomes, 0, biomes, 0, width * length);
            return biomes;
        }
        else
        {
            for (int partX = 0; partX < width; ++partX)
            {
                for (int partZ = 0; partZ < length; ++partZ)
                {
                    biomes[partX * partZ] = getBiomeAt(x + partX, z + partZ);
                }
            }

            return biomes;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int x, int z, int radius, List biomes)
    {
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
                    if (!biomes.contains(getBiomeAt(x + partX, z + partZ)))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
            crashreportcategory.addCrashSection("allowed", biomes);
            throw new ReportedException(crashreport);
        }
    }

    public ChunkPosition findBiomePosition(int x, int z, int radius, List p_150795_4_, Random rand)
    {
        IntCache.resetIntCache();
        int l = x - radius >> 2;
        int i1 = z - radius >> 2;
        int j1 = x + radius >> 2;
        int k1 = z + radius >> 2;
        int width = j1 - l + 1;
        int length = k1 - i1 + 1;
        ChunkPosition chunkposition = null;
        int j2 = 0;

        for (int partX = 0; partX < width; ++partX)
        {
            for (int partZ = 0; partZ < length; ++partZ)
            {
                int index = partX * partZ;

                int chunkX = l + index % width << 2;
                int chunkZ = i1 + index / width << 2;
                BiomeGenBase biomegenbase = getBiomeAt(x + partX, z + partZ);

                if (p_150795_4_.contains(biomegenbase) && (chunkposition == null || rand.nextInt(j2 + 1) == 0))
                {
                    chunkposition = new ChunkPosition(chunkX, 0, chunkZ);
                    ++j2;
                }
            }
        }

        return chunkposition;
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }

    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
    {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }
}