package net.ilexiconn.llibrary.common.config;

import net.minecraftforge.common.config.Configuration;

public class LLibraryConfigHandler implements IConfigHandler
{
    public static boolean threadedScreenshots;

    public void loadConfig(Configuration config)
    {
        threadedScreenshots = config.getBoolean("threadedScreenshots", Configuration.CATEGORY_GENERAL, true, "Enable threaded screenshots. Disable this if you experience crashes. (Restart required)");
    }
}