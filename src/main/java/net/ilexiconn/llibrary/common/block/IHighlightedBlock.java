package net.ilexiconn.llibrary.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface for {@link net.minecraft.block.Block}, implement to render all {@link net.minecraft.util.AxisAlignedBB} in a list.
 *
 * @author iLexiconn
 * @see net.minecraft.util.AxisAlignedBB
 * @since 0.1.0
 */
public interface IHighlightedBlock
{
    /**
     * @see net.minecraft.util.AxisAlignedBB
     * @since 0.1.0
     */
    @SideOnly(Side.CLIENT)
    List<AxisAlignedBB> getHighlightedBoxes(World world, BlockPos pos, IBlockState state, EntityPlayer player);
}
