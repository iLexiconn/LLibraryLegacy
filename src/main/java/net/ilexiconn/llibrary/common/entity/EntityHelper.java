package net.ilexiconn.llibrary.common.entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Class for registering entities, removing entities and getting entities.
 *
 * @author iLexiconn
 * @author Gegy1000
 * @author FiskFille
 * @author Ry_dog101
 * @since 0.1.0
 */
public class EntityHelper {
    private static Field classToIDMappingField;
    private static Field stringToIDMappingField;

    private static List<Class<? extends Entity>> removedEntities = Lists.newArrayList();

    static {
        int i = 0;

        for (Field field : EntityList.class.getDeclaredFields()) {
            if (field.getType() == Map.class) {
                if (i == 3) {
                    field.setAccessible(true);
                    classToIDMappingField = field;
                } else if (i == 4) {
                    field.setAccessible(true);
                    stringToIDMappingField = field;
                    break;
                }

                i++;
            }
        }
    }
    
    /**
     * Registers an entity for a mod with default tracking range and update frequency
     * 
     * @param entityName Name of the entity
     * @param entityClass Class of the entity
     * @param modEntityId A mod specific entity id
     * @param mod An instance of a mod to register the entity for
     */
    public static void registerEntity(String entityName, Class<? extends Entity> entityClass, int modEntityId, Object mod) {
        EntityRegistry.registerModEntity(entityClass, entityName, modEntityId, mod, 64, 1, true);
    }

    /**
     * Registers an entity for a mod with default tracking range and update frequency
     * 
     * @param entityName Name of the entity
     * @param entityClass Class of the entity
     * @param modEntityID A mod specific entity id
     * @param mod An instance of a mod to register the entity for
     * @param primaryEggColor Primary egg color
     * @param secondaryEggColor Secondary egg color
     */
    public static void registerEntity(String entityName, Class<? extends Entity> entityClass, int modEntityId, Object mod, int primaryEggColor, int secondaryEggColor) {
        EntityRegistry.registerModEntity(entityClass, entityName, modEntityId, mod, 64, 1, true, primaryEggColor, secondaryEggColor);
    }

    public static boolean hasEntityBeenRemoved(Class<? extends Entity> entity) {
        return removedEntities.contains(entity);
    }

    public static void removeLivingEntity(Class<? extends EntityLiving> clazz) {
        removeEntity(clazz);
        removeEntityEgg(clazz);

        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null) {
                EntityRegistry.removeSpawn(clazz, EnumCreatureType.AMBIENT, biome);
                EntityRegistry.removeSpawn(clazz, EnumCreatureType.CREATURE, biome);
                EntityRegistry.removeSpawn(clazz, EnumCreatureType.MONSTER, biome);
                EntityRegistry.removeSpawn(clazz, EnumCreatureType.WATER_CREATURE, biome);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeEntity(Class<? extends Entity> clazz) {
        removedEntities.add(clazz);

        EntityList.idToClassMapping.remove(clazz);

        Object name = EntityList.classToStringMapping.get(clazz);

        EntityList.stringToClassMapping.remove(name);
        EntityList.classToStringMapping.remove(clazz);

        try {
            Map<Class<? extends Entity>, Integer> classToIDMapping = (Map<Class<? extends Entity>, Integer>) classToIDMappingField.get(null);
            Map<String, Integer> stringToIDMapping = (Map<String, Integer>) stringToIDMappingField.get(null);
            classToIDMapping.remove(clazz);
            stringToIDMapping.remove(name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void removeEntityEgg(Class<? extends EntityLiving> clazz) {
        Integer toRemove = null;

        for (Object key : EntityList.entityEggs.keySet()) {
            Integer intKey = (Integer) key;

            Class<? extends Entity> entityClass = EntityList.getClassFromID(intKey);

            if (clazz.equals(entityClass)) {
                toRemove = intKey;

                break;
            }
        }

        if (toRemove != null) {
            EntityList.entityEggs.remove(toRemove);
        }
    }

    public static Entity getEntityFromClass(Class<? extends Entity> entityClass, World world) {
        Entity entity = null;

        try {
            entity = entityClass.getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }
}
