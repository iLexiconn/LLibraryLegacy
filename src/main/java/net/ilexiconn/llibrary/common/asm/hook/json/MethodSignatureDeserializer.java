package net.ilexiconn.llibrary.common.asm.hook.json;

import java.lang.reflect.Type;

import net.ilexiconn.llibrary.common.asm.ASMHelper;
import net.ilexiconn.llibrary.common.asm.hook.MethodSignature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class MethodSignatureDeserializer implements JsonDeserializer<MethodSignature> {
    @Override
    public MethodSignature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement jsonOwner = object.get("owner");
        String owner;
        if (jsonOwner == null) {
            owner = null;
        } else {
            owner = jsonOwner.getAsString();
            ASMHelper.checkQualifiedClassName("owner", owner);
        }
        JsonElement jsonParameters = object.get("parameters");
        String[] parameters = jsonParameters == null ? new String[0] : (String[]) context.deserialize(jsonParameters, String[].class);
        for (String parameter : parameters) {
            ASMHelper.checkType("parameter", parameter);
        }
        return new MethodSignature(owner, object.get("name").getAsString(), parameters, "V");
    }
}
