package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.util.MathHelper;

import java.util.Random;

public class IntermittentAnimation
{
    private double timer;
    private double duration;
    private boolean runInterval;
    private int inverter;
    private double timerInterval;
    private double intervalDuration;
    private int goChance;
    private int returnChance;
    private Random random = new Random();

    public IntermittentAnimation(int d, int i, int c, int r)
    {
        timer = 0;
        duration = (double) d;
        intervalDuration = (double) i;
        runInterval = true;
        goChance = c;
        returnChance = r;
        inverter = -1;
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

    public void runAnimation()
    {
        if (!runInterval)
        {
            if (timer < duration && timer > 0d) timer += inverter;
            else
            {
                if (timer >= duration) timer = duration;
                else if (timer <= 0d) timer = 0d;
                timerInterval = 0d;
                runInterval = true;
            }
        }
        else
        {
            if (timerInterval < intervalDuration) timerInterval++;
            else
            {
                if (inverter > 0 && random.nextInt(returnChance) == 0) inverter = -1;
                if (inverter < 0 && random.nextInt(goChance) == 0) inverter = 1;
                timer += inverter;
                runInterval = false;
            }
        }
    }

    public void stopAnimation()
    {
        if (timer > 0d) timer--;
        else
        {
            timer = 0d;
            runInterval = true;
            timerInterval = 0d;
            inverter = 1;
        }
    }

    public void stopAnimation(int time)
    {
        if (timer - time > 0d) timer -= time;
        else
        {
            timer = 0d;
            runInterval = false;
            timerInterval = 0d;
            inverter = 1;
        }
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
        return 0.5F + 0.5F * MathHelper.cos(x + MathHelper.sin(x));
    }
}
