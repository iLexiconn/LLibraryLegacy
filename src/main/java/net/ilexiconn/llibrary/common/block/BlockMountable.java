package net.ilexiconn.llibrary.common.block;

import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public abstract class BlockMountable extends BlockContainer
{
    private float mountPosX = 0.5f;
    private float mountPosY = 1f;
    private float mountPosZ = 0.5f;

    public BlockMountable(Material material)
    {
        super(material);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            List<EntityMountableBlock> mountableBlocks = world.getEntitiesWithinAABB(EntityMountableBlock.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f).expand(1f, 1f, 1f));
            for (EntityMountableBlock mountableBlock : mountableBlocks)
                if (mountableBlock.blockPosX == x && mountableBlock.blockPosY == y && mountableBlock.blockPosZ == z)
                    return mountableBlock.interactFirst(player);

            float mountX = x + mountPosX;
            float mountY = y + mountPosY;
            float mountZ = z + mountPosZ;

            EntityMountableBlock mountableBlock = new EntityMountableBlock(world, x, y, z, mountX, mountY, mountZ);
            world.spawnEntityInWorld(mountableBlock);
            return mountableBlock.interactFirst(player);
        }

        return true;
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        if (!world.isRemote)
        {
            List<EntityMountableBlock> mountableBlocks = world.getEntitiesWithinAABB(EntityMountableBlock.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f).expand(1f, 1f, 1f));
            for (EntityMountableBlock mountableBlock : mountableBlocks)
                mountableBlock.setDead();
        }
    }

    public void setMountingPosition(float x, float y, float z)
    {
        mountPosX = x;
        mountPosY = y;
        mountPosZ = z;
    }
}