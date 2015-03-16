package net.ilexiconn.llibrary.update;

import java.util.ArrayList;
import java.util.List;

import net.ilexiconn.llibrary.web.WebHelper;

public class VersionHandler
{
	public static List<ModUpdateContainer> getOutdatedMods()
	{
		List<ModUpdateContainer> outdatedMods = new ArrayList<ModUpdateContainer>();
		
		for (ModUpdateContainer mod : UpdateHelper.modList)
		{
			String version = getVersion(mod);
			
			if (!mod.version.equals(version))
			{
				outdatedMods.add(mod);
			}
		}
		return outdatedMods;
	}

	public static String getVersion(ModUpdateContainer mod)
	{
		try
		{
			List<String> list = WebHelper.readPastebinAsList(mod.pastebinId);
			
			for (String string : list)
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