package net.ilexiconn.llibrary.common.structure.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author coolAlias
 */
public class Structure
{
    /**
     * The name of this structure
     */
    public final String name;

    /**
     * The List of all blockArray layers necessary to complete the structure
     */
    private final List<int[][][][]> blockArrayList = new LinkedList();

    /**
     * Stores the direction this structure faces. Default is EAST.
     */
    private int facing = StructureGeneratorBase.EAST;

    /**
     * Stores default amount to offset structure's location in the world.
     */
    private int offsetX = 0, offsetY = 0, offsetZ = 0;

    /**
     * Constructor for unnamed structures
     */
    public Structure()
    {
        name = "";
    }

    /**
     * Constructor for named structures
     */
    public Structure(String n)
    {
        name = n;
    }

    /**
     * Returns the blockArray List for this structure
     */
    public final List<int[][][][]> blockArrayList()
    {
        return blockArrayList;
    }

    /**
     * Returns the structure's default facing
     */
    public final int getFacing()
    {
        return facing;
    }

    /**
     * Sets the default direction the structure is facing. This side will always face the player
     * unless you manually rotate the structure with the rotateStructureFacing() method.
     */
    public final void setFacing(int f)
    {
        facing = f;
    }

    /**
     * Adds a block array 'layer' to the list to be generated
     */
    public final void addBlockArray(int blocks[][][][])
    {
        blockArrayList.add(blocks);
    }

    /**
     * Adds all elements contained in the parameter list to the structure
     */
    public final void addBlockArrayList(List<int[][][][]> list)
    {
        blockArrayList.addAll(list);
    }

    /**
     * Returns lowest structure layer's width along the x axis or 0 if no structure has been added
     */
    public final int getWidthX()
    {
        return blockArrayList.size() > 0 ? blockArrayList.get(0)[0].length : 0;
    }

    /**
     * Returns lowest structure layer's width along the z axis or 0 if no structure has been set
     */
    public final int getWidthZ()
    {
        return blockArrayList != null ? blockArrayList.get(0)[0][0].length : 0;
    }

    /**
     * Returns structure's total height
     */
    public final int getHeight()
    {
        int sum = 0;
        for (int[][][][] blockArray : blockArrayList) sum += blockArray.length;

        return sum;
    }

    /**
     * Returns the structure's offset for the x axis
     */
    public final int getOffsetX()
    {
        return offsetX;
    }

    /**
     * Returns the structure's offset for the y axis
     */
    public final int getOffsetY()
    {
        return offsetY;
    }

    /**
     * Returns the structure's offset for the z axis
     */
    public final int getOffsetZ()
    {
        return offsetZ;
    }

    /**
     * This is how much the structure should be offset from default; i.e. sets the values
     * that should be passed in to StructureGeneratorBase.setDefaultOffset. Used, for
     * example, if your structure's front door is not in the center, but a few blocks
     * to the left and you want the door to spawn in front of the player, or if your
     * structure should always be spawned in the air, etc.
     */
    public final void setStructureOffset(int x, int y, int z)
    {
        offsetX = x;
        offsetZ = z;
        offsetY = y;
    }
}
