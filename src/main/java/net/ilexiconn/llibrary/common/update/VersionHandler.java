package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.web.WebHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author FiskFille
 */
public class VersionHandler
{
    private static List<ModUpdateContainer> outdatedMods = Lists.newArrayList();
    
    public static List<ModUpdateContainer> searchForOutdatedModsInefficiently() throws IOException
    {
        List<ModUpdateContainer> outdatedMods = Lists.newArrayList();

        for (ModUpdateContainer mod : UpdateHelper.modList)
        {
            List<String> list = WebHelper.downloadTextFileList(mod.updateTextFileURL);
            mod.updateFile = list;

            String version = getVersion(mod);

            if (!mod.version.equals(version))
            {
                mod.latestVersion = version;
                outdatedMods.add(mod);
            }
        }

        VersionHandler.outdatedMods = outdatedMods;
        return outdatedMods;
    }
    
    public static List<ModUpdateContainer> searchForOutdatedMods()
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

    public static List<ModUpdateContainer> getOutdatedMods()
    {
        return VersionHandler.outdatedMods;
    }

    public static String getVersion(ModUpdateContainer mod)
    {
        try
        {
            for (String string : mod.updateFile)
            {
                if (string.contains(mod.modid))
                {
                    String string1 = string.substring(string.indexOf(mod.modid + "|"));
                    String[] astring = string1.split(":");

                    if (astring[0].equals(mod.modid + "|"))
                    {
                        return astring[1];
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}