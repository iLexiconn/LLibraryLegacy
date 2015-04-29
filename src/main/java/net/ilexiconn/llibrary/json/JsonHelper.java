package net.ilexiconn.llibrary.json;

import java.io.InputStreamReader;
import java.util.List;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.entity.animation.IModelAnimator;
import net.ilexiconn.llibrary.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.json.container.JsonHitbox;
import net.ilexiconn.llibrary.json.container.JsonTabulaModel;
import net.minecraft.entity.EntityLivingBase;

public class JsonHelper
{
    public static EntityPart[] parseHitboxList(EntityLivingBase parent, List<JsonHitbox> hitbox)
    {
        return parseHitboxList(parent, hitbox.toArray(new JsonHitbox[hitbox.size()]));
    }

    public static EntityPart[] parseHitboxList(EntityLivingBase parent, JsonHitbox[] hitbox)
    {
        EntityPart[] list = new EntityPart[hitbox.length];
        for (int i = 0; i < hitbox.length; i++) list[i] = parseHitbox(parent, hitbox[i]);
        return list;
    }

    public static EntityPart parseHitbox(EntityLivingBase parent, JsonHitbox hitbox)
    {
        return new EntityPart(parent, hitbox.getRadius(), hitbox.getAngleYaw(), hitbox.getOffsetY(), hitbox.getSizeX(), hitbox.getSizeY(), hitbox.getDamageMultiplier());
    }

    public static JsonTabulaModel parseTabulaModel(String file)
    {
        return JsonFactory.getGson().fromJson(new InputStreamReader(LLibrary.class.getResourceAsStream(file)), JsonTabulaModel.class);
    }
}
