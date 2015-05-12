package net.ilexiconn.llibrary.common.json.container;

import net.ilexiconn.llibrary.common.json.JsonFactory;

public class JsonHitbox
{
    private float radius;
    private float angleYaw;
    private float offsetY;
    private float sizeX;
    private float sizeY;
    private float damageMultiplier;

    public float getRadius()
    {
        return radius;
    }

    public float getAngleYaw()
    {
        return angleYaw;
    }

    public float getOffsetY()
    {
        return offsetY;
    }

    public float getSizeX()
    {
        return sizeX;
    }

    public float getSizeY()
    {
        return sizeY;
    }

    public float getDamageMultiplier()
    {
        return damageMultiplier;
    }

    public String toString()
    {
        return JsonFactory.getGson().toJson(this);
    }
}
