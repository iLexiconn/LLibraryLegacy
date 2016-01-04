package net.ilexiconn.llibrary.common.asm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.commons.Remapper;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ObfRemapper extends Remapper {
    private HashMap<String, String> fields = new HashMap<String, String>();
    private HashMap<String, String> funcs = new HashMap<String, String>();

    @SuppressWarnings("unchecked")
    public ObfRemapper() {
        try {
            Field rawFieldMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawFieldMaps");
            Field rawMethodMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawMethodMaps");
            rawFieldMapsField.setAccessible(true);
            rawMethodMapsField.setAccessible(true);
            Map<String, Map<String, String>> rawFieldMaps = (Map<String, Map<String, String>>) rawFieldMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
            Map<String, Map<String, String>> rawMethodMaps = (Map<String, Map<String, String>>) rawMethodMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);

            if (rawFieldMaps == null) {
                throw new IllegalStateException("codechicken.lib.asm.ObfMapping loaded too early. Make sure all references are in or after the asm transformer load stage");
            }

            for (Map<String, String> map : rawFieldMaps.values()) {
                for (Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().startsWith("field")) {
                        fields.put(entry.getValue(), entry.getKey().substring(0, entry.getKey().indexOf(':')));
                    }
                }
            }
            for (Map<String, String> map : rawMethodMaps.values()) {
                for (Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().startsWith("func")) {
                        funcs.put(entry.getValue(), entry.getKey().substring(0, entry.getKey().indexOf('(')));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public String map(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }

    public String unmap(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
    }

    public boolean isObf(String typeName) {
        return !map(typeName).equals(typeName) || !unmap(typeName).equals(typeName);
    }
}
