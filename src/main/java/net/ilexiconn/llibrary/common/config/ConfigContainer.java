package net.ilexiconn.llibrary.common.config;

import net.minecraftforge.common.config.Configuration;

public class ConfigContainer
{
    private IConfigHandler configHandler;
    private Configuration configuration;

    public ConfigContainer(IConfigHandler handler, Configuration config)
    {
        configHandler = handler;
        configuration = config;
    }

    public IConfigHandler getConfigHandler()
    {
        return configHandler;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }
}
