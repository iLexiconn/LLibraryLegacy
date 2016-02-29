package net.ilexiconn.llibrary.client.model.item;

import com.google.gson.*;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class ItemTransformVec3fDeserializer implements JsonDeserializer<ItemTransformVec3f> {
    private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    public ItemTransformVec3f deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
        Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
        vector3f1.scale(0.0625F);
        vector3f1.x = MathHelper.clamp_float(vector3f1.x, -1.5F, 1.5F);
        vector3f1.y = MathHelper.clamp_float(vector3f1.y, -1.5F, 1.5F);
        vector3f1.z = MathHelper.clamp_float(vector3f1.z, -1.5F, 1.5F);
        Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
        vector3f2.x = MathHelper.clamp_float(vector3f2.x, -4.0F, 4.0F);
        vector3f2.y = MathHelper.clamp_float(vector3f2.y, -4.0F, 4.0F);
        vector3f2.z = MathHelper.clamp_float(vector3f2.z, -4.0F, 4.0F);
        return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
    }

    private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue) {
        if (!jsonObject.has(key)) {
            return defaultValue;
        } else {
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonObject, key);

            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
            } else {
                float[] afloat = new float[3];

                for (int i = 0; i < afloat.length; ++i) {
                    afloat[i] = JsonUtils.getFloat(jsonarray.get(i), key + "[" + i + "]");
                }

                return new Vector3f(afloat[0], afloat[1], afloat[2]);
            }
        }
    }
}
