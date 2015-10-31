package net.ilexiconn.llibrary.common.animation;

import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibraryAnimation;
import net.ilexiconn.llibrary.common.message.MessageLLibraryAnimationAction;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Map;

public class Animation
{
    public Map<Integer, IAnimationAction> actions = Maps.newHashMap();
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
        LLibrary.networkWrapper.sendToAll(new MessageLLibraryAnimation(animation.animationId, ((Entity) entity).getEntityId()));
    }

    public static void tickAnimations(IAnimated entity)
    {
        if (entity.getAnimation() == null) entity.setAnimation(entity.animations()[0]);
        else
        {
            if (entity.getAnimation().animationId != 0)
            {
                if (entity.getAnimationTick() == 0) sendAnimationPacket(entity, entity.getAnimation());
                if (entity.getAnimation().actions.containsKey(entity.getAnimationTick()))
                {
                    entity.getAnimation().actions.get(entity.getAnimationTick()).execute(entity.getAnimationTick(), (Entity) entity);
                    LLibrary.networkWrapper.sendToAll(new MessageLLibraryAnimationAction(entity.getAnimation().animationId, ((Entity) entity).getEntityId(), entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() < entity.getAnimation().duration)
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                if (entity.getAnimationTick() == entity.getAnimation().duration)
                {
                    entity.setAnimationTick(0);
                    entity.setAnimation(entity.animations()[0]);
                }
            }
        }
    }

    public void registerAction(int tick, IAnimationAction action)
    {
        actions.put(tick, action);
    }
}
