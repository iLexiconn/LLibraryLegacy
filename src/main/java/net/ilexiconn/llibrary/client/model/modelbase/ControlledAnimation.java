package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.util.MathHelper;

public class ControlledAnimation
{
    private double timer;
    private double duration;

    public ControlledAnimation(int d)
    {
        timer = 0;
        duration = (double) d;
    }

    public void setDuration(int d)
    {
        timer = 0;
        duration = (double) d;
    }

    public double getTimer()
    {
        return timer;
    }

    public void setTimer(int time)
    {
        timer = (double) time;

        if (timer > duration) timer = duration;
        else if (timer < 0) timer = 0;
    }

    public void resetTimer()
    {
        timer = 0;
    }

    public void increaseTimer()
    {
        if (timer < duration) timer++;
    }

    public void increaseTimer(int time)
    {
        if (timer + (double) time < duration) timer += (double) time;
        else timer = duration;
    }

    public void decreaseTimer()
    {
        if (timer > 0d) timer--;
    }

    public void decreaseTimer(int time)
    {
        if (timer - (double) time > 0d) timer -= (double) time;
        else timer = 0.0D;
    }

    public float getAnimationFraction()
    {
        return (float) (timer / duration);
    }

    public float getAnimationProgressSmooth()
    {
        if (timer > 0d)
        {
            if (timer < duration) return (float) (1d / (1d + Math.exp(4d - 8d * (timer / duration))));
            else return 1f;
        }
        return 0f;
    }

    public float getAnimationProgressSteep()
    {
        return (float) (1d / (1d + Math.exp(6d - 12d * (timer / duration))));
    }

    public float getAnimationProgressSin()
    {
        return MathHelper.sin(1.57079632679f * (float) (timer / duration));
    }

    public float getAnimationProgressSinSqrt()
    {
        float result = MathHelper.sin(1.57079632679f * (float) (timer / duration));
        return result * result;
    }

    public float getAnimationProgressSinToTen()
    {
        return (float) Math.pow((double) MathHelper.sin(1.57079632679f * (float) (timer / duration)), 10);
    }

    public float getAnimationProgressSinPowerOf(int i)
    {
        return (float) Math.pow((double) MathHelper.sin(1.57079632679f * (float) (timer / duration)), i);
    }

    public float getAnimationProgressPoly2()
    {
        float x = (float) (timer / duration);
        float x2 = x * x;
        return x2 / (x2 + (1 - x) * (1 - x));
    }

    public float getAnimationProgressPoly3()
    {
        float x = (float) (timer / duration);
        float x3 = x * x * x;
        return x3 / (x3 + (1 - x) * (1 - x) * (1 - x));
    }

    public float getAnimationProgressPolyN(int n)
    {
        double x = timer / duration;
        double xi = Math.pow(x, (double) n);
        return (float) (xi / (xi + Math.pow((1d - x), (double) n)));
    }

    public float getAnimationProgressArcTan()
    {
        return (float) (0.5f + 0.49806510671f * Math.atan(3.14159265359d * (timer / duration - 0.5d)));
    }

    public float getAnimationProgressTemporary()
    {
        float x = 6.28318530718f * (float) (timer / duration);
        return 0.5f - 0.5f * MathHelper.cos(x + MathHelper.sin(x));
    }

    public float getAnimationProgressTemporaryFS()
    {
        float x = 3.14159265359f * (float) (timer / duration);
        return MathHelper.sin(x + MathHelper.sin(x));
    }

    public float getAnimationProgressTemporaryInvesed()
    {
        float x = 6.28318530718f * (float) (timer / duration);
        return 0.5f + 0.5f * MathHelper.cos(x + MathHelper.sin(x));
    }
}
