package net.ilexiconn.llibrary.common.asm.mappings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Mappings {
    private static Map<String, String> map;

    public static void parseMappings(InputStream stream) throws IOException {
        map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("=");
            map.put(split[0], split[1]);
        }
        br.close();
    }

    public static String getClassMappings(String deobf) {
        deobf = deobf.replaceAll("\\.", "/");
        if (map == null || !map.containsKey(deobf)) {
            return deobf;
        } else {
            return map.get(deobf);
        }
    }

    public static String getMethodMappings(String deobf) {
        deobf = deobf.replaceAll("\\.", "/");
        if (map == null || !map.containsKey(deobf)) {
            String[] split = deobf.split("/");
            return split[split.length - 1];
        } else {
            return map.get(deobf);
        }
    }
}
