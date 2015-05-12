package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;

import java.net.URL;
import java.util.ArrayList;

/**
 * Helper class to register a mod for automatic update checking.
 *
 * @author FiskFille
 */
public class UpdateHelper
{
    public static ArrayList<ModUpdateContainer> modList = Lists.newArrayList();

    /**
     * Register the main mod class for automatic update checking.
     * <p/>
     * Example pastebin version file:
     * <p/>
     * fiskutils|:1.0.1
     * fiskutilsLog|1.0.0:* Released mod.
     * fiskutilsLog|1.0.1:* Updated to 1.7.10.
     *
     * @param mod        the main mod instance
     * @param pastebinId the paste id
     * @param website    the update website
     */
    public static void registerUpdateChecker(Object mod, String pastebinId, String website)
    {
        ModUpdateContainer container = new ModUpdateContainer();

        Class<?> modClass = mod.getClass();

        if (!modClass.isAnnotationPresent(Mod.class)) return;

        Mod annotation = modClass.getAnnotation(Mod.class);

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

    public static ModUpdateContainer getModContainerById(String modid)
    {
        for (ModUpdateContainer mod : modList)
        {
            if (mod.modid.equals(modid))
            {
                return mod;
            }
        }

        return null;
    }
}