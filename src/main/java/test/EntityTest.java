package test;

import net.ilexiconn.llibrary.client.model.ChainBuffer;
import net.ilexiconn.llibrary.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.entity.multipart.IEntityMultiPart;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityTest extends EntityLiving implements IEntityMultiPart
{
    public EntityPart[] partArray;
    public int frame;
    public ChainBuffer tailBuffer = new ChainBuffer(5);

    public EntityTest(World world)
    {
        super(world);

        partArray = new EntityPart[]{
                new EntityPart(this, 1.3f, 0f, 0.4f, 0.4f, 0.5f, 2f),
                new EntityPart(this, 1.6f, 0f, 0.5f, 0.2f, 0.3f, 2f),
                new EntityPart(this, 1f, 0f, 0.4f, 0.5f, 0.6f, 2f),
                new EntityPart(this, 0.3f, 0f, 0.4f, 0.8f, 0.8f),
                new EntityPart(this, 0.6f, 60f, 0f, 0.3f, 0.8f),
                new EntityPart(this, 0.6f, -60f, 0f, 0.3f, 0.8f),
                new EntityPart(this, -0.4f, 0f, 0.4f, 0.9f, 0.9f),
                new EntityPart(this, 0.8f, 145f, 0f, 0.3f, 0.8f),
                new EntityPart(this, 0.8f, -145f, 0f, 0.3f, 0.8f)
        };

        setSize(0.5f, 0.2f);
    }

    public void onUpdate()
    {
        super.onUpdate();

        tailBuffer.calculateChainSwingBuffer(45f, 5, 4f, this);
        frame++;
    }

    public EntityPart[] getParts()
    {
        return partArray;
    }
}
