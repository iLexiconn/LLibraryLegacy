package net.ilexiconn.llibrary.common.json.container;

import net.ilexiconn.llibrary.client.model.tabula.Animation;
import net.ilexiconn.llibrary.client.model.tabula.CubeGroup;
import net.ilexiconn.llibrary.client.model.tabula.CubeInfo;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Container class for {@link net.ilexiconn.llibrary.client.model.tabula.ModelJson}. Use {@link net.ilexiconn.llibrary.common.json.JsonHelper#parseTabulaModel(InputStream)} to get a new instance.
 *
 * @author Gegy1000
 * @see net.ilexiconn.llibrary.client.model.tabula.ModelJson
 * @since 0.1.0
 */
public class JsonTabulaModel {
    protected int textureWidth;
    protected int textureHeight;

    protected final double[] scale = new double[]{1d, 1d, 1d};

    protected ArrayList<CubeGroup> cubeGroups;
    protected ArrayList<CubeInfo> cubes;
    protected ArrayList<Animation> anims;

    protected int cubeCount;

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public double[] getScale() {
        return scale;
    }

    public ArrayList<CubeGroup> getCubeGroups() {
        return cubeGroups;
    }

    public ArrayList<CubeInfo> getCubes() {
        return cubes;
    }

    public ArrayList<Animation> getAnimations() {
        return anims;
    }

    public int getCubeCount() {
        return cubeCount;
    }
}