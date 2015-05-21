package net.ilexiconn.llibrary.common.json.container;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class JsonModUpdate
{
    public String modid;
    public String name;
    public String currentVersion;
    public BufferedImage thumbnail;
    private String newestVersion;
    private Map<String, List<String>> versions;
    private String updateUrl;
    private String iconUrl;

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
}