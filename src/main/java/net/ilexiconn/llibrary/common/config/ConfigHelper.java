package net.ilexiconn.llibrary.common.config;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.util.Map;

/**
 * Helper class for registering {@link net.minecraftforge.common.config.Configuration} for a specific {@link net.minecraftforge.fml.common.Mod}
 * 
 * @author iLexiconn
 * @see net.minecraftforge.common.config.Configuration
 * @see net.minecraftforge.fml.common.Mod
 * @since 0.1.0
 */
public class ConfigHelper
{
    private static Map<String, ConfigContainer> configHandlers = Maps.newHashMap();

    /**
     * Register a {@link net.ilexiconn.llibrary.common.config.IConfigHandler} for a specific {@link net.minecraftforge.fml.common.Mod}
     * <p>
     * {@link net.ilexiconn.llibrary.common.config.IConfigHandler#loadConfig(Configuration)} will be called every time the user clicks on 'Done'
     * 
     * @see net.ilexiconn.llibrary.common.config.IConfigHandler
     * @see net.minecraftforge.fml.Mod
     * @since 0.1.0
     */
    public static void registerConfigHandler(String modid, File location, IConfigHandler configHandler)
    {
        configHandlers.put(modid, new ConfigContainer(configHandler, new Configuration(location)));
        configHandler.loadConfig(getConfigContainer(modid).getConfiguration());
        getConfigContainer(modid).getConfiguration().save();
    }

    public static ConfigContainer getConfigContainer(String modid)
    {
        if (hasConfiguration(modid))
            return configHandlers.get(modid);
        else
            return null;
    }

    /**
     * Check if the given {@link net.minecraftforge.fml.common.Mod} has a {@link net.ilexiconn.llibrary.common.config.IConfigHandler} container
     * 
     * @see net.minecraftforge.fml.common.Mod
     * @since 0.1.0
     */
    public static boolean hasConfiguration(String modid)
    {
        return configHandlers.containsKey(modid);
    }

    public static void setProperty(String modid, String category, String name, String value, Property.Type type)
    {
        if (!hasConfiguration(modid))
            return;
        getConfigContainer(modid).getConfiguration().getCategory(category).put(name, new Property(name, value, type));
        FMLCommonHandler.instance().bus().post(new ConfigChangedEvent.OnConfigChangedEvent(modid, "", false, false));
    }
}
