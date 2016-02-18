package net.ilexiconn.llibrary.common.asm.hook.json;

import java.lang.reflect.Type;

import net.ilexiconn.llibrary.common.asm.hook.Bait;
import net.ilexiconn.llibrary.common.asm.hook.BaitHead;
import net.ilexiconn.llibrary.common.asm.hook.BaitParameterInterception;
import net.ilexiconn.llibrary.common.json.JsonFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class BaitDeserializer implements JsonDeserializer<Bait> {
    @Override
    public Bait deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String type = object.get("head").getAsString();
        // TODO: implement extensible hook types
        if (type.equals("parameterIntercept")) {
            return JsonFactory.getGson().fromJson(json, BaitParameterInterception.class);
        }
        return JsonFactory.getGson().fromJson(json, BaitHead.class);
    }
}
