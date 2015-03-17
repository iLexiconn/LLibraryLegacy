package net.ilexiconn.llibrary.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ilexiconn.llibrary.web.WebHelper;

public class VersionHandler
{
	private static List<ModUpdateContainer> outdatedMods = new ArrayList<ModUpdateContainer>();
	
	public static List<ModUpdateContainer> searchForOutdatedMods()
	{
		List<ModUpdateContainer> outdatedMods = new ArrayList<ModUpdateContainer>();
		
		for (ModUpdateContainer mod : UpdateHelper.modList)
		{
			try 
			{
				List<String> list = WebHelper.readPastebinAsList(mod.pastebinId);
				mod.updateFile = list;
				
				String version = getVersion(mod);
				
				if (!mod.version.equals(version))
				{
					mod.latestVersion = version;
					outdatedMods.add(mod);
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		VersionHandler.outdatedMods = outdatedMods;
		
		return outdatedMods;
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