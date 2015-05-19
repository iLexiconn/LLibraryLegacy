package net.ilexiconn.llibrary.common.update;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.web.WebHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Helper class to register a mod for automatic update checking.
 *
 * @author FiskFille, iLexiconn
 */
public class UpdateHelper
{
    public static ArrayList<JsonModUpdate> modList = Lists.newArrayList();

    /**
     * Register the main mod class for automatic update checking.
     * <p/>
     * Example pastebin version file:
     * <p/>
     *  {
     *      "newestVersion": "9000",
     *      "versions":
     *      {
     *          "0.1.0":
     *          [
     *              "Initial release"
     *          ],
     *          "9000":
     *          [
     *              "Added more awesomeness"
     *          ]
     *      },
     *      "updateUrl": "http://ilexiconn.net",
     *      "iconUrl": "http://ilexiconn.net/llibrary/data/llibrary_64.png"
     *  }
     *
     * @param mod        		the main mod instance
     * @param url               the updater file
     *
     * @throws IOException
     */
    public static void registerUpdateChecker(Object mod, String url) throws IOException
    {
        JsonModUpdate json = JsonFactory.getGson().fromJson(WebHelper.downloadTextFile(url), JsonModUpdate.class);
        Class<?> modClass = mod.getClass();

        if (!modClass.isAnnotationPresent(Mod.class)) throw new RuntimeException("Please register the updater in your main class.");
        
        Mod annotation = modClass.getAnnotation(Mod.class);

        try
        {
            json.modid = annotation.modid();
            json.currentVersion = annotation.version();
            json.name = annotation.name();
            json.thumbnail = WebHelper.downloadImage(json.getIconUrl());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        modList.add(json);
    }

    public static JsonModUpdate getModContainerById(String modid)
    {
        for (JsonModUpdate mod : modList)
        {
            if (mod.modid.equals(modid))
            {
                return mod;
            }
        }

        return null;
    }
}