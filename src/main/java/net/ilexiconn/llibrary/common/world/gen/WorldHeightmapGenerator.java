package net.ilexiconn.llibrary.common.world.gen;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

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

    public abstract int adjustHeight(int height);

    public abstract IBlockState getStoneBlock();

    public abstract BiomeGenBase getDefaultBiome();

    public abstract String getName();

    public abstract int getColourForBiome(BiomeGenBase biome);

    public int getWorldOffsetX()
    {
        return width / 2;
    }

    public int getWorldOffsetZ()
    {
        return height / 2;
    }

    public abstract boolean hasOcean();

    public abstract IBlockState getOceanLiquid();

    public abstract int getOceanHeight(int x, int z);

    public abstract int getOutOfBoundsHeight();

    public void loadHeightmap() {
        LLibrary.logger.info("Loading " + getName() + " Heightmap...");

        try {
            BufferedImage image = ImageIO.read(LLibrary.class.getResourceAsStream(getHeightmapLocation()));

            width = image.getWidth();
            height = image.getHeight();

            heightmap = new byte[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int height = image.getColorModel().getRed(image.getRaster().getDataElements(x, y, null));

                    height = Math.min(adjustHeight(height), 255);

                    heightmap[x][y] = (byte) ((height - 128));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBiomemap() {
        String biomeMapLocation = getBiomeMapLocation();

        if (biomeMapLocation != null) {
            LLibrary.logger.info("Loading " + getName() + " Biomemap..");

            for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
                biomes.put(getColourForBiome(biome), biome);
            }

            try {
                BufferedImage biomeImage = ImageIO.read(LLibrary.class.getResourceAsStream(biomeMapLocation));

                biomeWidth = biomeImage.getWidth();
                biomeHeight = biomeImage.getHeight();

                biomemap = new byte[biomeWidth][biomeHeight];

                for (int y = 0; y < biomeHeight; y++) {
                    for (int x = 0; x < biomeWidth; x++) {
                        biomemap[x][y] = (byte) getDefaultBiome().biomeID;

                        for (Map.Entry<Integer, BiomeGenBase> entry : biomes.entrySet()) {
                            if (isSimilarColour(biomeImage.getRGB(x, y), entry.getKey())) {
                                biomemap[x][y] = (byte) entry.getValue().biomeID;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            biomemapToHeightmapHeightRatio = (double) biomeHeight / (double) height;
            biomemapToHeightmapWidthRatio = (double) biomeWidth / (double) width;
        }
    }

    private boolean isSimilarColour(int colour, int desiredColour) {
        int r1 = (colour) & 0xFF;
        int g1 = (colour >> 8) & 0xFF;
        int b1 = (colour >> 16) & 0xFF;

        int r2 = (desiredColour) & 0xFF;
        int g2 = (desiredColour >> 8) & 0xFF;
        int b2 = (desiredColour >> 16) & 0xFF;

        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2) < 10;
    }

    public int getHeightForCoords(int x, int z) {
        double scale = getWorldScale();

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        x += (getWorldOffsetX() * scale);
        z += (getWorldOffsetZ() * scale);

        if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight) {
            return getOutOfBoundsHeight();
        }

        double[][] buffer = new double[4][4];

        double xScaled = (double) x / (scaledWidth - 1) * (width - 1);
        double yScaled = (double) z / (scaledHeight - 1) * (height - 1);
        int xOrigin = (int) xScaled;
        int yOrigin = (int) yScaled;
        double xIntermediate = xScaled - xOrigin;
        double yIntermediate = yScaled - yOrigin;

        for (int u = 0; u < 4; u++) {
            for (int v = 0; v < 4; v++) {
                buffer[u][v] = extract(heightmap, xOrigin - 1 + u, yOrigin - 1 + v);
            }
        }

        int value = (int) Math.round(BiCubic.bicubic(buffer, xIntermediate, yIntermediate)) + 128;
        value = Math.min(0xff, Math.max(value, 0));

        return value;
    }

    public BiomeGenBase getBiomeAt(int x, int z) {
        if (biomemap != null) {
            double scale = getWorldScale();

            int scaledWidth = (int) (width * scale);
            int scaledHeight = (int) (height * scale);

            x += (getWorldOffsetX() * scale);
            z += (getWorldOffsetZ() * scale);

            if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight) {
                return getDefaultBiome();
            }

            double newX = (x * biomemapToHeightmapWidthRatio / scale);
            double newZ = (z * biomemapToHeightmapHeightRatio / scale);

            return BiomeGenBase.getBiome(biomemap[((int) Math.round(newX))][((int) Math.round(newZ))]);
        }

        return getDefaultBiome();
    }

    public void load() {
        loaded = true;

        loadHeightmap();
        loadBiomemap();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public static double cubic(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0]
                + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    private static class BiCubic
    {
        private static ThreadLocal<double[]> ARR_THREADSAFE = new ThreadLocal<double[]>() {
            protected double[] initialValue() {
                return new double[4];
            }
        };

        public static double bicubic(double[][] p, double x, double y) {
            double[] arr = ARR_THREADSAFE.get();
            arr[0] = cubic(p[0], y);
            arr[1] = cubic(p[1], y);
            arr[2] = cubic(p[2], y);
            arr[3] = cubic(p[3], y);
            return cubic(arr, x);
        }
    }

    public static double extract(byte[][] arr, int x, int y)
    {
        x = Math.min(arr.length - 1, Math.max(0, x));
        byte[] col = arr[x];
        y = Math.min(col.length - 1, Math.max(0, y));
        return col[y];
    }
}
