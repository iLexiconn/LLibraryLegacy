package net.ilexiconn.llibrary.entity.multipart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class EntityPart extends Entity
{
    public EntityLivingBase parent;

    public float radius;
    public float angleYaw;
    public float offsetY;

    public float damageMultiplier;

    public EntityPart(EntityLivingBase e, float r, float y, float o, float sizeX, float sizeY)
    {
        this(e, r, y, o, sizeX, sizeY, 1f);
    }

    public EntityPart(EntityLivingBase e, float r, float y, float o, float sizeX, float sizeY, float d)
    {
        super(e.worldObj);
        setSize(sizeX, sizeY);
        parent = e;

        radius = r;
        angleYaw =  (y + 90f) * ((float) Math.PI / 180f);
        offsetY = o;

        damageMultiplier = d;
    }

    public void onUpdate()
    {
        super.onUpdate();

        setLocationAndAngles(parent.posX + radius * Math.cos(parent.renderYawOffset * (Math.PI / 180f) + angleYaw),
                             parent.posY + offsetY,
                             parent.posZ + radius * Math.sin(parent.renderYawOffset * (Math.PI / 180f) + angleYaw),
                             0f, 0f);
    }

    public boolean canBeCollidedWith()
    {
        return true;
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

    public void applyEntityCollision(Entity entity)
    {
        if (entity.riddenByEntity != this && entity.ridingEntity != this)
        {
            double x = entity.posX - posX;
            double y = entity.posZ - posZ;
            double side = MathHelper.abs_max(x, y);

            if (side >= 0.009999999776482582d)
            {
                side = (double) MathHelper.sqrt_double(side);
                x /= side;
                y /= side;
                double d3 = 1d / side;

                if (d3 > 1d) d3 = 1d;

                x *= d3;
                y *= d3;
                x *= 0.05000000074505806d;
                y *= 0.05000000074505806d;
                x *= (double) (1f - entityCollisionReduction);
                y *= (double) (1f - entityCollisionReduction);
                parent.addVelocity(-x, 0d, -y);
                addVelocity(-x, 0d, -y);
                entity.addVelocity(x, 0d, y);
            }
        }
    }
}