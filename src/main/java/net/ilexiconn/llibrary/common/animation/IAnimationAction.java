package net.ilexiconn.llibrary.common.animation;

import net.minecraft.entity.Entity;

public interface IAnimationAction
{
    void execute(int animationId, Entity entity);
}
