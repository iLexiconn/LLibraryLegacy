package net.ilexiconn.llibrary.entity;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.registry.EntityRegistry;

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

	public static void removeLivingEntity(Class<? extends Entity> clazz)
	{
		removeEntity(clazz);

		Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>)clazz;

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

	public static void removeEntity(Class<? extends Entity> clazz)
	{
		EntityList.IDtoClassMapping.remove(clazz);
		EntityList.stringToClassMapping.remove(EntityList.classToStringMapping.get(clazz));
		EntityList.classToStringMapping.remove(clazz);
	}

	public static void removeEntityEgg(Class<? extends EntityLiving> clazz)
	{
		Integer toRemove = null;

		for (Object key : EntityList.entityEggs.keySet())
		{
			EntityEggInfo eggInfo = (EntityEggInfo) EntityList.entityEggs.get(key);
			Integer intKey = (Integer) key;

			Class<? extends Entity> entityClass = EntityList.getClassFromID(intKey);

			if(clazz.equals(entityClass))
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
