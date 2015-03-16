package net.ilexiconn.llibrary.update;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.web.WebHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangelogHandler
{
	public static String[] getChangelog(ModUpdateContainer mod, String version) throws Exception
	{
		ArrayList<String> list = getVersionChangelog(mod, version);
		return list.toArray(new String[4096]);
	}
	
	private static ArrayList<String> getVersionChangelog(ModUpdateContainer mod, String version) throws Exception
	{
		ArrayList<String> astring = Lists.newArrayList();

        try
		{
			List<String> list = WebHelper.readPastebinAsList(mod.pastebinId);
			
			for (String string : list)
			{
				String s = mod.modid + "Log|" + version + ":";
				
				if (string.contains(s))
				{
					String s1 = string.substring(string.indexOf(s));
					String s2 = s1.substring(s.length());
					String[] astring1 = s2.split(" ENDLINE ");

                    Collections.addAll(astring, astring1);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return astring;
	}
	
	public static boolean hasModGotChangelogForVersion(ModUpdateContainer mod, String version) throws Exception
	{
		try
		{
			List<String> list = WebHelper.readPastebinAsList(mod.pastebinId);
			
			for (String string : list)
			{
				String s = mod.modid + "Log|" + version + ":";
				
				if (string.contains(s))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}