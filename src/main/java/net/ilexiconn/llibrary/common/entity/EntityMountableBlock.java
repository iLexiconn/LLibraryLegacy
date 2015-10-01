package net.ilexiconn.llibrary.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.block.BlockMountable
 * @since 0.1.0
 */
public class EntityMountableBlock extends Entity
{
    public BlockPos blockPos;
    public Block block;

    public EntityMountableBlock(World world)
    {
        super(world);
    }

    public EntityMountableBlock(World world, BlockPos pos, float mountX, float mountY, float mountZ)
    {
        super(world);
        noClip = true;
        preventEntitySpawning = true;
        blockPos = pos;
        block = world.getBlockState(blockPos).getBlock();

        setPosition(mountX, mountY, mountZ);
        setSize(0f, 0f);
    }

    public boolean interactFirst(EntityPlayer player)
    {
        if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != player) return true;
        else
        {
            if (!worldObj.isRemote) player.mountEntity(this);
            return true;
        }
    }

    public void onEntityUpdate()
    {
        if (!worldObj.isRemote)
        {
            worldObj.theProfiler.startSection("entityBaseTick");
            if (riddenByEntity == null || riddenByEntity.isDead) setDead();
            else if (worldObj.getBlockState(blockPos).getBlock() != block) interactFirst((EntityPlayer) riddenByEntity);
            ticksExisted++;
            worldObj.theProfiler.endSection();
        }
    }

    public void entityInit()
    {
        setSize(0f, 0f);
    }

    public void readEntityFromNBT(NBTTagCompound nbtTag)
    {

    }

    public void writeEntityToNBT(NBTTagCompound nbtTag)
    {

    }
}