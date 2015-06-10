package net.ilexiconn.llibrary.common.config;

import net.minecraftforge.common.config.Configuration;

/**
 * Interface for loading configs using {@link net.ilexiconn.llibrary.common.config.ConfigHelper}
 * 
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.config.ConfigHelper
 * @see net.minecraftforge.common.config.Configuration
 * @since 0.1.0
 */
public interface IConfigHandler
{
    void loadConfig(Configuration config);
}
