package net.ilexiconn.llibrary.common.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.ilexiconn.llibrary.common.asm.hook.Bait;
import net.ilexiconn.llibrary.common.asm.hook.BaitHead;
import net.ilexiconn.llibrary.common.asm.hook.BaitParameterInterception;
import net.ilexiconn.llibrary.common.asm.hook.FishingPole;
import net.ilexiconn.llibrary.common.asm.hook.MethodSignature;
import net.ilexiconn.llibrary.common.asm.hook.json.BaitDeserializer;
import net.ilexiconn.llibrary.common.asm.hook.json.FishermanDeserializer;
import net.ilexiconn.llibrary.common.asm.hook.json.MethodSignatureDeserializer;
import net.ilexiconn.llibrary.common.asm.mappings.Mappings;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.json.JsonHelper;
import net.ilexiconn.llibrary.common.json.SubstitutionJsonReader;
import net.ilexiconn.llibrary.common.json.container.JsonUpdateEntry;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

@IFMLLoadingPlugin.Name("LLibrary")
@IFMLLoadingPlugin.MCVersion(MinecraftForge.MC_VERSION)
@IFMLLoadingPlugin.TransformerExclusions({"net.ilexiconn.llibrary.common.asm"})
public class LLibraryPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static LoggerHelper logger = new LoggerHelper("LLibraryUpdater");
    public static List<FishingPole> hookList = new ArrayList<FishingPole>();

    private static final Gson HOOK_GSON;

    static {
        GsonBuilder hookGsonBuilder = new GsonBuilder();
        hookGsonBuilder.registerTypeAdapter(FishingPole[].class, new FishermanDeserializer());
        hookGsonBuilder.registerTypeAdapter(Bait.class, new BaitDeserializer());
        hookGsonBuilder.registerTypeAdapter(MethodSignature.class, new MethodSignatureDeserializer());
        HOOK_GSON = hookGsonBuilder.create();
    }

    @Override
    public Void call() throws Exception {
        logger.info("Searching for mod updates");
        File mods = new File("mods").getAbsoluteFile();
        File file = new File("updatequeue.json").getAbsoluteFile();
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
                    JsonReader reader = new SubstitutionJsonReader(new InputStreamReader(zip.getInputStream(entry)));
                	hookList.addAll(Arrays.asList(HOOK_GSON.<FishingPole[]>fromJson(reader, FishingPole[].class)));
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
        return new String[]{"net.ilexiconn.llibrary.common.asm.ClassHeirachyManager", "net.ilexiconn.llibrary.common.asm.HookPatchManager"};
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
