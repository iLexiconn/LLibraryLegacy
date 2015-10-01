package net.ilexiconn.llibrary.common.animation;

public interface IAnimated
{
    Animation animation_none = new Animation(0, 0);

    void setAnimationTick(int tick);

    void setAnimation(Animation animation);

    int getAnimationTick();

    Animation getAnimation();

    Animation[] animations();
}
