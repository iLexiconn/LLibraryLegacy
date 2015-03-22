package net.ilexiconn.llibrary.config;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

public class ConfigHelper
{
    private static Map<String, ConfigContainer> configHandlers = Maps.newHashMap();
    
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
    
    public static boolean hasConfiguration(String modid)
    {
        return configHandlers.containsKey(modid);
    }
}
