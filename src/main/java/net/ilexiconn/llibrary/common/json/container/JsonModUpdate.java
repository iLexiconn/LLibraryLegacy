package net.ilexiconn.llibrary.common.json.container;

import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.update.UpdateType;
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
public class JsonModUpdate
{
    private transient ArtifactVersion releaseVersion;
    private transient ArtifactVersion betaVersion;
    private transient ArtifactVersion alphaVersion;

    private Map<String, List<String>> versions;
    private String updateUrl;
    private String iconUrl;

    @Deprecated
    private String newestVersion;

    private String release;
    private String beta;
    private String alpha;

    public String modid;
    public String name;
    public String currentVersion;
    public BufferedImage thumbnail;
    public UpdateType updateType;

    public Map<String, List<String>> getVersions()
    {
        return versions;
    }

    public String getUpdateUrl()
    {
        return updateUrl;
    }

    public String getIconUrl()
    {
        return iconUrl;
    }

    public ArtifactVersion getRelease()
    {
        if (releaseVersion == null) releaseVersion = new DefaultArtifactVersion(modid, release == null ? newestVersion : release);
        return releaseVersion;
    }

    public ArtifactVersion getBeta()
    {
        if (betaVersion == null && beta != null) betaVersion = new DefaultArtifactVersion(modid, beta);
        return betaVersion;
    }

    public ArtifactVersion getAlpha()
    {
        if (alphaVersion == null && alpha != null) alphaVersion = new DefaultArtifactVersion(modid, alpha);
        return alphaVersion;
    }

    public ArtifactVersion getUpdateVersion()
    {
        switch (LLibraryConfigHandler.updateType)
        {
            case ALPHA:
                if (getAlpha() != null)
                {
                    updateType = UpdateType.ALPHA;
                    return getAlpha();
                }
                else if (getBeta() != null)
                {
                    updateType = UpdateType.BETA;
                    return getBeta();
                }
                else
                {
                    updateType = UpdateType.RELEASE;
                    return getRelease();
                }
            case BETA:
                if (getBeta() != null)
                {
                    updateType = UpdateType.BETA;
                    return getBeta();
                }
                else
                {
                    updateType = UpdateType.RELEASE;
                    return getRelease();
                }
            default:
                updateType = UpdateType.RELEASE;
                return getRelease();
        }
    }
}