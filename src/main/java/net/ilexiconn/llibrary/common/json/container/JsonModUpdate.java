package net.ilexiconn.llibrary.common.json.container;

import com.google.gson.annotations.SerializedName;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.update.UpdateType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Container class for {@link net.ilexiconn.llibrary.common.update.UpdateHelper}
 *
 * @author iLexiconn
 * @author FiskFille
 * @see net.ilexiconn.llibrary.common.update.UpdateHelper
 * @since 0.1.0
 */
public class JsonModUpdate {
    public transient boolean updated;
    public transient ModContainer modContainer;
    public transient String modid;
    public transient String name;
    public transient String currentVersion;
    public transient BufferedImage thumbnail;
    public transient UpdateType updateType;
    private transient ArtifactVersion releaseVersion;
    private transient ArtifactVersion betaVersion;
    private transient ArtifactVersion alphaVersion;
    public Map<String, List<String>> versions;
    public String updateUrl;
    public String iconUrl;
    @Deprecated
    public String newestVersion;
    public String release;
    public String beta;
    public String alpha;
    @SerializedName("directUpdateUrl-" + MinecraftForge.MC_VERSION)
    public String directUpdateUrl;

    public Map<String, List<String>> getVersions() {
        return versions;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public ArtifactVersion getRelease() {
        if (releaseVersion == null) {
            releaseVersion = new DefaultArtifactVersion(modid, release == null ? newestVersion : release);
        }
        return releaseVersion;
    }

    public ArtifactVersion getBeta() {
        if (betaVersion == null && beta != null) {
            betaVersion = new DefaultArtifactVersion(modid, beta);
        }
        return betaVersion;
    }

    public ArtifactVersion getAlpha() {
        if (alphaVersion == null && alpha != null) {
            alphaVersion = new DefaultArtifactVersion(modid, alpha);
        }
        return alphaVersion;
    }

    public ArtifactVersion getUpdateVersion() {
        switch (LLibraryConfigHandler.updateType) {
            case ALPHA:
                if (getAlpha() != null) {
                    updateType = UpdateType.ALPHA;
                    return getAlpha();
                } else if (getBeta() != null) {
                    updateType = UpdateType.BETA;
                    return getBeta();
                } else {
                    updateType = UpdateType.RELEASE;
                    return getRelease();
                }
            case BETA:
                if (getBeta() != null) {
                    updateType = UpdateType.BETA;
                    return getBeta();
                } else {
                    updateType = UpdateType.RELEASE;
                    return getRelease();
                }
            default:
                updateType = UpdateType.RELEASE;
                return getRelease();
        }
    }

    public String getDirectUpdateUrl() {
        return directUpdateUrl;
    }
}