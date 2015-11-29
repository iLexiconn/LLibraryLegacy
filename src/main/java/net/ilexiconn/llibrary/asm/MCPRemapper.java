package net.ilexiconn.llibrary.asm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;

import org.objectweb.asm.commons.Remapper;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class MCPRemapper extends Remapper implements LineProcessor<Void> {
    private static final int DIR_GUESSES = 4;
    private static final int DIR_ASKS = 3;

    private HashMap<String, String> fields = new HashMap<String, String>();
    private HashMap<String, String> funcs = new HashMap<String, String>();

    public MCPRemapper() {
        File[] mappings = getConfFiles();
        try {
            Resources.readLines(mappings[1].toURI().toURL(), Charsets.UTF_8, this);
            Resources.readLines(mappings[2].toURI().toURL(), Charsets.UTF_8, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        String s = funcs.get(name);
        return s == null ? name : s;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        String s = fields.get(name);
        return s == null ? name : s;
    }

    @Override
    public boolean processLine(String line) throws IOException {
        int i = line.indexOf(',');
        String srg = line.substring(0, i);
        int i2 = i + 1;
        i = line.indexOf(',', i2);
        String mcp = line.substring(i2, i);
        (srg.startsWith("func") ? funcs : fields).put(srg, mcp);
        return true;
    }

    @Override
    public Void getResult() {
        return null;
    }

    public static File[] getConfFiles() {
        // Check for GradleStart system vars
        if (!Strings.isNullOrEmpty(System.getProperty("net.minecraftforge.gradle.GradleStart.srgDir"))) {
            File srgDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.srgDir"));
            File csvDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.csvDir"));

            if (srgDir.exists() && csvDir.exists()) {
                File srg = new File(srgDir, "notch-srg.srg");
                File fieldCsv = new File(csvDir, "fields.csv");
                File methodCsv = new File(csvDir, "methods.csv");

                if (srg.exists() && fieldCsv.exists() && methodCsv.exists()) {
                    return new File[] {srg, fieldCsv, methodCsv};
                }
            }
        }

        for (int i = 0; i < DIR_GUESSES + DIR_ASKS; i++) {
            File dir = confDirectoryGuess(i, LLibraryConfigHandler.asmMappingDir);
            if (dir == null || dir.isFile()) {
                continue;
            }
            
            File[] mappings;
            try {
                mappings = parseConfDir(dir);
            } catch (Exception e) {
                if (i >= DIR_GUESSES) {
                    e.printStackTrace();
                }
                continue;
            }

            ConfigHelper.getConfigContainer("llibrary").getConfiguration().get(Configuration.CATEGORY_GENERAL, "mappingDir", "").set(dir.getPath());
            return mappings;
        }

        throw new RuntimeException("Failed to select mappings directory, set it manually in the config");
    }

    public static File confDirectoryGuess(int i, String tag) {
        File mcDir = (File) FMLInjectionData.data()[6];
        switch (i) {
            case 0:
                return tag != "" ? new File(tag) : null;
            case 1:
                return new File(mcDir, "../conf");
            case 2:
                return new File(mcDir, "../build/unpacked/conf");
            case 3:
                return new File(System.getProperty("user.home"), ".gradle/caches/minecraft/net/minecraftforge/forge/" + FMLInjectionData.data()[4] + "-" + ForgeVersion.getVersion() + "/unpacked/conf");
            default:
                JFileChooser fc = new JFileChooser(mcDir);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setDialogTitle("Select an mcp conf dir for the deobfuscator.");
                int ret = fc.showDialog(null, "Select");
                return ret == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
        }
    }

    public static File[] parseConfDir(File confDir) {
        File srgDir = new File(confDir, "conf");
        if (!srgDir.exists()) {
            srgDir = confDir;
        }
        File srgs = new File(srgDir, "packaged.srg");
        if (!srgs.exists()) {
            srgs = new File(srgDir, "joined.srg");
        }
        if (!srgs.exists()) {
            throw new RuntimeException("Could not find packaged.srg or joined.srg");
        }

        File mapDir = new File(confDir, "mappings");
        if (!mapDir.exists()) {
            mapDir = confDir;
        }
        File methods = new File(mapDir, "methods.csv");
        if (!methods.exists()) {
            throw new RuntimeException("Could not find methods.csv");
        }
        File fields = new File(mapDir, "fields.csv");
        if (!fields.exists()) {
            throw new RuntimeException("Could not find fields.csv");
        }
        return new File[] {srgs, methods, fields};
    }
}
