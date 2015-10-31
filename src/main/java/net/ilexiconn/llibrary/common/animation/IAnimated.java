package net.ilexiconn.llibrary.common.animation;

public interface IAnimated
{
    Animation animation_none = new Animation(0, 0);

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] animations();
}
