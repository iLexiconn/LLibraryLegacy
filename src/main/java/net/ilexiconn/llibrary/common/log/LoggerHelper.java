package net.ilexiconn.llibrary.common.log;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Class to help with usage of Log4J2
 *
 * @author Ry_dog101
 */
public class LoggerHelper {
    private String modid;

    public LoggerHelper(String modid) {
        this.modid = modid;
    }

    private void log(Level logLevel, Object object) {
        FMLLog.log(modid, logLevel, String.valueOf(object));
    }

    public void debug(Object object) {
        log(Level.DEBUG, object);
    }

    public void error(Object object) {
        log(Level.ERROR, object);
    }

    public void fatal(Object object) {
        log(Level.FATAL, object);
    }

    public void info(Object object) {
        log(Level.INFO, object);
    }

    public void off(Object object) {
        log(Level.OFF, object);
    }

    public void trace(Object object) {
        log(Level.TRACE, object);
    }

    public void warn(Object object) {
        log(Level.WARN, object);
    }
}
