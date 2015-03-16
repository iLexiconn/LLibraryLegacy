package net.ilexiconn.llibrary.update;

import java.net.URL;
import java.util.ArrayList;

import net.ilexiconn.llibrary.IContentHandler;
import cpw.mods.fml.common.Mod;

public class UpdateHelper
{
	public static ArrayList<ModUpdateContainer> modList = new ArrayList<ModUpdateContainer>();
	
	public static void registerUpdateChecker(IContentHandler contentHandlers, String pastebinId, String website)
	{
		ModUpdateContainer container = new ModUpdateContainer();
		Mod annotation = (Mod)contentHandlers.getClass().getAnnotation(Mod.class);
		
		try
		{
			container.modid = annotation.modid();
			container.version = annotation.version();
			container.name = annotation.name();
			container.pastebinId = pastebinId;
			container.website = new URL(website);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		modList.add(container);
	}
}