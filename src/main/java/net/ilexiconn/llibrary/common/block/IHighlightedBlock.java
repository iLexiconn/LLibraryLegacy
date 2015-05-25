package net.ilexiconn.llibrary.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface for blocks, implement to render all AxisAlignedBB in a list.
 *
 * @author iLexiconn
 */
public interface IHighlightedBlock
{
    @SideOnly(Side.CLIENT)
    List<AxisAlignedBB> getHighlightedBoxes(World world, BlockPos pos, EntityPlayer player);
}
