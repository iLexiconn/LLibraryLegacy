package net.ilexiconn.llibrary.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

import java.io.File;
import java.util.Map;

@MCVersion(value = "1.7.10")
public class LLibraryLoadingPlugin implements IFMLLoadingPlugin
{
    public static File modFile;

    public String[] getASMTransformerClass()
    {
        //return null;
        return new String[]{LLibraryClassPatcher.class.getName()};
    }

    public String getModContainerClass()
    {
        return null;
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map<String, Object> data)
    {
        modFile = (File) data.get("coremodLocation");

        if (modFile == null)
        {
            System.out.println("[LLibrary] Mod file is missing. Either you are in a development environment, or things are not going to work!");
        }
    }

    public String getAccessTransformerClass()
    {
        return null;
    }
}
