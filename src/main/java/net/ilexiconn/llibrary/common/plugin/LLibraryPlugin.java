package net.ilexiconn.llibrary.common.plugin;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.config.JsonConfigHelper;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.json.container.JsonUpdate;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@IFMLLoadingPlugin.Name("LLibrary")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
public class LLibraryPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static LoggerHelper logger = new LoggerHelper("LLibraryUpdater");

    public Void call() throws Exception {
        logger.info("Searching for mod updates");
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
                FileDeleteStrategy.FORCE.delete(file);
            }
            else if (file.getName().equalsIgnoreCase("update.json"))
            {
                deleteModsFromFile(file, mods);
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

    public void addClasspath(File file) {
        try {
            ((LaunchClassLoader) LLibraryPlugin.class.getClassLoader()).addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Failed adding file " + file + " to the classpath"));
        }
    }

    public void deleteModsFromFile(File deleteFile, File dir) {
        JsonUpdate update = JsonConfigHelper.loadConfig(deleteFile, JsonUpdate.class);
        for (String s : update.delete) {
            File file = new File(dir, s);
            if (file.exists()) {
                try {
                    FileDeleteStrategy.FORCE.delete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            FileDeleteStrategy.FORCE.delete(deleteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
