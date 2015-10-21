package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;

import java.io.IOException;
import java.util.List;

/**
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
public class VersionHandler
{
    private static List<JsonModUpdate> outdatedMods = Lists.newArrayList();

    public static List<JsonModUpdate> searchForOutdatedModsInefficiently() throws IOException
    {
        List<JsonModUpdate> outdatedMods = Lists.newArrayList();

        for (JsonModUpdate mod : UpdateHelper.modList)
        {
            if (LLibraryConfigHandler.updateType == UpdateType.ALPHA && mod.getAlpha() != null)
            {
                if (!mod.currentVersion.equals(mod.getAlpha()))
                {
                    mod.updateType = UpdateType.ALPHA;
                    outdatedMods.add(mod);
                }
            }
            else if ((LLibraryConfigHandler.updateType == UpdateType.ALPHA || LLibraryConfigHandler.updateType == UpdateType.BETA) && mod.getBeta() != null)
            {
                if (!mod.currentVersion.equals(mod.getBeta()))
                {
                    mod.updateType = UpdateType.BETA;
                    outdatedMods.add(mod);
                }
            }
            else if (mod.getRelease() != null)
            {
                if (!mod.currentVersion.equals(mod.getRelease()))
                {
                    mod.updateType = UpdateType.RELEASE;
                    outdatedMods.add(mod);
                }
            }
            else if (mod.getNewestVersion() != null)
            {
                if (!mod.currentVersion.equals(mod.getNewestVersion()))
                {
                    mod.updateType = UpdateType.RELEASE;
                    outdatedMods.add(mod);
                }
            }
        }

        VersionHandler.outdatedMods = outdatedMods;
        return outdatedMods;
    }

    public static List<JsonModUpdate> searchForOutdatedMods()
    {
        try
        {
            return searchForOutdatedModsInefficiently();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Lists.newArrayList();
    }

    public static List<JsonModUpdate> getOutdatedMods()
    {
        return VersionHandler.outdatedMods;
    }
}