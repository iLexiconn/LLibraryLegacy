package net.ilexiconn.llibrary.common.structure.util;

/**
 * Stores all data needed for post-gen processing, specifically for custom 'hooks'
 *
 * @author coolAlias
 */
public class BlockData
{
    private final int posX, posY, posZ, blockId, metadata, customData1, customData2;

    public BlockData(int x, int y, int z, int id, int meta, int data1, int data2)
    {
        posX = x;
        posY = y;
        posZ = z;
        blockId = id;
        metadata = meta;
        customData1 = data1;
        customData2 = data2;
    }

    public final int getPosX()
    {
        return this.posX;
    }

    public final int getPosY()
    {
        return this.posY;
    }

    public final int getPosZ()
    {
        return this.posZ;
    }

    public final int getBlockID()
    {
        return this.blockId;
    }

    public final int getMetaData()
    {
        return this.metadata;
    }

    public final int getCustomData1()
    {
        return this.customData1;
    }

    public final int getCustomData2()
    {
        return this.customData2;
    }
}
