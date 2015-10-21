package net.ilexiconn.llibrary.common.json.container;

import net.ilexiconn.llibrary.common.update.UpdateType;

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

    @Deprecated
    public String getNewestVersion()
    {
        return newestVersion;
    }

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

    public String getRelease()
    {
        return release;
    }

    public String getBeta()
    {
        return beta;
    }

    public String getAlpha()
    {
        return alpha;
    }
}