package net.ilexiconn.llibrary.common.animation;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Animation
{
    public int animationId;
    public int duration;

    public Animation(int id, int d)
    {
        animationId = id;
        duration = d;
    }

    public static void sendAnimationPacket(IAnimated entity, Animation animation)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        entity.setAnimation(animation);
        LLibrary.networkWrapper.sendToAll(new MessageLLibraryAnimation((byte) animation.animationId, ((Entity) entity).getEntityId()));
    }

    public static void tickAnimations(IAnimated entity)
    {
        if (entity.getAnimation() == null) entity.setAnimation(entity.animations()[0]);
        else
        {
            if (entity.getAnimation().animationId != 0)
            {
                if (entity.getAnimationTick() == 0) sendAnimationPacket(entity, entity.getAnimation());
                if (entity.getAnimationTick() < entity.getAnimation().duration) entity.setAnimationTick(entity.getAnimationTick() + 1);
                if (entity.getAnimationTick() == entity.getAnimation().duration)
                {
                    entity.setAnimationTick(0);
                    entity.setAnimation(entity.animations()[0]);
                }
            }
        }
    }
}
