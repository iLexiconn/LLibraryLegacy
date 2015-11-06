package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.IOException;
import java.util.List;

/**
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
public class VersionHandler {
    private static List<JsonModUpdate> outdatedMods = Lists.newArrayList();

    public static List<JsonModUpdate> searchForOutdatedModsInefficiently() throws IOException {
        List<JsonModUpdate> outdatedMods = Lists.newArrayList();

        for (JsonModUpdate mod : UpdateHelper.modList) {
            ModContainer modContainer = null;
            for (ModContainer c : Loader.instance().getModList()) {
                if (c.getModId().equals(mod.modid)) {
                    modContainer = c;
                }
            }
            if (modContainer == null) {
                continue;
            }
            if (mod.getUpdateVersion().compareTo(modContainer.getProcessedVersion()) > 0) {
                outdatedMods.add(mod);
            }
        }

        VersionHandler.outdatedMods = outdatedMods;
        return outdatedMods;
    }

    public static List<JsonModUpdate> searchForOutdatedMods() {
        try {
            return searchForOutdatedModsInefficiently();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Lists.newArrayList();
    }

    public static List<JsonModUpdate> getOutdatedMods() {
        return VersionHandler.outdatedMods;
    }
}