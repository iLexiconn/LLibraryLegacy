package net.ilexiconn.llibrary.common.entity;

import net.minecraft.entity.Entity;

/**
 * Placeholder class for registering entities using the EntityHelper class.
 *
 * @author iLexiconn
 */
public class EntityObject
{
    private String entityName;
    private Class<? extends Entity> entityClass;

    private boolean registerEgg;
    private int eggColorPrimary;
    private int eggColorSecondary;

    public EntityObject(String n, Class<? extends Entity> c)
    {
        entityName = n;
        entityClass = c;

        registerEgg = false;
    }

    public EntityObject(String n, Class<? extends Entity> c, int c1, int c2)
    {
        entityName = n;
        entityClass = c;

        registerEgg = true;
        eggColorPrimary = c1;
        eggColorSecondary = c2;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public Class<? extends Entity> getEntityClass()
    {
        return entityClass;
    }

    public boolean doRegisterEgg()
    {
        return registerEgg;
    }

    public int getEggColorPrimary()
    {
        return eggColorPrimary;
    }

    public int getEggColorSecondary()
    {
        return eggColorSecondary;
    }
}
