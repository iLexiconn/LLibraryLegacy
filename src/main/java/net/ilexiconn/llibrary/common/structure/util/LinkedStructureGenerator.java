package net.ilexiconn.llibrary.common.structure.util;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.structure.StructureGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class LinkedStructureGenerator
{
    /**
     * Constant index values for offset arrays
     */
    private static final byte X = 0, Y = 1, Z = 2;
    /**
     * List of structures to be generated
     */
    private final List<Structure> structures = Lists.newLinkedList();

    /**
     * Individual offsets for each structure
     */
    private final List<int[]> offsets = Lists.newLinkedList();

    /**
     * Individual rotation for each structure
     */
    private final List<Byte> rots = Lists.newLinkedList();
    /**
     * StructureGenerator that defines how custom hooks are handled
     */
    private StructureGeneratorBase gen = null;
    /**
     * Overall rotation for entire structure complex
     */
    private int rotation = 0;

    public LinkedStructureGenerator()
    {

    }

    /**
     * Specifies which 'StructureGenerator' class to use for generation, which in
     * turn determines how custom hooks are handled
     */
    public <T extends StructureGeneratorBase> void setGenerator(T generator)
    {
        gen = generator;
    }

    /**
     * Increments rotation for all linked structures
     */
    public void rotateStructures()
    {
        rotation = ++rotation % 4;
    }

    /**
     * Sets base rotation for all linked structures
     */
    public void setRotation(int rot)
    {
        rotation = rot % 4;
    }

    /**
     * Adds structure to list with no offset
     */
    public void addStructure(Structure structure)
    {
        addStructureWithOffset(structure, 0, 0, 0);
    }

    /**
     * Adds structure to list with offset from base structure location
     */
    public void addStructureWithOffset(Structure structure, int x, int y, int z)
    {
        addStructureWithOffsetAndRotation(structure, x, y, z, 0);
    }

    /**
     * Adds structure with offset and individual rotation
     */
    public void addStructureWithOffsetAndRotation(Structure structure, int x, int y, int z, int rot)
    {
        structures.add(structure);
        addOffset(x, y, z);
        rots.add((byte) (rot % 4));
    }

    private void addOffset(int x, int y, int z)
    {
        offsets.add(new int[]{-z, y, x});
    }

    /**
     * Sets the offset values for the last structure added; x and z are switched to maintain
     * +x moves forward, +z to right and -z to left relationships
     */
    public void setLastOffset(int x, int y, int z)
    {
        if (!structures.isEmpty())
        {
            if (offsets.size() < structures.size()) addOffset(x, y, z);
            else offsets.set(offsets.size() - 1, new int[]{-z, y, x});
        }
    }

    /**
     * Sets the individual rotation value for the last structure added
     */
    public void setLastRotation(int rot)
    {
        if (!rots.isEmpty()) rots.set(rots.size() - 1, (byte) (rot % 4));
    }

    /**
     * Generates all linked structures with overall orientation determined by first structure
     */
    public void generateLinkedStructures(World world, Random random, int x, int y, int z)
    {
        generateLinkedStructures(null, world, random, x, y, z);
    }

    /**
     * Generates all linked structures with overall orientation determined by player's facing or,
     * if player is null, by the first structure's default facing
     */
    public void generateLinkedStructures(EntityPlayer player, World world, Random random, int x, int y, int z)
    {
        int i = 0;
        if (structures.size() != offsets.size() || structures.size() != rots.size())
        {
            System.err.println("[Library] Structure List and Offset List are not the same size, aborting generation.");
            return;
        }
        if (gen == null) gen = new StructureGenerator();
        if (player != null) gen.setPlayerFacing(player);
        setOffsetFromRotation(player != null ? gen.getPlayerFacing() : -1);
        for (Structure structure : structures)
        {
            int[] offset = offsets.get(i);
            gen.setStructureWithRotation(structure, rotation + rots.get(i));
            gen.generate(world, random, x + offset[X], y + offset[Y] + structure.getOffsetY(), z + offset[Z]);
            ++i;
        }
    }

    /**
     * Adjusts offsetX and offsetZ amounts to compensate for player facing or, if player
     * was null (facing < 0), for number of manual rotations
     */
    private void setOffsetFromRotation(int facing)
    {
        int x, z;
        for (int[] offset : offsets)
        {
            for (int i = 0; i < (facing > 0 ? facing : 0); ++i)
            {
                x = -offset[Z];
                z = offset[X];
                offset[X] = x;
                offset[Z] = z;
            }
        }
    }
}
