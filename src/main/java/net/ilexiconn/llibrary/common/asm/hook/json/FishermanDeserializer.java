package net.ilexiconn.llibrary.common.asm.hook.json;

import java.lang.reflect.Type;

import net.ilexiconn.llibrary.common.asm.ASMHelper;
import net.ilexiconn.llibrary.common.asm.hook.Bait;
import net.ilexiconn.llibrary.common.asm.hook.FishingPole;
import net.ilexiconn.llibrary.common.asm.hook.Hook;
import net.ilexiconn.llibrary.common.asm.hook.MethodSignature;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class FishermanDeserializer implements JsonDeserializer<FishingPole[]> {
    @Override
    public FishingPole[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement jsonDefaultCallee = object.get("defaultCallee");
        String defaultCallee;
        if (jsonDefaultCallee == null) {
            defaultCallee = null;
        } else {
            defaultCallee = jsonDefaultCallee.getAsString();
            if (!ASMHelper.isQualifiedClassName(defaultCallee)) {
                throw new JsonParseException("Expected a qualified class name for defaultCallee, instead got " + defaultCallee);
            }
        }
        JsonArray jsonHooks = object.get("hooks").getAsJsonArray();
        FishingPole[] poles = new FishingPole[jsonHooks.size()];
        for (int i = 0; i < jsonHooks.size(); i++) {
            JsonObject jsonPole = jsonHooks.get(i).getAsJsonObject();
            String owner = jsonPole.get("owner").getAsString();
            ASMHelper.checkQualifiedClassName("owner", owner);
            JsonArray jsonMethods = jsonPole.get("methods").getAsJsonArray();
            Hook[] hooks = new Hook[jsonMethods.size()];
            for (int j = 0; j < jsonMethods.size(); j++) {
                JsonObject jsonMethod = jsonMethods.get(j).getAsJsonObject();
                MethodSignature signature = context.deserialize(jsonMethod.get("signature"), MethodSignature.class);
                signature.setOwner(owner);
                boolean includeCallerInInvocation = jsonMethod.get("includeCallerInInvocation").getAsBoolean();
                JsonElement jsonCallee = jsonMethod.get("callee");
                String callee;
                if (jsonCallee == null) {
                    if (defaultCallee == null) {
                        throw new NullPointerException();
                    } else {
                        callee = defaultCallee;
                    }
                } else {
                    callee = jsonCallee.getAsString();
                    ASMHelper.checkQualifiedClassName("callee", callee);
                }
                Bait bait = context.deserialize(jsonMethod.get("type"), Bait.class);
                hooks[j] = new Hook(signature, includeCallerInInvocation, callee, bait);
            }
            poles[i] = new FishingPole(owner, hooks);
        }
        return poles;
    }
}
