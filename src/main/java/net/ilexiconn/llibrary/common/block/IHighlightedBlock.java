package net.ilexiconn.llibrary.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Interface for blocks, implement to render all AxisAlignedBB in a list.
 *
 * @author iLexiconn
 */
public interface IHighlightedBlock
{
    @SideOnly(Side.CLIENT)
    public List<AxisAlignedBB> getHighlightedBoxes(World world, int x, int y, int z, EntityPlayer player);
}
