package test;

import net.ilexiconn.llibrary.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.entity.multipart.IEntityMultiPart;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityTest extends EntityLiving implements IEntityMultiPart
{
    public EntityPart[] partArray;

    public EntityTest(World world)
    {
        super(world);

        partArray = new EntityPart[] {
                new EntityPart(this, 0f, 0f, 0f, 0.75f, 0.5f, 0.5f),
                new EntityPart(this, 0f, 0f, 1.2f, 0.55f, 1f, 1.5f),
                new EntityPart(this, 1f, 0f, 0f, 0.8f, 0.8f)
        };

        setSize(0.4f, 1.8f);
    }

    public EntityPart[] getParts()
    {
        return partArray;
    }
}
