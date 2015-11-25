package net.ilexiconn.llibrary.common.config;

import net.ilexiconn.llibrary.common.update.UpdateCheckerThread;
import net.ilexiconn.llibrary.common.update.UpdateType;
import net.minecraftforge.common.config.Configuration;

public class LLibraryConfigHandler implements IConfigHandler {
    
    public static String asmMappingDir;
    public static boolean asmDump;
    public static boolean asmTextify;
    
    public static boolean threadedScreenshots;
    public static UpdateType updateType;
    public UpdateType lastUpdateType;

    @Override
    public void loadConfig(Configuration config) {
        asmMappingDir = config.getString("mappingDir", Configuration.CATEGORY_GENERAL, "", "Path to directory holding packaged.srg, fields.csv and methods.csv for mcp remapping");
        asmDump = config.getBoolean("dump_asm", Configuration.CATEGORY_GENERAL, true, "Dump ASM changes made by modular transformers");
        asmTextify = config.getBoolean("textify_asm", Configuration.CATEGORY_GENERAL, true, "Textify ASM dumps");
        
        threadedScreenshots = config.getBoolean("Threaded Screenshots", Configuration.CATEGORY_GENERAL, true, "Enable threaded screenshots. Disable this if you experience crashes. (Restart required)");
        updateType = UpdateType.valueOf(config.getString("Update Type", Configuration.CATEGORY_GENERAL, "Release", "Select the type of updates you want to receive. Be warned though, there may be lots of bugs is alpha and beta versions.", new String[]{"Release", "Beta", "Alpha"}).toUpperCase());

        if (lastUpdateType == null) {
            lastUpdateType = updateType;
        } else if (lastUpdateType != updateType) {
            new UpdateCheckerThread().start();
        }
    }
}
