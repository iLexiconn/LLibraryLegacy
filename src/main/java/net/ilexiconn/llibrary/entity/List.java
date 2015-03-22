package net.ilexiconn.llibrary.entity;

import net.minecraft.entity.Entity;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ry_dog101
 */
public class List
{
    /**
     * Provides a mapping between entity classes and a string
     */
    public static Map<String, Class> nameToClassMapping = new HashMap<String, Class>();
    /**
     * Provides a mapping between a string and an entity classes
     */
    public static Map<Class, String> classToNameMapping = new HashMap<Class, String>();
    /**
     * provides a mapping between an entityID and an Entity Class
     */
    public static Map<Integer, Class> IDtoClassMapping = new HashMap<Integer, Class>();
    /**
     * provides a mapping between an Entity Class and an entity ID
     */
    private static Map<Class, Integer> classToIDMapping = new HashMap<Class, Integer>();
    /**
     * Maps entity names to their numeric identifiers
     */
    private static Map<String, Integer> nameToIDMapping = new HashMap<String, Integer>();
    /** This is a HashMap of the Creative Entity Eggs/Spawners. */
    public static HashMap<Integer, EntityEggInfo> entityEggs = new LinkedHashMap<Integer, EntityEggInfo>();

    /**
     * @param entityClass Class of entity to be added to list
     * @param entityName  Name of enitty to be eadded to list
     * @param entityID    ID of entty to be added to list
     */
    public static void addToList(Class entityClass, String entityName, int entityID, int background, int forground)
    {
        if (nameToClassMapping.containsKey(entityName))
        {
            throw new IllegalArgumentException("ID is already registered: " + entityID);
        }
        else if (IDtoClassMapping.containsKey(entityID))
        {
            throw new IllegalArgumentException("ID is already registered: " + entityID);
        }
        else
        {
            nameToClassMapping.put(entityName, entityClass);
            classToNameMapping.put(entityClass, entityName);
            IDtoClassMapping.put(entityID, entityClass);
            classToIDMapping.put(entityClass, entityID);
            nameToIDMapping.put(entityName, entityID);
            entityEggs.put(entityID, new EntityEggInfo(entityID, background, forground));
        }
    }

    /**
     * @param entityName Entities Name
     */
    public static Class getClassFromName(String entityName)
    {
       return nameToClassMapping.get(entityName);
    }

    /**
     * @param entityClass Entities Class
     */
    public static String getNameFromClass(Class entityClass)
    {
        return classToNameMapping.get(entityClass);
    }

    /**
     * @param entityID Entities ID
     */
    public static Class getClassFromID(int entityID)
    {
        return IDtoClassMapping.get(entityID);
    }

    /**
     * @param entityClass Entities Class
     */
    public static int getIDFromClass(Class entityClass)
    {
        return classToIDMapping.get(entityClass);
    }

    /**
     * @param entityName Entities Name
     */
    public static int getIDFromName(String entityName)
    {
        return nameToIDMapping.get(entityName);
    }

    public static String getNameFromID(int entityID)
    {
        Class clazz = getClassFromID(entityID);
        return clazz != null ? getNameFromClass(clazz) : null;
    }

    /**
     * Create a new instance of an entity in the world by using an entity ID.
     */
    public static Entity createEntityByID(int p_75616_0_, World p_75616_1_)
    {
        Entity entity = null;

        try
        {
            Class oclass = getClassFromID(p_75616_0_);

            if (oclass != null)
            {
                entity = (Entity)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {p_75616_1_});
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return entity;
    }

    public static class EntityEggInfo
    {
        /** The entityID of the spawned mob */
        public final int spawnedID;
        /** Base color of the egg */
        public final int primaryColor;
        /** Color of the egg spots */
        public final int secondaryColor;

        public EntityEggInfo(int p_i1583_1_, int p_i1583_2_, int p_i1583_3_)
        {
            this.spawnedID = p_i1583_1_;
            this.primaryColor = p_i1583_2_;
            this.secondaryColor = p_i1583_3_;
        }
    }
}
