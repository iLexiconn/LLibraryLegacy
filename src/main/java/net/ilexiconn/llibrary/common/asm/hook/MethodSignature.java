package net.ilexiconn.llibrary.common.asm.hook;

import java.util.HashMap;
import java.util.Map;

import net.ilexiconn.llibrary.common.asm.ASMHelper;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

public class MethodSignature {
    private static final String VOID = "V";

    private static final ImmutableMap<String, String> PRIMITIVE_TYPE_MAP = ImmutableMap.<String, String> builder()
        .put("boolean", "Z")
        .put("byte", "B")
        .put("short", "S")
        .put("char", "C")
        .put("int", "I")
        .put("float", "F")
        .put("long", "J")
        .put("double", "D")
        .build();

    private static final ImmutableMap<String, String> BUILT_IN_TYPE_MAP = ImmutableMap.<String, String> builder()
        .put("string", "java.lang.String")
        .build();

    private String owner;

    private String name;

    private String[] parameters;

    private String description;

    public MethodSignature(String owner, String name, String[] parameters, String ret) {
        this.owner = owner;
        this.name = name;
        assignDescription(parameters, ret);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private String assignDescription(String[] parameters, String ret) {
        this.parameters = new String[parameters.length];
        StringBuilder desc = new StringBuilder("(");
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];
            desc.append(getType(parameter));
        }
        desc.append(')');
        desc.append(getReturnType(ret));
        return desc.toString();
    }

    private static String getType(String parameter) {
        int arrEnd = parameter.lastIndexOf('[') + 1;
        String arr = parameter.substring(0, arrEnd);
        String type = parameter.substring(arrEnd, parameter.length());
        String paramDesc;
        if (PRIMITIVE_TYPE_MAP.containsKey(type)) {
            paramDesc = PRIMITIVE_TYPE_MAP.get(type);
        } else {
            String clazz = Objects.firstNonNull(BUILT_IN_TYPE_MAP.get(type), type);
            // TODO: obfuscate
            paramDesc = ASMHelper.getClassAsParameter(clazz);
        }
        return arr + paramDesc;
    }

    private static String getReturnType(String ret) {
        return VOID.equals(ret) ? ret : getType(ret);
    }
}
