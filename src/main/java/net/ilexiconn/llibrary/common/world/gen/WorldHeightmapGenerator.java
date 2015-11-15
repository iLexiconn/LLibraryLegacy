package net.ilexiconn.llibrary.common.world.gen;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class WorldHeightmapGenerator
{
    public byte[][] heightmap, biomemap;
    public int width, height, biomeWidth, biomeHeight;

    private Map<Integer, BiomeGenBase> biomes = new HashMap<Integer, BiomeGenBase>();

    private double biomemapToHeightmapWidthRatio, biomemapToHeightmapHeightRatio;
    private boolean loaded;

    public abstract String getBiomeMapLocation();
    public abstract String getHeightmapLocation();

    public abstract double getWorldScale();
    public abstract double getHeightScale();

    public int getHeightOffset()
    {
        return 0;
    }

    public abstract IBlockState getStoneBlock();

    public abstract BiomeGenBase getDefaultBiome();

    public abstract String getName();

    public abstract int getColourForBiome(BiomeGenBase biome);

    public void loadHeightmap()
    {
        LLibrary.logger.info("Loading " + getName() + " Heightmap...");

        try
        {
            BufferedImage image = ImageIO.read(LLibrary.class.getResourceAsStream(getHeightmapLocation()));

            width = image.getWidth();
            height = image.getHeight();

            heightmap = new byte[width][height];

            Random random = new Random(Long.MAX_VALUE);

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int height = (int) (((image.getRGB(x, y) & 0x0000FF) * getHeightScale()) + getHeightOffset());

                    if (height + 5 > 255)
                    {
                        height = 250;
                    }

                    if (height <= 1)
                    {
                        heightmap[x][y] = (byte) ((random.nextInt(5) + 25) - 128);
                    }
                    else
                    {
                        heightmap[x][y] = (byte) ((height - 128) + 5);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadBiomemap()
    {
        String biomeMapLocation = getBiomeMapLocation();

        if (biomeMapLocation != null)
        {
            LLibrary.logger.info("Loading " + getName() + " Biomemap..");

            for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
            {
                biomes.put(getColourForBiome(biome), biome);
            }

            try
            {
                BufferedImage biomeImage = ImageIO.read(LLibrary.class.getResourceAsStream(biomeMapLocation));

                biomeWidth = biomeImage.getWidth();
                biomeHeight = biomeImage.getHeight();

                biomemap = new byte[biomeWidth][biomeHeight];

                for (int y = 0; y < biomeHeight; y++)
                {
                    for (int x = 0; x < biomeWidth; x++)
                    {
                        biomemap[x][y] = (byte) getDefaultBiome().biomeID;

                        for (Map.Entry<Integer, BiomeGenBase> entry : biomes.entrySet())
                        {
                            if (isSimilarColour(biomeImage.getRGB(x, y), entry.getKey()))
                            {
                                biomemap[x][y] = (byte) entry.getValue().biomeID;
                                break;
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            biomemapToHeightmapHeightRatio = (double) biomeHeight / (double) height;
            biomemapToHeightmapWidthRatio = (double) biomeWidth / (double) width;
        }
    }

    private boolean isSimilarColour(int colour, int desiredColour)
    {
        int r1 = (colour) & 0xFF;
        int g1 = (colour >> 8) & 0xFF;
        int b1 = (colour >> 16) & 0xFF;

        int r2 = (desiredColour) & 0xFF;
        int g2 = (desiredColour >> 8) & 0xFF;
        int b2 = (desiredColour >> 16) & 0xFF;

        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2) < 10;
    }

    private int getHeight(int x, int y)
    {
        return ((int) heightmap[x][y]) + 128;
    }

    public int getHeightForCoords(int x, int z)
    {
        double scale = getWorldScale();

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight)
        {
            return 10;
        }

        BicubicInterpolator bi = new BicubicInterpolator();

        return bi.getValue(heightmap, x, z, scale) + 128;
    }

    public BiomeGenBase getBiomeAt(int x, int z)
    {
        if (biomemap != null)
        {
            double scale = getWorldScale();

            int scaledWidth = (int) (width * scale);
            int scaledHeight = (int) (height * scale);

            if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight)
            {
                return getDefaultBiome();
            }

            double newX = ((x) * biomemapToHeightmapWidthRatio / scale);
            double newZ = ((z) * biomemapToHeightmapHeightRatio / scale);

            return BiomeGenBase.getBiome(biomemap[((int) newX)][((int) newZ)]);
        }

        return getDefaultBiome();
    }

    public void load()
    {
        loaded = true;

        loadHeightmap();
        loadBiomemap();
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public static class CubicInterpolator
    {
        public static byte getValue(byte[] p, double x, double scale)
        {
            x /= scale;

            int xi = (int) x;
            x -= xi;
            double p0 = p[Math.max(0, xi - 1)];
            double p1 = p[xi];
            double p2 = p[Math.min(p.length - 1,xi + 1)];
            double p3 = p[Math.min(p.length - 1, xi + 2)];
            return (byte) Math.round(p1 + 0.5 * x * (p2 - p0 + x * (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3 + x * (3.0 * (p1 - p2) + p3 - p0))));
        }
    }

    public static class BicubicInterpolator extends CubicInterpolator
    {
        private byte[] arr = new byte[4];

        public byte getValue(byte[][] p, double x, double y, double scale)
        {
            x /= scale;

            int xi = (int) x;
            x -= xi;
            arr[0] = getValue(p[Math.max(0, xi - 1)], y, scale);
            arr[1] = getValue(p[xi], y, scale);
            arr[2] = getValue(p[Math.min(p.length - 1,xi + 1)], y, scale);
            arr[3] = getValue(p[Math.min(p.length - 1, xi + 2)], y, scale);
            return getValue(arr, x + 1, scale);
        }
    }
}
