package net.ilexiconn.llibrary.common.structure.util;

import com.mojang.authlib.GameProfile;
import com.sun.istack.internal.NotNull;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.FakePlayer;

import java.util.*;

public abstract class StructureGeneratorBase extends WorldGenerator
{
    /**
     * Use this value to skip setting a block at an x,y,z coordinate for whatever reason.
     */
    public static final int SET_NO_BLOCK = Integer.MAX_VALUE;

    /**
     * The directional values associated with player facing:
     */
    public static final int SOUTH = 0, WEST = 1, NORTH = 2, EAST = 3;

    /**
     * Valid rotation types. Each type is handled like vanilla blocks of this kind.
     */
    public enum ROTATION
    {
        ANVIL, DOOR, GENERIC, PISTON_CONTAINER, QUARTZ, RAIL, REPEATER,
        SIGNPOST, SKULL, STAIRS, TRAPDOOR, VINE, WALL_MOUNTED, LEVER, WOOD
    }

    /**
     * Stores the direction this structure faces. Default is EAST.
     */
    private int structureFacing = EAST, manualRotations = 0;

    /**
     * Stores the player's facing for structure generation
     */
    private int facing;

    /**
     * Stores amount to offset structure's location in the world, if any.
     */
    private int offsetX = 0, offsetY = 0, offsetZ = 0;

    /**
     * When true all blocks will be set to air within the structure's area.
     */
    private boolean removeStructure = false;

    /**
     * A mapping of block ids to rotation type for handling rotation. Allows custom blocks to be added.
     */
    private static final Map<Integer, ROTATION> blockRotationData = new HashMap<Integer, ROTATION>();

    /**
     * Stores the data for current layer. See StructureArray.java for information on how create a blockArray.
     */
    private int[][][][] blockArray;

    /**
     * Stores a list of the structure to build, in 'layers' made up of individual blockArrays.
     */
    private final List<int[][][][]> blockArrayList = new LinkedList<int[][][][]>();

    /**
     * Stores blocks that need to be set post-generation, such as torches
     */
    private final List<BlockData> postGenBlocks = new LinkedList<BlockData>();

    private static final GameProfile generatorName = new GameProfile(UUID.fromString("54acf800-054d-11e4-9191-0800200c9a66"), "fake");

    /**
     * Basic constructor. Sets generator to notify other blocks of blocks it changes.
     */
    public StructureGeneratorBase()
    {
        super(true);
    }

    /**
     * Constructs the generator based on the player's facing and blockArray for the structure
     */
    public StructureGeneratorBase(Entity entity, int[][][][] blocks)
    {
        this(entity, blocks, EAST, 0, 0, 0);
    }

    /**
     * Constructs the generator with the player's facing, the blockArray for the structure
     * and the structure's facing
     */
    public StructureGeneratorBase(Entity entity, int[][][][] blocks, int structureFacing)
    {
        this(entity, blocks, structureFacing, 0, 0, 0);
    }

    /**
     * Constructor for one line setting of all variables necessary to generate structure
     *
     * @param blocks          The structure's blockArray
     * @param structureFacing The direction in which the structure faces
     * @param offX            Amount to offset the structure's location along the east-west axis
     * @param offY            Amount to offset the structure's location along the vertical axis
     * @param offZ            Amount to offset the structure's location along the north-south axis
     */
    public StructureGeneratorBase(Entity entity, int[][][][] blocks, int structureFacing, int offX, int offY, int offZ)
    {
        super(true);
        setPlayerFacing(entity);
        setBlockArray(blocks);
        setStructureFacing(structureFacing);
        setOffset(offX, offY, offZ);
    }

    /**
     * Allows the use of block ids greater than 4095 as custom 'hooks' to trigger onCustomBlockAdded
     *
     * @param fakeID      ID you use to identify your 'event'. Absolute value must be greater than 4095
     * @param customData1 Custom data may be used to subtype events for given fakeID
     *                    Returns the real id of the block to spawn in the world; must be <= 4095
     */
    public abstract int getRealBlockID(int fakeID, int customData1);

    /**
     * A custom 'hook' to allow setting of tile entities, spawning entities, etc.
     *
     * @param fakeID      The custom identifier used to distinguish between types
     * @param customData1 Custom data which can be used to subtype events for given fakeID
     * @param customData2 Additional custom data
     */
    public abstract void onCustomBlockAdded(World world, int x, int y, int z, int fakeID, int customData1, int customData2);

    /**
     * Maps a block id to a specified rotation type. Allows custom blocks to rotate with structure.
     *
     * @param blockID      a valid block id, 0 to 4095 (4096 total)
     * @param rotationType types predefined by enumerated type ROTATION
     * @return false if a rotation type has already been specified for the given blockID
     */
    public static boolean registerCustomBlockRotation(int blockID, ROTATION rotationType)
    {
        return registerCustomBlockRotation(blockID, rotationType, false);
    }

    /**
     * Maps a block id to a specified rotation type. Allows custom blocks to rotate with structure.
     *
     * @param blockID      a valid block id, 0 to 4095 (4096 total)
     * @param rotationType types predefined by enumerated type ROTATION
     * @param override     if true, will override the previously set rotation data for specified blockID
     * @return false if a rotation type has already been specified for the given blockID
     */
    public static boolean registerCustomBlockRotation(int blockID, ROTATION rotationType, boolean override)
    {
        if (Block.getBlockById(blockID) == null || blockID < 0 || blockID > 4095)
        {
            throw new IllegalArgumentException("[LLibrary] Error setting custom block rotation for block ID " + blockID + (Block.getBlockById(blockID) == null ? "; block was not found in Block.blocksList. Please register your block." : "Valid ids are (0-4095)"));
        }

        if (blockRotationData.containsKey(blockID))
        {
            if (override) blockRotationData.remove(blockID);
            else return false;
        }

        blockRotationData.put(blockID, rotationType);

        return true;
    }

    /**
     * Use this method to add an ItemStack to the first available slot in a TileEntity that
     * implements IInventory (and thus, by extension, ISidedInventory)
     *
     * @return true if entire itemstack was added
     */
    public static boolean addItemToTileInventory(World world, ItemStack itemstack, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile == null || !(tile instanceof IInventory))
        {
            System.out.println("[LLibrary] Tile Entity at " + x + "/" + y + "/" + z + " is " + (tile != null ? "not an IInventory" : "null"));
            return false;
        }

        if (itemstack.stackSize < 1)
        {
            System.out.println("[LLibrary] Trying to add ItemStack of size 0 to Tile Inventory");
            return false;
        }

        IInventory inventory = (IInventory) tile;
        int remaining = itemstack.stackSize;

        for (int i = 0; i < inventory.getSizeInventory() && remaining > 0; ++i)
        {
            ItemStack slotstack = inventory.getStackInSlot(i);

            if (slotstack == null && inventory.isItemValidForSlot(i, itemstack))
            {
                remaining -= inventory.getInventoryStackLimit();
                itemstack.stackSize = (remaining > 0 ? inventory.getInventoryStackLimit() : itemstack.stackSize);
                inventory.setInventorySlotContents(i, itemstack);
                inventory.markDirty();
            }
            else if (slotstack != null && itemstack.isStackable() && inventory.isItemValidForSlot(i, itemstack))
            {
                if (Item.getIdFromItem(slotstack.getItem()) == Item.getIdFromItem(itemstack.getItem()) && (!itemstack.getHasSubtypes() ||
                        itemstack.getItemDamage() == slotstack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, slotstack))
                {
                    int l = slotstack.stackSize + remaining;

                    if (l <= itemstack.getMaxStackSize() && l <= inventory.getInventoryStackLimit())
                    {
                        remaining = 0;
                        slotstack.stackSize = l;
                        inventory.markDirty();
                    }
                    else if (slotstack.stackSize < itemstack.getMaxStackSize() && itemstack.getMaxStackSize() <= inventory.getInventoryStackLimit())
                    {
                        remaining -= itemstack.getMaxStackSize() - slotstack.stackSize;
                        slotstack.stackSize = itemstack.getMaxStackSize();
                        inventory.markDirty();
                    }
                }
            }
        }

        return remaining < 1;
    }

    /**
     * Sets an entity's location so that it doesn't spawn inside of walls.
     * Automatically removes placeholder block at coordinates x/y/z.
     *
     * @return false if no suitable location found
     */
    public static boolean setEntityInStructure(World world, Entity entity, int x, int y, int z)
    {
        if (entity == null)
        {
            return false;
        }
        int i = 0, iMax = (entity.width > 1.0F ? 16 : 4);

        world.setBlockToAir(x, y, z);

        entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);

        while (entity.isEntityInsideOpaqueBlock() && i < iMax)
        {
            if (i == 4 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F)
            {
                entity.setLocationAndAngles(x, y, z, 90.0F, 0.0F);
            }
            else if (i == 8 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F)
            {
                entity.setLocationAndAngles(x, y, z, 180.0F, 0.0F);
            }
            else if (i == 12 && entity.isEntityInsideOpaqueBlock() && entity.width > 1.0F)
            {
                entity.setLocationAndAngles(x, y, z, 270.0F, 0.0F);
            }

            switch (i % 4)
            {
                case 0:
                    entity.setPosition(entity.posX + 0.5D, entity.posY, entity.posZ + 0.5D);
                    break;
                case 1:
                    entity.setPosition(entity.posX, entity.posY, entity.posZ - 1.0D);
                    break;
                case 2:
                    entity.setPosition(entity.posX - 1.0D, entity.posY, entity.posZ);
                    break;
                case 3:
                    entity.setPosition(entity.posX, entity.posY, entity.posZ + 1.0D);
                    break;
            }

            ++i;
        }
        if (entity.isEntityInsideOpaqueBlock())
        {
            entity.setPosition(entity.posX + 0.5D, entity.posY, entity.posZ + 0.5D);
            return false;
        }

        return true;
    }

    /**
     * Spawns an entity in the structure by using setEntityInStructure.
     *
     * @return true if entity spawned without collision (entity will still spawn if false, but may be in a wall)
     */
    public static boolean spawnEntityInStructure(World world, Entity entity, int x, int y, int z)
    {
        if (world.isRemote || entity == null)
        {
            return false;
        }

        boolean collided = setEntityInStructure(world, entity, x, y, z);
        world.spawnEntityInWorld(entity);

        return collided;
    }

    /**
     * Returns an AxisAlignedBB suitable for a hanging entity at x/y/z facing direction
     */
    public static AxisAlignedBB getHangingEntityAxisAligned(int x, int y, int z, int direction)
    {
        double minX = (double) x, minZ = (double) z, maxX = minX, maxZ = minZ;

        switch (direction)
        {
            case 2: // frame facing NORTH
                minX += 0.25D;
                maxX += 0.75D;
                minZ += 0.5D;
                maxZ += 1.5D;
                break;
            case 3: // frame facing SOUTH
                minX += 0.25D;
                maxX += 0.75D;
                minZ -= 0.5D;
                maxZ += 0.5D;
                break;
            case 4: // frame facing WEST
                minX += 0.5D;
                maxX += 1.5D;
                minZ += 0.25D;
                maxZ += 0.75D;
                break;
            case 5: // frame facing EAST
                minX -= 0.5D;
                maxX += 0.5D;
                minZ += 0.25D;
                maxZ += 0.75D;
                break;
        }

        return AxisAlignedBB.getBoundingBox(minX, (double) y, minZ, maxX, (double) y + 1, maxZ);
    }

    /**
     * Places a hanging item entity in the world at the correct location and facing.
     * Note that you MUST use a WALL_MOUNTED type block id (such as torch) for your custom
     * block id's getRealBlockID return value in order for orientation to be correct.
     * Coordinates x,y,z are the location of the block used to spawn the entity
     * NOTE: Automatically removes the dummy block at x/y/z before placing the entity, so the
     * metadata stored in the block will no longer be available, but will be returned by this
     * method so it can be stored in a local variable for later use.
     *
     * @param hanging Must be an instance of ItemHangingEntity, such as Items.painting
     * @return Returns direction for further processing such as for ItemFrames, or -1 if no entity set
     */
    public static int setHangingEntity(World world, ItemStack hanging, int x, int y, int z)
    {
        if (hanging.getItem() == null || !(hanging.getItem() instanceof ItemHangingEntity))
        {
            return -1;
        }

        if (world.getBlockMetadata(x, y, z) < 1 || world.getBlockMetadata(x, y, z) > 5)
        {
            return -1;
        }

        int[] metaToFacing = {5, 4, 3, 2};
        int direction = metaToFacing[world.getBlockMetadata(x, y, z) - 1];
        FakePlayer player = new FakePlayer((WorldServer) world, generatorName);

        world.setBlockToAir(x, y, z);

        switch (direction)
        {
            case 2:
                ++z;
                break; // frame facing NORTH
            case 3:
                --z;
                break; // frame facing SOUTH
            case 4:
                ++x;
                break; // frame facing WEST
            case 5:
                --x;
                break; // frame facing EAST
        }

        hanging.getItem().onItemUse(hanging, player, world, x, y, z, direction, 0, 0, 0);

        return direction;
    }

    /**
     * Set's the itemstack contained in ItemFrame at x/y/z with default rotation.
     *
     * @param direction Use the value returned from the setHangingEntity method
     */
    public static void setItemFrameStack(World world, ItemStack itemstack, int x, int y, int z, int direction)
    {
        setItemFrameStack(world, itemstack, x, y, z, direction, 0);
    }

    /**
     * Set's the itemstack contained in ItemFrame at x/y/z with specified rotation.
     *
     * @param direction    Use the value returned from the setHangingEntity method
     * @param itemRotation 0,1,2,3 starting at default and rotating 90 degrees clockwise
     */
    public static void setItemFrameStack(World world, ItemStack itemstack, int x, int y, int z, int direction, int itemRotation)
    {
        List<EntityItemFrame> frames = world.getEntitiesWithinAABB(EntityItemFrame.class, getHangingEntityAxisAligned(x, y, z, direction));
        if (frames != null && !frames.isEmpty())
        {
            for (EntityItemFrame frame : frames)
            {
                frame.setDisplayedItem(itemstack);
                frame.setItemRotation(itemRotation);
            }
        }
    }

    /**
     * Sets the art for a painting at location x/y/z and sends a packet to update players.
     *
     * @param direction Use the value returned from the setHangingEntity method
     * @return false if 'name' didn't match any EnumArt values.
     */
    public static boolean setPaintingArt(World world, String name, int x, int y, int z, int direction)
    {
        List<EntityPainting> paintings = world.getEntitiesWithinAABB(EntityPainting.class, getHangingEntityAxisAligned(x, y, z, direction));

        if (paintings != null && !paintings.isEmpty() && name.length() > 0)
        {
            for (EntityPainting toEdit : paintings)
            {
                EntityPainting.EnumArt[] aenumart = EntityPainting.EnumArt.values();

                for (EntityPainting.EnumArt enumart : aenumart)
                {
                    if (enumart.title.equals(name))
                    {
                        toEdit.art = enumart;
                        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                        if (server != null)
                        {
                            server.getConfigurationManager().sendToAllNear(x, y, z, 64, world.provider.dimensionId, new S10PacketSpawnPainting(toEdit));
                        }
                        else
                        {
                            System.out.println("[LLibrary] Attempt to send packet to all around without a server instance available");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds text to a sign in the world. Use EnumChatFormatting to set colors. Text of more
     * than 15 characters per line will be truncated automatically.
     *
     * @param text A String array of no more than 4 elements; additional elements will be ignored
     * @return false if no sign tile entity was found at x/y/z
     */
    public static boolean setSignText(World world, String[] text, int x, int y, int z)
    {
        TileEntitySign sign = (world.getTileEntity(x, y, z) instanceof TileEntitySign ? (TileEntitySign) world.getTileEntity(x, y, z) : null);

        if (sign != null)
        {
            for (int i = 0; i < sign.signText.length && i < text.length; ++i)
            {
                if (text[i] != null && text[i].length() > 15)
                {
                    sign.signText[i] = text[i].substring(0, 15);
                }
                else sign.signText[i] = text[i];
            }

            return true;
        }
        return false;
    }

    /**
     * Method to set skulls not requiring extra rotation data (i.e. wall-mounted skulls whose rotation is determined by metadata)
     */
    public static boolean setSkullData(World world, int type, int x, int y, int z)
    {
        return setSkullData(world, type, -1, x, y, z);
    }

    /**
     * Sets skull type and name for a TileEntitySkull at x/y/z
     *
     * @param type Type of skull: 0 Skeleton, 1 Wither Skeleton, 2 Zombie, 3 Human, 4 Creeper
     * @param rot  Sets the rotation for the skull if positive value is used
     * @return false if errors were encountered (i.e. incorrect tile entity at x/y/z)
     */
    public static boolean setSkullData(World world, int type, int rot, int x, int y, int z)
    {
        TileEntitySkull skull = (world.getTileEntity(x, y, z) instanceof TileEntitySkull ? (TileEntitySkull) world.getTileEntity(x, y, z) : null);

        if (skull != null)
        {
            skull.func_152107_a(3);

            if (rot > -1)
            {
                skull.func_145903_a(rot % 16);
            }

            return true;
        }
        return false;
    }

    /**
     * Returns facing value as set from player, or 0 if no facing was specified
     */
    public final int getPlayerFacing()
    {
        return facing;
    }

    /**
     * Sets the direction in which the player is facing. The structure will be generated
     * opposite of player view (so player will be looking at front when finished)
     */
    public final void setPlayerFacing(@NotNull Entity entity)
    {
        facing = MathHelper.floor_double((double) ((entity.rotationYaw * 4F) / 360f) + 0.5D) & 3;
    }

    /**
     * Sets the default direction the structure is facing. This side will always face the player
     * unless you manually rotate the structure with the rotateStructureFacing() method.
     */
    public final void setStructureFacing(int facing)
    {
        structureFacing = facing % 4;
    }

    /**
     * This will manually rotate the structure's facing 90 degrees clockwise.
     * Note that a different side will now face the player when generated.
     */
    public final void rotateStructureFacing()
    {
        structureFacing = ++structureFacing % 4;
        manualRotations = ++manualRotations % 4;
    }

    /**
     * Manually rotates the structure's facing a specified number of times.
     */
    public final void rotateStructureFacing(int rotations)
    {
        structureFacing = (structureFacing + rotations) % 4;
        manualRotations = (manualRotations + rotations) % 4;
    }

    /**
     * Returns a string describing current facing of structure
     */
    public final String currentStructureFacing()
    {
        return (structureFacing == EAST ? "East" : structureFacing == WEST ? "West" : structureFacing == NORTH ? "North" : "South");
    }

    /**
     * Adds a block array 'layer' to the list to be generated
     */
    public final void addBlockArray(int blocks[][][][])
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            blockArrayList.add(blocks);
            if (blockArray == null)
                blockArray = blocks;
        }
    }

    /**
     * Overwrites current list with the provided blockArray
     */
    public final void setBlockArray(int blocks[][][][])
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            blockArrayList.clear();
            blockArrayList.add(blocks);
            blockArray = blocks;
        }
    }

    /**
     * Adds all elements contained in the parameter list to the structure
     */
    public final void addBlockArrayList(List<int[][][][]> list)
    {
        blockArrayList.addAll(list);

        if (blockArray == null && list.size() > 0)
            blockArray = list.get(0);
    }

    /**
     * Overwrites current blockArrayList with list provided
     */
    public final void setBlockArrayList(List<int[][][][]> list)
    {
        blockArrayList.clear();
        blockArrayList.addAll(list);
        blockArray = (list.size() > 0 ? list.get(0) : null);
    }

    /**
     * Overwrites current Structure information with passed in structure
     * Sets structure facing to the default facing of the structure
     * Does NOT set offset for the structure
     */
    public final void setStructure(@NotNull Structure structure)
    {
        if (structure != null)
        {
            reset();
            setBlockArrayList(structure.blockArrayList());
            setStructureFacing(structure.getFacing());
        }
    }

    /**
     * Overwrites current Structure information with passed in structure and rotates it
     * a number of times starting from its default facing
     */
    public final void setStructureWithRotation(@NotNull Structure structure, int rotations)
    {
        setStructure(structure);
        manualRotations = 0;
        for (int i = 0; i < rotations % 4; ++i)
            rotateStructureFacing();
    }

    /**
     * Returns lowest structure layer's width along the x axis or 0 if no structure has been added
     */
    public final int getWidthX()
    {
        return blockArray != null ? blockArray[0].length : 0;
    }

    /**
     * Returns lowest structure layer's width along the z axis or 0 if no structure has been set
     */
    public final int getWidthZ()
    {
        return blockArray != null ? blockArray[0][0].length : 0;
    }

    /**
     * Returns current structure layer's height or 0 if no structure has been set
     */
    public final int getHeight()
    {
        return blockArray != null ? blockArray.length : 0;
    }

    /**
     * Returns the original facing for the structure
     */
    public final int getOriginalFacing()
    {
        return (structureFacing + (4 - manualRotations)) % 4;
    }

    /**
     * Returns true if the structure has been rotated onto the opposite axis from original
     */
    public final boolean isOppositeAxis()
    {
        return getOriginalFacing() % 2 != structureFacing % 2;
    }

    /**
     * Sets the amount by which to offset the structure's generated location in the world.
     * For advanced users only. Recommended to use setDefaultOffset() methods instead.
     */
    public final void setOffset(int offX, int offY, int offZ)
    {
        offsetX = offX;
        offsetY = offY;
        offsetZ = offZ;
    }

    /**
     * Call this only after setting the blockArray and immediately before generation.
     * Sets a default offset amount that will keep the entire structure's boundaries
     * from overlapping with the position spawned at, so it will never spawn on the player.
     */
    public final void setDefaultOffset()
    {
        setDefaultOffset(0, 0, 0);
    }

    /**
     * Sets offsets such that the structure always generates in front of the player,
     * regardless of structure facing, offset by parameters x/y/z.
     * Only call this method immediately before generation.
     * NOTE: If your structures y=0 layer has an area smaller than another part of the structure,
     * setting default offset will not work correctly.
     *
     * @param x Positive value spawns structure further away from player, negative closer to or behind
     * @param z Positive value spawns structure more to the right, negative to the left
     */
    public final void setDefaultOffset(int x, int y, int z)
    {
        boolean flagNS = getOriginalFacing() % 2 == 0;
        int length = flagNS ? getWidthX() : getWidthZ();
        int adj1 = length - (flagNS ? getWidthZ() : getWidthX());

        boolean flag1 = (flagNS ? (getWidthX() % 2 == 0 && adj1 % 2 == 1) || (getWidthX() % 2 == 1 && adj1 % 2 == -1) : (getWidthX() % 2 == 0 && adj1 % 2 == -1) || (getWidthX() % 2 == 1 && adj1 % 2 == 1));

        if (flag1 && !flagNS)
        {
            adj1 = -adj1;
        }

        int adj2 = (length + 1) % 2;
        int adj3 = adj1 % 2;
        int adj4 = adj1 / 2 + adj3;

        switch (getOriginalFacing())
        {
            case 0: // SOUTH
                offsetZ = x + length / 2 - (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 ? 0 : adj1 < 0 && flag1 ? adj3 : adj2) : manualRotations == 1 ? (adj3 == 0 ? adj2 : adj1 > 0 && flag1 ? adj3 : 0) : manualRotations == 2 ? adj1 / 2 + (adj3 == 0 || flag1 ? adj2 : adj3) : 0);
                offsetX = -z + (manualRotations == 0 ? adj2 + (adj3 > 0 && !flag1 ? adj4 : 0) : manualRotations == 1 ? (adj3 == 0 ? adj2 : flag1 ? (adj3 < 0 ? -adj3 : 0) : adj3) : manualRotations == 2 ? (adj3 > 0 && !flag1 ? adj4 : 0) : 0);
                break;
            case 1: // WEST
                offsetX = x + length / 2 - (manualRotations == 0 ? (flag1 ? -adj4 : adj1 / 2) : manualRotations == 2 ? (flag1 ? (adj1 > 0 ? -adj1 / 2 : -adj4) : adj1 / 2 + (adj3 == 0 ? adj2 : 0)) : manualRotations == 3 ? (adj3 == 0 || flag1 ? adj2 : -adj3) : 0);
                offsetZ = z + (manualRotations == 1 ? (adj3 < 0 && !flag1 ? adj4 : adj3 > 0 && flag1 ? (adj1 > 1 ? -adj1 / 2 : -adj4) : 0) + (adj3 == 0 ? -adj2 : 0) : manualRotations == 2 ? (adj3 == 0 || flag1 ? -adj2 : adj3) : manualRotations == 3 ? (adj3 < 0 && !flag1 ? adj4 : 0) : 0);
                break;
            case 2: // NORTH
                offsetZ = -x - length / 2 + (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 || flag1 ? adj2 : adj3) : manualRotations == 2 ? (flag1 ? adj4 : adj1 / 2) : manualRotations == 3 ? (adj3 == 0 || flag1 ? adj2 : 0) : 0);
                offsetX = z - (manualRotations == 0 ? (adj3 > 0 ? adj3 - adj2 : 0) : manualRotations == 2 ? (adj3 > 0 ? adj3 : adj2) : manualRotations == 3 ? (adj3 > 0 ? adj3 - adj2 : adj3 < 0 ? -adj3 : adj2) : 0);
                break;
            case 3: // EAST
                offsetX = -x - length / 2 + (manualRotations == 0 ? adj1 / 2 + (adj3 == 0 ? adj2 : flag1 ? -adj1 + (adj1 > 0 ? adj3 : 0) : 0) : manualRotations == 1 ? (adj3 == 0 || flag1 ? adj2 : -adj3) : manualRotations == 2 ? (flag1 ? -adj4 : adj1 / 2) : 0);
                offsetZ = -z - (manualRotations == 0 ? (adj3 == 0 || flag1 ? -adj2 : adj3) : manualRotations == 1 ? (adj3 != 0 && !flag1 ? adj4 : 0) : manualRotations == 3 ? (adj3 < 0 && !flag1 ? adj4 : adj3 > 0 && flag1 ? -adj4 : 0) + (adj3 == 0 ? -adj2 : flag1 && adj1 > 1 ? adj3 : 0) : 0);
                break;
        }

        offsetY = 1 + y;
    }

    /**
     * Toggles between generate and remove structure setting. Returns value for ease of reference.
     */
    public final boolean toggleRemoveStructure()
    {
        removeStructure = !removeStructure;
        return removeStructure;
    }

    /**
     * Sets remove structure to true or false
     */
    public final void setRemoveStructure(boolean value)
    {
        removeStructure = value;
    }

    /**
     * Returns true if the generator has enough information to generate a structure
     */
    public final boolean canGenerate()
    {
        return blockArrayList.size() > 0 || blockArray != null;
    }

    /**
     * Generates each consecutive blockArray in the current list at location posX, posZ,
     * with posY incremented by the height of each previously generated blockArray.
     */
    public final boolean generate(World world, Random random, int posX, int posY, int posZ)
    {
        if (world.isRemote || !canGenerate())
        {
            return false;
        }

        boolean generated = true;
        int rotations = ((isOppositeAxis() ? structureFacing + 2 : structureFacing) + facing) % 4;

        setOffsetFromRotation();

        for (int[][][][] blocks : blockArrayList)
        {
            if (!generated) break;
            this.blockArray = blocks;
            generated = generateLayer(world, posX, posY, posZ, rotations);
            offsetY += blocks.length;
        }

        if (generated)
            doPostGenProcessing(world);

        reset();

        return generated;
    }

    /**
     * Custom 'generate' method that generates a single 'layer' from the list of blockArrays
     */
    private boolean generateLayer(World world, int posX, int posY, int posZ, int rotations)
    {
        int centerX = blockArray[0].length / 2, centerZ = blockArray[0][0].length / 2;

        for (int y = (removeStructure ? blockArray.length - 1 : 0); (removeStructure ? y >= 0 : y < blockArray.length); y = (removeStructure ? --y : ++y))
        {
            for (int x = 0; x < blockArray[y].length; ++x)
            {
                for (int z = 0; z < blockArray[y][x].length; ++z)
                {
                    if (blockArray[y][x][z].length == 0 || blockArray[y][x][z][0] == SET_NO_BLOCK)
                        continue;

                    int rotX = posX, rotZ = posZ, rotY = posY + y + offsetY;

                    switch (rotations)
                    {
                        case 0: // Player is looking at the front of the default structure
                            rotX += x - centerX + offsetX;
                            rotZ += z - centerZ + offsetZ;
                            break;
                        case 1: // Rotate structure 90 degrees clockwise
                            rotX += -(z - centerZ + offsetZ);
                            rotZ += x - centerX + offsetX;
                            break;
                        case 2: // Rotate structure 180 degrees
                            rotX += -(x - centerX + offsetX);
                            rotZ += -(z - centerZ + offsetZ);
                            break;
                        case 3: // Rotate structure 270 degrees clockwise
                            rotX += z - centerZ + offsetZ;
                            rotZ += -(x - centerX + offsetX);
                            break;
                        default:
                            break;
                    }

                    int customData1 = (blockArray[y][x][z].length > 2 ? blockArray[y][x][z][2] : 0);
                    int fakeID = blockArray[y][x][z][0];
                    int realID = (Math.abs(fakeID) > 4095 ? getRealBlockID(fakeID, customData1) : fakeID);

                    if (removeStructure)
                    {
                        if (!removeBlockAt(world, fakeID, realID, rotX, rotY, rotZ, rotations))
                            return false;
                    }
                    else
                    {
                        if (Math.abs(realID) > 4095)
                        {
                            System.err.println("[LLibrary] Invalid block ID. Initial ID: " + fakeID + ", returned id from getRealID: " + realID);
                            continue;
                        }

                        int customData2 = (blockArray[y][x][z].length > 3 ? blockArray[y][x][z][3] : 0);
                        int meta = (blockArray[y][x][z].length > 1 ? blockArray[y][x][z][1] : 0);

                        setBlockAt(world, fakeID, realID, meta, customData1, customData2, rotX, rotY, rotZ);
                    }
                }
            }
        }

        return true;
    }

    /**
     * Handles setting block with fakeID at x/y/z in world.
     * Arguments should be those retrieved from blockArray
     */
    private void setBlockAt(World world, int fakeID, int realID, int meta, int customData1, int customData2, int x, int y, int z)
    {
        if (realID >= 0 || world.isAirBlock(x, y, z) || world.getBlock(x, y, z) != null
                && world.getBlock(x, y, z).getMaterial().blocksMovement())
        {
            if (blockRotationData.containsKey(realID))
                meta = getMetadata(Math.abs(realID), meta, facing);

            if (blockRotationData.containsKey(realID) && (blockRotationData.get(realID) == ROTATION.WALL_MOUNTED || blockRotationData.get(realID) == ROTATION.LEVER))
            {
                postGenBlocks.add(new BlockData(x, y, z, fakeID, meta, customData1, customData2));
            }
            else
            {
                world.setBlock(x, y, z, Block.getBlockById(realID), meta, 2);

                if (blockRotationData.containsKey(realID))
                    setMetadata(world, x, y, z, meta, facing);

                if (Math.abs(fakeID) > 4095)
                {
                    onCustomBlockAdded(world, x, y, z, fakeID, customData1, customData2);
                }
            }
        }
    }

    /**
     * Removes block at x/y/z and cleans up any items/entities that may be left behind
     * Returns false if realID is mismatched with world's blockID at x/y/z
     */
    private boolean removeBlockAt(World world, int fakeID, int realID, int x, int y, int z, int rotations)
    {
        int worldID = Block.getIdFromBlock(world.getBlock(x, y, z));

        if (realID == 0 || Block.getBlockById(worldID) == null || (realID < 0 && worldID != Math.abs(realID)))
        {
            return true;
        }
        else if (Math.abs(realID) == worldID || materialsMatch(realID, worldID)) // || Math.abs(fakeID) > 4095
        {
            world.setBlockToAir(x, y, z);
            List<Entity> list = world.getEntitiesWithinAABB(Entity.class, getHangingEntityAxisAligned(x, y, z, Direction.directionToFacing[rotations]).expand(1.0F, 1.0F, 1.0F));

            for (Entity entity : list)
            {
                if (!(entity instanceof EntityPlayer)) entity.setDead();
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * Returns true if material for realID matches or is compatible with worldID material
     */
    private boolean materialsMatch(int realID, int worldID)
    {
        return (Block.getBlockById(worldID).getMaterial() == Material.grass && Block.getBlockById(Math.abs(realID)).getMaterial() == Material.ground) || (Block.getBlockById(worldID).getMaterial().isLiquid() && Block.getBlockById(Math.abs(realID)).getMaterial() == Block.getBlockById(worldID).getMaterial()) || (Block.getBlockById(worldID).getMaterial() == Material.ice && Block.getBlockById(Math.abs(realID)).getMaterial() == Material.water) || (Block.getBlockById(worldID).getMaterial() == Material.piston && Block.getBlockById(Math.abs(realID)).getMaterial() == Material.piston) || (Block.getBlockById(worldID) instanceof BlockRedstoneTorch && Block.getBlockById(Math.abs(realID)) instanceof BlockRedstoneTorch) || (Block.getBlockById(worldID) instanceof BlockRedstoneRepeater && Block.getBlockById(Math.abs(realID)) instanceof BlockRedstoneRepeater);
    }

    /**
     * Sets blocks flagged for post-gen processing; triggers onCustomBlockAdded method where applicable
     */
    private void doPostGenProcessing(World world)
    {
        int fakeID, realID;

        for (BlockData block : postGenBlocks)
        {
            fakeID = block.getBlockID();
            realID = (Math.abs(fakeID) > 4095 ? getRealBlockID(fakeID, block.getCustomData1()) : fakeID);

            if (Math.abs(realID) > 4095)
            {
                System.err.println("[LLibrary] Invalid block ID. Initial ID: " + fakeID + ", returned id from getRealID: " + realID);
                continue;
            }

            if (realID >= 0 || world.isAirBlock(block.getPosX(), block.getPosY(), block.getPosZ()) ||
                    (world.getBlock(block.getPosX(), block.getPosY(), block.getPosZ()) != null
                            && !world.getBlock(block.getPosX(), block.getPosY(), block.getPosZ()).getMaterial().blocksMovement()))
            {
                world.setBlock(block.getPosX(), block.getPosY(), block.getPosZ(), Block.getBlockById(Math.abs(realID)), block.getMetaData(), 3);

                if (Math.abs(fakeID) > 4095)
                {
                    onCustomBlockAdded(world, block.getPosX(), block.getPosY(), block.getPosZ(), fakeID, block.getCustomData1(), block.getCustomData2());
                }
            }
        }

        postGenBlocks.clear();
    }

    /**
     * Fixes blocks metadata after they've been placed in the world, specifically for blocks
     * such as rails, furnaces, etc. whose orientation is automatically determined by the block
     * when placed via the onBlockAdded method.
     */
    private void setMetadata(World world, int x, int y, int z, int origMeta, int facing)
    {
        int id = Block.getIdFromBlock(world.getBlock(x, y, z));

        if (blockRotationData.get(id) == null) return;

        switch (blockRotationData.get(id))
        {
            case PISTON_CONTAINER:
                world.setBlockMetadataWithNotify(x, y, z, origMeta, 2);
                break;
            case RAIL:
                world.setBlockMetadataWithNotify(x, y, z, origMeta, 2);
                break;
            default:
                break;
        }
    }

    /**
     * This method will return the correct metadata value for the block type based on
     * how it was rotated in the world, IF and ONLY IF you used the correct metadata
     * value to set the block's default orientation for your structure's default facing.
     * <p>
     * If your structure's front faces EAST by default, for example, and you want a wall
     * sign out front greeting all your guests, you'd better use '5' as its metadata value
     * in your blockArray so it faces EAST as well.
     * <p>
     * Please read the blockArray notes very carefully and test out your structure to make
     * sure everything is oriented how you thought it was.
     */
    private int getMetadata(int id, int origMeta, int facing)
    {
        if (blockRotationData.get(id) == null) return 0;

        int rotations = ((isOppositeAxis() ? structureFacing + 2 : structureFacing) + facing) % 4;

        int meta = origMeta, bitface, tickDelay = meta >> 2, bit9 = meta >> 3,
                bit4 = meta & 4, bit8 = meta & 8, extra = meta & ~3;

        for (int i = 0; i < rotations; ++i)
        {
            bitface = meta % 4;

            switch (blockRotationData.get(id))
            {
                case ANVIL:
                    meta ^= 1;
                    break;
                case DOOR:
                    if (bit8 != 0) return meta;
                    meta = (bitface == 3 ? 0 : bitface + 1);
                    meta |= extra;
                    break;
                case GENERIC:
                    meta = (bitface == 3 ? 0 : bitface + 1) | bit4 | bit8;
                    break;
                case PISTON_CONTAINER:
                    meta -= meta > 7 ? 8 : 0;
                    if (meta > 1) meta = meta == 2 ? 5 : meta == 5 ? 3 : meta == 3 ? 4 : 2;
                    meta |= bit8 | bit9 << 3;
                    break;
                case QUARTZ:
                    meta = meta == 3 ? 4 : meta == 4 ? 3 : meta;
                    break;
                case RAIL:
                    if (meta < 2) meta ^= 1;
                    else if (meta < 6) meta = meta == 2 ? 5 : meta == 5 ? 3 : meta == 3 ? 4 : 2;
                    else meta = meta == 9 ? 6 : meta + 1;
                    break;
                case REPEATER:
                    meta = (bitface == 3 ? 0 : bitface + 1) | (tickDelay << 2);
                    break;
                case SIGNPOST:
                    meta = meta < 12 ? meta + 4 : meta - 12;
                    break;
                case SKULL:
                    meta = meta == 1 ? 1 : meta == 4 ? 2 : meta == 2 ? 5 : meta == 5 ? 3 : 4;
                    break;
                case STAIRS:
                    meta = (bitface == 0 ? 2 : bitface == 2 ? 1 : bitface == 1 ? 3 : 0) | bit4;
                    break;
                case TRAPDOOR:
                    meta = (bitface == 0 ? 3 : bitface == 3 ? 1 : bitface == 1 ? 2 : 0) | bit4 | bit8;
                    break;
                case VINE:
                    meta = meta == 1 ? 2 : meta == 2 ? 4 : meta == 4 ? 8 : 1;
                    break;
                case WALL_MOUNTED:
                    if (meta > 0 && meta < 5) meta = meta == 4 ? 1 : meta == 1 ? 3 : meta == 3 ? 2 : 4;
                    break;
                case LEVER:
                    meta -= meta > 7 ? 8 : 0;
                    if (meta > 0 && meta < 5) meta = meta == 4 ? 1 : meta == 1 ? 3 : meta == 3 ? 2 : 4;
                    else if (meta == 5 || meta == 6) meta = meta == 5 ? 6 : 5;
                    else meta = meta == 7 ? 0 : 7;
                    meta |= bit8;
                    break;
                case WOOD:
                    if (meta > 4 && meta < 12) meta = meta < 8 ? meta + 4 : meta - 4;
                    break;
                default:
                    break;
            }
        }

        return meta;
    }

    /**
     * Adjusts offsetX and offsetZ amounts to compensate for manual rotation
     */
    private void setOffsetFromRotation()
    {
        int x, z;

        for (int i = 0; i < manualRotations; ++i)
        {
            x = -offsetZ;
            z = offsetX;
            offsetX = x;
            offsetZ = z;
        }
    }

    /**
     * Clears blockArray, blockArrayList and offsets for next structure
     */
    private void reset()
    {
        blockArrayList.clear();
        blockArray = null;
        offsetX = offsetY = offsetZ = 0;
    }

    /** Set rotation data for vanilla blocks */
    static
    {
        blockRotationData.put(Block.getIdFromBlock(Blocks.anvil), ROTATION.ANVIL);

        blockRotationData.put(Block.getIdFromBlock(Blocks.iron_door), ROTATION.DOOR);
        blockRotationData.put(Block.getIdFromBlock(Blocks.wooden_door), ROTATION.DOOR);

        blockRotationData.put(Block.getIdFromBlock(Blocks.bed), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.cocoa), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.fence_gate), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.pumpkin), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.lit_pumpkin), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.end_portal_frame), ROTATION.GENERIC);
        blockRotationData.put(Block.getIdFromBlock(Blocks.tripwire_hook), ROTATION.GENERIC);

        blockRotationData.put(Block.getIdFromBlock(Blocks.chest), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.trapped_chest), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.dispenser), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.dropper), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.ender_chest), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.lit_furnace), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.furnace), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.hopper), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.ladder), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.wall_sign), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.piston), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.piston_extension), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.piston_head), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.sticky_piston), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.activator_rail), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.detector_rail), ROTATION.PISTON_CONTAINER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.golden_rail), ROTATION.PISTON_CONTAINER);

        blockRotationData.put(Block.getIdFromBlock(Blocks.quartz_ore), ROTATION.QUARTZ);

        blockRotationData.put(Block.getIdFromBlock(Blocks.rail), ROTATION.RAIL);

        blockRotationData.put(Block.getIdFromBlock(Blocks.powered_comparator), ROTATION.REPEATER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.unpowered_comparator), ROTATION.REPEATER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.powered_repeater), ROTATION.REPEATER);
        blockRotationData.put(Block.getIdFromBlock(Blocks.unpowered_repeater), ROTATION.REPEATER);

        blockRotationData.put(Block.getIdFromBlock(Blocks.standing_sign), ROTATION.SIGNPOST);

        blockRotationData.put(Block.getIdFromBlock(Blocks.skull), ROTATION.SKULL);

        blockRotationData.put(Block.getIdFromBlock(Blocks.brick_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.stone_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.nether_brick_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.quartz_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.sandstone_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.stone_brick_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.birch_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.jungle_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.oak_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.spruce_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.acacia_stairs), ROTATION.STAIRS);
        blockRotationData.put(Block.getIdFromBlock(Blocks.dark_oak_stairs), ROTATION.STAIRS);

        blockRotationData.put(Block.getIdFromBlock(Blocks.trapdoor), ROTATION.TRAPDOOR);

        blockRotationData.put(Block.getIdFromBlock(Blocks.vine), ROTATION.VINE);

        blockRotationData.put(Block.getIdFromBlock(Blocks.lever), ROTATION.LEVER);

        blockRotationData.put(Block.getIdFromBlock(Blocks.stone_button), ROTATION.WALL_MOUNTED);
        blockRotationData.put(Block.getIdFromBlock(Blocks.wooden_button), ROTATION.WALL_MOUNTED);
        blockRotationData.put(Block.getIdFromBlock(Blocks.redstone_torch), ROTATION.WALL_MOUNTED);
        blockRotationData.put(Block.getIdFromBlock(Blocks.unlit_redstone_torch), ROTATION.WALL_MOUNTED);
        blockRotationData.put(Block.getIdFromBlock(Blocks.torch), ROTATION.WALL_MOUNTED);

        blockRotationData.put(Block.getIdFromBlock(Blocks.log), ROTATION.WOOD);
    }
}