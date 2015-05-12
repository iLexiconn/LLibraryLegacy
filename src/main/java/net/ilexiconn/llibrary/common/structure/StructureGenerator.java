package net.ilexiconn.llibrary.common.structure;

import net.ilexiconn.llibrary.common.structure.util.StructureGeneratorBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * This class is responsible for handling custom block hooks used in your block arrays,
 * though currently it is here only as a place-holder.
 *
 * @author coolAlias
 */
public class StructureGenerator extends StructureGeneratorBase
{
    public StructureGenerator()
    {

    }

    public StructureGenerator(Entity entity, int[][][][] blocks)
    {
        super(entity, blocks);
    }

    public StructureGenerator(Entity entity, int[][][][] blocks, int structureFacing)
    {
        super(entity, blocks, structureFacing);
    }

    public StructureGenerator(Entity entity, int[][][][] blocks, int structureFacing, int offX, int offY, int offZ)
    {
        super(entity, blocks, structureFacing, offX, offY, offZ);
    }

    public int getRealBlockID(int fakeID, int customData1)
    {
        return 0;
    }

    public void onCustomBlockAdded(World world, int x, int y, int z, int fakeID, int customData1, int customData2)
    {

    }
}
