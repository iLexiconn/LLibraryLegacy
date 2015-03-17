package net.ilexiconn.llibrary.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

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

    public static void removeEntity(Class<? extends Entity> entityClass)
	{
		removeEntityMapping(entityClass);
		
		if(entityClass.isInstance(EntityLiving.class))
		{	
			Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>)entityClass;
			
			removeEntityEgg(livingClass);
			
			for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
			{
				if (biome != null)
				{
					EntityRegistry.removeSpawn(livingClass, EnumCreatureType.ambient, biome);
					EntityRegistry.removeSpawn(livingClass, EnumCreatureType.creature, biome);
					EntityRegistry.removeSpawn(livingClass, EnumCreatureType.monster, biome);
					EntityRegistry.removeSpawn(livingClass, EnumCreatureType.waterCreature, biome);
				}
			}
		}
	}
	
	private static void removeEntityMapping(Class<? extends Entity> entityClass)
	{
		EntityList.IDtoClassMapping.remove(entityClass);
		EntityList.stringToClassMapping.remove(EntityList.classToStringMapping.get(entityClass));
		EntityList.classToStringMapping.remove(entityClass);
	}
	
	public static void removeEntityEgg(Class<? extends EntityLiving> entityClass)
	{
		Integer toRemove = null;
		
		for (Object key : EntityList.entityEggs.keySet())
		{
            Integer intKey = (Integer) key;
			
			Class<? extends Entity> eClass = EntityList.getClassFromID(intKey);
			
			if(entityClass.equals(eClass))
			{
				toRemove = intKey;
				
				break;
			}
		}
		
		if (toRemove != null)
		{
			EntityList.entityEggs.remove(toRemove);
		}
	}
    
    private static int getUniqueEntityId()
    {
        do startEntityId++;
        while (EntityList.getStringFromID(startEntityId) != null);
        return startEntityId;
    }
}
