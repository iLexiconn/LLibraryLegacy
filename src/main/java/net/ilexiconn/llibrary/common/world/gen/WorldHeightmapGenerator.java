package net.ilexiconn.llibrary.common.world.gen;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class WorldHeightmapGenerator {
    public byte[][] heightmap, biomemap;
    public int width, height, biomeWidth, biomeHeight;

    public BufferedImage heightmapImage, biomemapImage;

    private Map<Integer, BiomeGenBase> biomes = new HashMap<Integer, BiomeGenBase>();

    private double biomemapToHeightmapWidthRatio, biomemapToHeightmapHeightRatio;
    private boolean loaded;

    public static double cubic(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0]
                + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    public abstract String getBiomeMapLocation();

    public abstract String getHeightmapLocation();

    public abstract double getWorldScale();

    public abstract int adjustHeight(int x, int y, int height);

    public abstract IBlockState getStoneBlock();

    public abstract BiomeGenBase getDefaultBiome();

    public abstract String getName();

    public abstract int getColourForBiome(BiomeGenBase biome);

    public int getWorldOffsetX() {
        return width / 2;
    }

    public int getWorldOffsetZ() {
        return height / 2;
    }

    public abstract boolean hasOcean();

    public abstract IBlockState getOceanLiquid();

    public abstract int getOceanHeight(int x, int z);

    public abstract int getOutOfBoundsHeight(int x, int z);

    public abstract boolean loadHeightmapIntoArray();

    public abstract boolean loadBiomemapIntoArray();

    public void loadHeightmap() {
        LLibrary.logger.info("Loading " + getName() + " Heightmap...");

        try {
            BufferedImage image = ImageIO.read(LLibrary.class.getResourceAsStream(getHeightmapLocation()));

            width = image.getWidth();
            height = image.getHeight();

            if (loadHeightmapIntoArray()) {
                heightmap = new byte[width][height];

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        heightmap[x][y] = (byte) ((getHeightmapImageValue(image, x, y) - 128));
                    }
                }
            } else {
                heightmapImage = image;
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

                if (loadBiomemapIntoArray()) {
                    biomemap = new byte[biomeWidth][biomeHeight];

                    for (int y = 0; y < biomeHeight; y++) {
                        for (int x = 0; x < biomeWidth; x++) {
                            biomemap[x][y] = (byte) getBiomeImageValue(biomeImage, x, y).biomeID;
                        }
                    }
                } else {
                    biomemapImage = biomeImage;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            biomemapToHeightmapHeightRatio = (double) biomeHeight / (double) height;
            biomemapToHeightmapWidthRatio = (double) biomeWidth / (double) width;
        }
    }

    public BiomeGenBase getBiomeImageValue(BufferedImage image, int x, int y) {
        BiomeGenBase biome = getDefaultBiome();

        int rgb = image.getRGB(x, y);

        for (Map.Entry<Integer, BiomeGenBase> entry : biomes.entrySet()) {
            if (isSimilarColour(rgb, entry.getKey())) {
                biome = entry.getValue();
                break;
            }
        }

        return biome;
    }

    public int getHeightmapImageValue(BufferedImage image, int x, int y) {
        return Math.min(adjustHeight(x, y, image.getColorModel().getRed(image.getRaster().getDataElements(x, y, null))), 255);
    }

    public int getHeight(int x, int y) {
        if (loadHeightmapIntoArray()) {
            return ((int) heightmap[x][y]) + 128;
        } else {
            return getHeightmapImageValue(heightmapImage, x, y);
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

        double[][] buffer = new double[4][4];

        double xScaled = (double) x / (scaledWidth - 1) * (width - 1);
        double yScaled = (double) z / (scaledHeight - 1) * (height - 1);
        int xOrigin = (int) xScaled;
        int yOrigin = (int) yScaled;
        double xIntermediate = xScaled - xOrigin;
        double yIntermediate = yScaled - yOrigin;

        for (int u = 0; u < 4; u++) {
            for (int v = 0; v < 4; v++) {
                buffer[u][v] = extract(xOrigin - 1 + u, yOrigin - 1 + v);
            }
        }

        int value = (int) Math.round(BiCubic.bicubic(buffer, xIntermediate, yIntermediate));
        value = Math.min(0xff, Math.max(value, 0));

        return value;
    }

    public BiomeGenBase getBiomeForCoords(int x, int z) {
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

            return getBiome((int) Math.round(newX), (int) Math.round(newZ));
        }

        return getDefaultBiome();
    }

    public BiomeGenBase getBiome(int x, int y) {
        if (loadBiomemapIntoArray()) {
            return BiomeGenBase.getBiome(biomemap[x][y]);
        } else {
            return (getBiomeImageValue(biomemapImage, x, y));
        }
    }

    public void load() {
        loaded = true;

        loadHeightmap();
        loadBiomemap();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public double extract(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Math.min(Math.max(0, getOutOfBoundsHeight(x, y)), 255);
        }

        return getHeight(x, y);
    }

    private static class BiCubic {
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
}
