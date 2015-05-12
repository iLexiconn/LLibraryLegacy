package net.ilexiconn.llibrary.common.json;

import net.ilexiconn.llibrary.common.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.common.json.container.JsonHitbox;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.minecraft.entity.EntityLivingBase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

    public static JsonTabulaModel parseTabulaModel(InputStream stream)
    {
        return JsonFactory.getGson().fromJson(new InputStreamReader(stream), JsonTabulaModel.class);
    }
}
