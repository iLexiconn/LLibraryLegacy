package net.ilexiconn.llibrary.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

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
    List<AxisAlignedBB> getHighlightedBoxes(World world, int x, int y, int z, EntityPlayer player);
}
