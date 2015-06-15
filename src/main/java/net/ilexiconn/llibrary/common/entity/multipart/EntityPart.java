package net.ilexiconn.llibrary.common.entity.multipart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

import java.util.List;

/**
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart
 */
public class EntityPart extends Entity
{
    public EntityLivingBase parent;

    public float radius;
    public float angleYaw;
    public float offsetY;

    public float damageMultiplier;

    public boolean solid;

    /**
     * @param e
     *            parent
     * @param r
     *            radius
     * @param y
     *            angle yaw
     * @param o
     *            y-offset
     * @param sizeX
     *            collision box x-size
     * @param sizeY
     *            collision box y-size
     */
    public EntityPart(EntityLivingBase e, float r, float y, float o, float sizeX, float sizeY)
    {
        this(e, r, y, o, sizeX, sizeY, 1f);
    }

    /**
     * @param e
     *            parent
     * @param r
     *            radius
     * @param y
     *            angle yaw
     * @param o
     *            y-offset
     * @param sizeX
     *            collision box x-size
     * @param sizeY
     *            collision box y-size
     * @param d
     *            damage multiplier
     */
    public EntityPart(EntityLivingBase e, float r, float y, float o, float sizeX, float sizeY, float d)
    {
        this(e, r, y, o, sizeX, sizeY, d, false);
    }

    /**
     * @param e
     *            parent
     * @param r
     *            radius
     * @param y
     *            angle yaw
     * @param o
     *            y-offset
     * @param sizeX
     *            collision box x-size
     * @param sizeY
     *            collision box y-size
     * @param d
     *            damage multiplier
     * @param s
     *            solid
     */
    public EntityPart(EntityLivingBase e, float r, float y, float o, float sizeX, float sizeY, float d, boolean s)
    {
        super(e.worldObj);
        setSize(sizeX, sizeY);
        parent = e;

        radius = r;
        angleYaw = (y + 90f) * ((float) Math.PI / 180f);
        offsetY = o;

        damageMultiplier = d;

        solid = s;
    }

    public void onUpdate()
    {
        super.onUpdate();

        setLocationAndAngles(parent.posX + radius * Math.cos(parent.renderYawOffset * (Math.PI / 180f) + angleYaw), parent.posY + offsetY, parent.posZ + radius * Math.sin(parent.renderYawOffset * (Math.PI / 180f) + angleYaw), 0f, 0f);

        if (!worldObj.isRemote)
            collideWithNearbyEntities();
    }

    public boolean canBePushed()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        return !isEntityInvulnerable() && parent.attackEntityFrom(source, damage * damageMultiplier);
    }

    public boolean isEntityEqual(Entity entity)
    {
        return this == entity || parent == entity;
    }

    public void entityInit()
    {

    }

    public void readEntityFromNBT(NBTTagCompound nbtTag)
    {

    }

    public void writeEntityToNBT(NBTTagCompound nbtTag)
    {

    }

    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return solid ? entity.boundingBox : null;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return solid ? boundingBox : null;
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public void collideWithNearbyEntities()
    {
        List entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224d, 0d, 0.20000000298023224d));

        if (entities != null && !entities.isEmpty())
        {
            for (Object object : entities)
            {
                Entity entity = (Entity) object;
                if (entity != parent && !(entity instanceof EntityPart) && entity.canBePushed())
                    entity.applyEntityCollision(parent);
            }
        }
    }
}