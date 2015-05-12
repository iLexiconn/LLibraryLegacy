package net.ilexiconn.llibrary.common.config;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

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
        if (hasConfiguration(modid)) return configHandlers.get(modid);
        else return null;
    }

    public static boolean hasConfiguration(String modid)
    {
        return configHandlers.containsKey(modid);
    }

    public static void setProperty(String modid, String category, String name, String value, Property.Type type)
    {
        if (!hasConfiguration(modid)) return;
        getConfigContainer(modid).getConfiguration().getCategory(category).put(name, new Property(name, value, type));
        FMLCommonHandler.instance().bus().post(new ConfigChangedEvent.OnConfigChangedEvent(modid, "", false, false));
    }
}
