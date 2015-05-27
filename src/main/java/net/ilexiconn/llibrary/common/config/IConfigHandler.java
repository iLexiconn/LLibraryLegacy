package net.ilexiconn.llibrary.common.config;

import net.minecraftforge.common.config.Configuration;

/**
 * Interface for loading configs using {@link net.ilexiconn.llibrary.common.config.ConfigHelper}
 * 
 * @see net.ilexiconn.llibrary.common.config.ConfigHelper
 * @see net.minecraftforge.common.config.Configuration
 * @author iLexiconn
 * @since 0.1.0
 */
public interface IConfigHandler
{
    void loadConfig(Configuration config);
}
