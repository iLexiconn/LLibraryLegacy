package net.ilexiconn.llibrary.update;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;

import java.net.URL;
import java.util.ArrayList;

public class UpdateHelper
{
	public static ArrayList<ModUpdateContainer> modList = Lists.newArrayList();

    public static void registerUpdateChecker(Object mod, String pastebinId, String website)
	{
		ModUpdateContainer container = new ModUpdateContainer();
        if (!mod.getClass().isAnnotationPresent(Mod.class)) return;
		Mod annotation = mod.getClass().getAnnotation(Mod.class);
		
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