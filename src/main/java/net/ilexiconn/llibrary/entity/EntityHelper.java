package net.ilexiconn.llibrary.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityHelper
{
    static int startEntityId = 0;

    public static void registerEntity(EntityObject entity)
    {
        if (entity.doRegisterEgg()) registerEntity(entity.getEntityName(), entity.getEntityClass(), entity.getEggColorPrimary(), entity.getEggColorSecondary());
        else registerEntity(entity.getEntityName(), entity.getEntityClass());
    }

    public static void registerEntity(String entityName, Class<? extends Entity> entityClass)
    {
        int entityId = getUniqueEntityId();
        EntityRegistry.registerModEntity(entityClass, entityName, entityId, LLibrary.instance, 64, 1, true);
    }

    public static void registerEntity(String entityName, Class<? extends Entity> entityClass, int primaryColor, int secondaryColor)
    {
        int entityId = getUniqueEntityId();
        EntityRegistry.registerModEntity(entityClass, entityName, entityId, LLibrary.instance, 64, 1, true);
        EntityList.IDtoClassMapping.put(entityId, entityClass);
        EntityList.entityEggs.put(entityId, new EntityList.EntityEggInfo(entityId, primaryColor, secondaryColor));
    }

    private static int getUniqueEntityId()
    {
        do startEntityId++;
        while (EntityList.getStringFromID(startEntityId) != null);
        return startEntityId;
    }
}
