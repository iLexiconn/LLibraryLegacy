package net.ilexiconn.llibrary.common.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.web.WebHelper;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.Mod;

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
     * fiskutilsLog|1.0.0:* Released mod. ENDLINE * Made example changelog. 
     * fiskutilsLog|1.0.1:* Updated to 1.7.10. 
     *
     * @param mod        		the main mod instance
     * @param updateTextFileURL the paste id
     * @param website    		the update website
     * @param icon		 		a square mod icon
     * @param iconURL			the URL from which LLib should download the mod's icon. If null, it will use the local mod icon provided in parameter 'icon'.  
     */
    public static void registerUpdateChecker(Object mod, String updateTextFileURL, String website, String icon, String iconURL)
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
            container.updateTextFileURL = updateTextFileURL;
            container.website = new URL(website);
            
            if (iconURL == null)
            {
            	File dest = new File(LLibrary.class.getResource("LLibrary.class").toString().replace("%20", " ")).getParentFile().getParentFile().getParentFile().getParentFile();
            	File destFile = new File(String.valueOf(dest).substring(6) + "/" + icon);
            	InputStream logoResource = new FileInputStream(destFile);
            	
            	if (logoResource != null)
            	{
            		container.thumbnail = ImageIO.read(logoResource);
            	}
            }
            else
            {
            	container.thumbnail = WebHelper.downloadImage(iconURL);
            }
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