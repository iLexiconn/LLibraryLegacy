package net.ilexiconn.llibrary.common.log;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;

/**
 * Class to help with usage of Log4J2
 *
 * @author Ry_dog101
 */
public class LoggerHelper
{
    public String modid;

    public LoggerHelper(String modid)
    {
        this.modid = modid;
    }

    public void sendLog(Level logLevel, Object object)
    {
        FMLLog.log(modid, logLevel, String.valueOf(object));
    }

    public void debug(Object object)
    {
        sendLog(Level.DEBUG, object);
    }

    public void error(Object object)
    {
        sendLog(Level.ERROR, object);
    }

    public void fatal(Object object)
    {
        sendLog(Level.FATAL, object);
    }

    public void info(Object object)
    {
        sendLog(Level.INFO, object);
    }

    public void off(Object object)
    {
        sendLog(Level.OFF, object);
    }

    public void trace(Object object)
    {
        sendLog(Level.TRACE, object);
    }

    public void warn(Object object)
    {
        sendLog(Level.WARN, object);
    }
}
