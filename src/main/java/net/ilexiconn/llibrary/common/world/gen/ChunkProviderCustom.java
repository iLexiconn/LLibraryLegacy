package net.ilexiconn.llibrary.common.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.eventhandler.Event.*;
import net.minecraftforge.event.terraingen.*;

public class ChunkProviderCustom implements IChunkProvider
{
    private Random rand;
    private World worldObj;

    private WorldHeightmapGenerator generator;

    private BiomeGenBase[] biomesForGeneration;

    public ChunkProviderCustom(World world, long seed, WorldHeightmapGenerator generator)
    {
        this.worldObj = world;
        this.rand = new Random(seed);
        this.generator = generator;

        if (!generator.isLoaded())
        {
            this.generator.load();
        }
    }

    public void func_147424_a(int chunkX, int chunkZ, Block[] blocks)
    {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int height = (generator.getHeightForCoords(x + (chunkX * 16), z + (chunkZ * 16)));

                for (int y = 0; y < height; y++)
                {
                    blocks[(x + z * 16) * y] = generator.getStoneBlock();
                }
            }
        }
    }

    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blocks, byte[] meta, BiomeGenBase[] biomes)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, blocks, meta, biomes, this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                generateBiome(worldObj, meta, biomes[z + x * 16], rand, blocks, x, z);
            }
        }
    }

    private void generateBiome(World world, byte[] meta, BiomeGenBase biome, Random rand, Block[] blocks, int x, int z)
    {
        int xInChunk = x & 15;
        int zInChunk = z & 15;
        int chunkWidth = blocks.length / 256;

        boolean reachedSurface = false;
        int depthSinceSurface = 0;

        for (int y = 255; y >= 0; --y)
        {
            int arrayPos = (zInChunk * 16 + xInChunk) * chunkWidth + y;

            if (y <= rand.nextInt(5))
            {
                blocks[arrayPos] = Blocks.bedrock;
            }
            else
            {
                Block previousBlock = blocks[arrayPos];

                if (previousBlock == null || previousBlock.getMaterial() == Material.air)
                {
                    reachedSurface = true;
                }
                else if (previousBlock == generator.getStoneBlock())
                {
                    if (reachedSurface)
                    {
                        if (depthSinceSurface == 0)
                        {
                            blocks[arrayPos] = biome.topBlock;
                        }
                        else
                        {
                            blocks[arrayPos] = biome.fillerBlock;
                        }

                        depthSinceSurface++;
                    }
                }
            }
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
    {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_)
    {
        this.rand.setSeed((long)p_73154_1_ * 341873128712L + (long)p_73154_2_ * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(p_73154_1_, p_73154_2_, ablock);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        this.replaceBlocksForBiome(p_73154_1_, p_73154_2_, ablock, abyte, this.biomesForGeneration);

        Chunk chunk = new Chunk(this.worldObj, ablock, abyte, p_73154_1_, p_73154_2_);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k)
        {
            abyte1[k] = (byte)this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int x, int y)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ)
    {
        BlockFalling.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x + 16, z + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) chunkX * i1 + (long) chunkZ * j1 ^ this.worldObj.getSeed());
        boolean hasVillageGenerated = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(chunkProvider, worldObj, rand, chunkX, chunkZ, hasVillageGenerated));

        int xInChunk;
        int zInChunk;
        int y;

        biomegenbase.decorate(this.worldObj, this.rand, x, z);

        if (TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, hasVillageGenerated, ANIMALS))
        {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, x + 8, z + 8, 16, 16, this.rand);
        }

        x += 8;
        z += 8;

        boolean doGen = TerrainGen.populate(chunkProvider, worldObj, rand, chunkX, chunkZ, hasVillageGenerated, ICE);
        for (xInChunk = 0; doGen && xInChunk < 16; ++xInChunk)
        {
            for (zInChunk = 0; zInChunk < 16; ++zInChunk)
            {
                y = this.worldObj.getPrecipitationHeight(x + xInChunk, z + zInChunk);

                if (this.worldObj.isBlockFreezable(xInChunk + x, y - 1, zInChunk + z))
                {
                    this.worldObj.setBlock(xInChunk + x, y - 1, zInChunk + z, Blocks.ice, 0, 2);
                }

                if (this.worldObj.func_147478_e(xInChunk + x, y, zInChunk + z, true))
                {
                    this.worldObj.setBlock(xInChunk + x, y, zInChunk + z, Blocks.snow_layer, 0, 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(chunkProvider, worldObj, rand, chunkX, chunkZ, hasVillageGenerated));

        BlockFalling.fallInstantly = false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate update)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {}

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z)
    {
        return this.worldObj.getBiomeGenForCoords(x, z).getSpawnableList(creatureType);
    }

    public ChunkPosition func_147416_a(World world, String structure, int x, int y, int z)
    {
        return null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_)
    {
    }
}