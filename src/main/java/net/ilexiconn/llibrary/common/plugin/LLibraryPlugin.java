package net.ilexiconn.llibrary.common.plugin;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.config.JsonConfigHelper;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.json.container.JsonUpdate;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.FileUtils;
import sun.misc.URLClassPath;
import sun.net.util.URLUtil;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("LLibrary")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
public class LLibraryPlugin extends DummyModContainer implements IFMLLoadingPlugin, IFMLCallHook {
    public static LoggerHelper logger = new LoggerHelper("llibrary");

    public LLibraryPlugin() {
        super(new ModMetadata());
        getMetadata().modId = "llibraryplugin";
        getMetadata().name = "LLibrary Updater";
    }

    public Void call() throws Exception {
        File mcDir = (File) FMLInjectionData.data()[6];
        File tempMods = new File(mcDir, "tempmods");
        if (!tempMods.exists()) {
            return null;
        }
        File mods = new File(mcDir, "mods");
        for (File file : tempMods.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                logger.info("Updating jar: " + file.getName());
                FileUtils.copyFileToDirectory(file, mods);
                addClasspath(new File(mods, file.getName()));
                deleteModsFromFile(file);
            }
        }
        deleteModsFromFile(new File(tempMods, "data.json"));
        return null;
    }

    public String[] getASMTransformerClass() {
        return null;
    }

    public String getModContainerClass() {
        return getClass().getName();
    }

    public String getSetupClass() {
        return getClass().getName();
    }

    public void injectData(Map<String, Object> data) {

    }

    public String getAccessTransformerClass() {
        return null;
    }

    public void addClasspath(File file) {
        try {
            ((LaunchClassLoader) LLibraryPlugin.class.getClassLoader()).addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Failed adding file " + file + " to the classpath"));
        }
    }

    public void removeClasspath(File file) {
        try {
            ClassLoader cl = LLibraryPlugin.class.getClassLoader();
            URL url = file.toURI().toURL();
            Field ucp = URLClassLoader.class.getDeclaredField("ucp");
            Field loaders = URLClassPath.class.getDeclaredField("loaders");
            Field lmap = URLClassPath.class.getDeclaredField("lmap");
            ucp.setAccessible(true);
            loaders.setAccessible(true);
            lmap.setAccessible(true);
            URLClassPath urlClassPath = (URLClassPath) ucp.get(cl);
            Closeable loader = ((Map<String, Closeable>) lmap.get(urlClassPath)).remove(URLUtil.urlNoFragString(url));
            if (loader != null) {
                loader.close();
                ((List<?>) loaders.get(urlClassPath)).remove(loader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteModsFromFile(File deleteFile) {
        JsonUpdate update = JsonConfigHelper.loadConfig(deleteFile, JsonUpdate.class);
        for (String s : update.delete) {
            File file = new File(s);
            removeClasspath(file);
            if (file.exists()) {
                if (!file.delete()) {
                    file.deleteOnExit();
                }
            }
        }
        if (!deleteFile.delete()) {
            deleteFile.deleteOnExit();
        }
    }
}
