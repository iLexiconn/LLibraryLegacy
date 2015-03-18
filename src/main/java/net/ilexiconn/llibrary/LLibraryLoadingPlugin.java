package net.ilexiconn.llibrary;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.10")
public class LLibraryLoadingPlugin implements IFMLLoadingPlugin
{
	public static File modFile;
	
	@Override
	public String[] getASMTransformerClass()
	{
		//return null;
		return new String[]{LLibraryClassPatcher.class.getName()};
	}

	@Override
	public String getModContainerClass() 
	{
		return LLibrary.class.getName();
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		modFile = (File)data.get("coremodLocation");
		
		if(modFile == null)
		{
			System.out.println("[LLibrary] Mod file is missing. Either you are in a development environment, or things are not going to work!");
		}
	}

	@Override
	public String getAccessTransformerClass() 
	{
		return null;
	}
}
