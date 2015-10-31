package net.ilexiconn.llibrary.common.block;

import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author iLexiconn
 * @since 0.1.0
 */
public abstract class BlockMountable extends BlockContainer
{
    private float mountPosX = 0.5f;
    private float mountPosY = 1f;
    private float mountPosZ = 0.5f;

    public BlockMountable(Material material)
    {
        super(material);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            List<EntityMountableBlock> mountableBlocks = world.getEntitiesWithinAABB(EntityMountableBlock.class, AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 1f).expand(1f, 1f, 1f));
            for (EntityMountableBlock mountableBlock : mountableBlocks)
            {
                if (mountableBlock.blockPos == pos)
                {
                    return mountableBlock.interactFirst(player);
                }
            }

            float mountX = pos.getX() + mountPosX;
            float mountY = pos.getY() + mountPosY;
            float mountZ = pos.getZ() + mountPosZ;

            EntityMountableBlock mountableBlock = new EntityMountableBlock(world, pos, mountX, mountY, mountZ);
            world.spawnEntityInWorld(mountableBlock);
            return mountableBlock.interactFirst(player);
        }

        return true;
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            List<EntityMountableBlock> mountableBlocks = world.getEntitiesWithinAABB(EntityMountableBlock.class, AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 1f).expand(1f, 1f, 1f));
            for (EntityMountableBlock mountableBlock : mountableBlocks)
            {
                mountableBlock.setDead();
            }
        }
    }

    public void setMountingPosition(float x, float y, float z)
    {
        mountPosX = x;
        mountPosY = y;
        mountPosZ = z;
    }
}