package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;

import java.io.IOException;
import java.util.List;

/**
 * @author FiskFille, iLexiconn
 */
public class VersionHandler
{
    private static List<JsonModUpdate> outdatedMods = Lists.newArrayList();
    
    public static List<JsonModUpdate> searchForOutdatedModsInefficiently() throws IOException
    {
        List<JsonModUpdate> outdatedMods = Lists.newArrayList();

        for (JsonModUpdate mod : UpdateHelper.modList) if (!mod.getNewestVersion().equals(mod.currentVersion)) outdatedMods.add(mod);

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