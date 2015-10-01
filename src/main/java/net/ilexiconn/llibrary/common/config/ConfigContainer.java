package net.ilexiconn.llibrary.common.config;

import net.minecraftforge.common.config.Configuration;

/**
 * Container class for {@link net.minecraftforge.common.config.Configuration}
 * 
 * @author iLexiconn
 * @see net.minecraftforge.common.config.Configuration
 * @since 0.1.0
 */
public class ConfigContainer
{
    private IConfigHandler configHandler;
    private Configuration configuration;

    public ConfigContainer(IConfigHandler handler, Configuration config)
    {
        configHandler = handler;
        configuration = config;
    }

    /**
     * @see net.ilexiconn.llibrary.common.config.IConfigHandler
     * @since 0.1.0
     */
    public IConfigHandler getConfigHandler()
    {
        return configHandler;
    }

    /**
     * @see net.minecraftforge.common.config.Configuration
     * @since 0.1.0
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }
}
