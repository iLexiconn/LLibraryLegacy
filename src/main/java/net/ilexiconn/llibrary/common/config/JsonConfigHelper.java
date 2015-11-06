package net.ilexiconn.llibrary.common.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class JsonConfigHelper {
    public static <T> T loadConfig(File f, Class<T> t) {
        if (!f.exists()) {
            try {
                return t.newInstance();
            } catch (Exception e) {
                LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Failed making a new instance of " + t.getName()));
                return null;
            }
        } else {
            try {
                return JsonFactory.getGson().fromJson(new FileReader(f), t);
            } catch (FileNotFoundException e) {
                LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Couldn't find config file " + f.getName())); //Huh?
                return null;
            }
        }
    }

    public static <T> T saveConfig(T t, File f) {
        String json = JsonFactory.getPrettyGson().toJson(t);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            IOUtils.write(json.getBytes(), new FileOutputStream(f));
        } catch (IOException e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Couldn't write to file " + f.getName()));
        }
        return t;
    }
}
