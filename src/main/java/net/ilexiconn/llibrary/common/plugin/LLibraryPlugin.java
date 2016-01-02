package net.ilexiconn.llibrary.common.plugin;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.ilexiconn.llibrary.common.asm.mappings.Mappings;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.json.container.JsonHook;
import net.ilexiconn.llibrary.common.json.container.JsonUpdateEntry;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@IFMLLoadingPlugin.Name("LLibrary")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
public class LLibraryPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static LoggerHelper logger = new LoggerHelper("LLibraryUpdater");
    public static List<JsonHook> hookList = new ArrayList<JsonHook>();

    @Override
    public Void call() throws Exception {
        logger.info("Searching for mod updates");
        File mods = new File("mods");
        File file = new File("updatequeue.json");
        if (file.exists()) {
            List<JsonUpdateEntry> updateQueue;
            try {
                updateQueue = JsonFactory.getGson().fromJson(new FileReader(file), new TypeToken<List<JsonUpdateEntry>>() {
                }.getType());
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
                        File mod = new File(mods, entry.getName() + "-" + entry.getVersion() + "-" + Loader.MC_VERSION + ".jar");
                        logger.info("Downloading new mod jar " + mod.getName() + " for mod " + entry.getName() + " (" + entry.getModid() + ")");
                        FileUtils.copyURLToFile(new URL(entry.getUrl()), mod);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                System.gc();
                FileDeleteStrategy.FORCE.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File mod : mods.listFiles()) {
            if (mod.getName().endsWith(".jar") || mod.getName().endsWith(".zip")) {
                ZipFile zip = new ZipFile(mod);
                ZipEntry entry = zip.getEntry("llibrary_hooks.json");
                if (entry != null) {
                    Collections.addAll(hookList, JsonFactory.getGson().fromJson(new InputStreamReader(zip.getInputStream(entry)), JsonHook[].class));
                }
                ZipEntry entry1 = zip.getEntry("mappings.txt");
                if (mod.getName().contains("llibrary") && entry1 != null) {
                    Mappings.parseMappings(zip.getInputStream(entry1));
                }
                zip.close();
            }
        }

        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"net.ilexiconn.llibrary.common.asm.HookPatchManager"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
