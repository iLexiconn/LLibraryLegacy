package net.ilexiconn.llibrary.common.plugin;

import com.google.gson.reflect.TypeToken;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.json.container.JsonUpdateEntry;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("LLibrary")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
public class LLibraryPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static LoggerHelper logger = new LoggerHelper("LLibraryUpdater");

    public Void call() throws Exception {
        logger.info("Searching for mod updates");
        File mods = new File("mods");
        File file = new File("updatequeue.json");
        if (file.exists()) {
            List<JsonUpdateEntry> updateQueue;
            try {
                updateQueue = JsonFactory.getGson().fromJson(new FileReader(file), new TypeToken<List<JsonUpdateEntry>>() {}.getType());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            for (JsonUpdateEntry entry : updateQueue) {
                File modFile = new File(mods, entry.getFile());
                if (modFile.exists()) {
                    logger.info("Deleting old mod jar " + modFile.getName() + " from mod " + entry.getName() + " (" + entry.getModid() + ")");
                    try {
                        FileDeleteStrategy.FORCE.delete(modFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                File mod = new File(mods, entry.getName() + "-" + entry.getVersion() + "-" + Loader.MC_VERSION + ".jar");
                logger.info("Downloading new mod jar " + mod.getName() + " for mod " + entry.getName() + " (" + entry.getModid() + ")");
                FileUtils.copyURLToFile(new URL(entry.getUrl()), mod);
            }
            try {
                System.gc();
                FileDeleteStrategy.FORCE.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String[] getASMTransformerClass() {
        return null;
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return getClass().getName();
    }

    public void injectData(Map<String, Object> data) {

    }

    public String getAccessTransformerClass() {
        return null;
    }
}
