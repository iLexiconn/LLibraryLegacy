package net.ilexiconn.llibrary.common.config;

import net.ilexiconn.llibrary.common.update.UpdateCheckerThread;
import net.ilexiconn.llibrary.common.update.UpdateType;
import net.minecraftforge.common.config.Configuration;

public class LLibraryConfigHandler implements IConfigHandler {
    public static boolean threadedScreenshots;
    public static UpdateType updateType;
    public UpdateType lastUpdateType;

    public void loadConfig(Configuration config) {
        threadedScreenshots = config.getBoolean("Threaded Screenshots", Configuration.CATEGORY_GENERAL, true, "Enable threaded screenshots. Disable this if you experience crashes. (Restart required)");
        updateType = UpdateType.valueOf(config.getString("Update Type", Configuration.CATEGORY_GENERAL, "Release", "Select the type of updates you want to receive. Be warned though, there may be lots of bugs is alpha and beta versions.", new String[]{"Release", "Beta", "Alpha"}).toUpperCase());

        if (lastUpdateType == null) {
            lastUpdateType = updateType;
        } else if (lastUpdateType != updateType) {
            new UpdateCheckerThread().start();
        }
    }
}
