package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ChainBuffer
{
    private int yawTimer;
    private float yawVariation;
    private int pitchTimer;
    private float pitchVariation;
    private float[] yawArray;
    private float[] pitchArray;

    public ChainBuffer(int numberOfParentedBoxes)
    {
        yawTimer = 0;
        pitchTimer = 0;
        yawVariation = 0f;
        pitchVariation = 0f;
        yawArray = new float[numberOfParentedBoxes];
        pitchArray = new float[numberOfParentedBoxes];
    }

    public void resetRotations()
    {
        yawVariation = 0f;
        pitchVariation = 0f;
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, EntityLivingBase entity)
    {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(yawVariation) < maxAngle)
            yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset) / divider;

        if (yawVariation > 0.7f * angleDecrement)
        {
            if (yawTimer > bufferTime)
            {
                yawVariation -= angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement)
                {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            }
            else yawTimer++;
        }
        else if (yawVariation < -0.7f * angleDecrement)
        {
            if (yawTimer > bufferTime)
            {
                yawVariation += angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement)
                {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            }
            else yawTimer++;
        }

        for (int i = 0; i < yawArray.length; i++)
            yawArray[i] = 0.01745329251f * yawVariation / pitchArray.length;
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divider, EntityLivingBase entity)
    {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(pitchVariation) < maxAngle)
            pitchVariation += (entity.prevRotationPitch - entity.rotationPitch) / divider;

        if (pitchVariation > 0.7f * angleDecrement)
        {
            if (pitchTimer > bufferTime)
            {
                pitchVariation -= angleDecrement;
                if (MathHelper.abs(pitchVariation) < angleDecrement)
                {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            }
            else pitchTimer++;
        }
        else if (pitchVariation < -0.7f * angleDecrement)
        {
            if (pitchTimer > bufferTime)
            {
                pitchVariation += angleDecrement;
                if (MathHelper.abs(pitchVariation) < angleDecrement)
                {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            }
            else pitchTimer++;
        }

        for (int i = 0; i < pitchArray.length; i++)
            pitchArray[i] = 0.01745329251f * pitchVariation / pitchArray.length;
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity)
    {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(yawVariation) < maxAngle)
            yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset);

        if (yawVariation > 0.7f * angleDecrement)
        {
            if (yawTimer > bufferTime)
            {
                yawVariation -= angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement)
                {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            }
            else yawTimer++;
        }
        else if (yawVariation < -0.7f * angleDecrement)
        {
            if (yawTimer > bufferTime)
            {
                yawVariation += angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement)
                {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            }
            else yawTimer++;
        }

        for (int i = 0; i < yawArray.length; i++)
            yawArray[i] = 0.01745329251f * yawVariation / pitchArray.length;
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity)
    {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(pitchVariation) < maxAngle)
            pitchVariation += (entity.prevRotationPitch - entity.rotationPitch);

        if (pitchVariation > 0.7f * angleDecrement)
        {
            if (pitchTimer > bufferTime)
            {
                pitchVariation -= angleDecrement;
                if (MathHelper.abs(pitchVariation) < angleDecrement)
                {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            }
            else pitchTimer++;
        }
        else if (pitchVariation < -0.7f * angleDecrement)
        {
            if (pitchTimer > bufferTime)
            {
                pitchVariation += angleDecrement;
                if (MathHelper.abs(pitchVariation) < angleDecrement)
                {
                    pitchVariation = 0f;
                    pitchTimer = 0;
                }
            }
            else pitchTimer++;
        }

        for (int i = 0; i < pitchArray.length; i++)
            pitchArray[i] = 0.01745329251f * pitchVariation / pitchArray.length;
    }

    public void applyChainSwingBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == yawArray.length)
            for (int i = 0; i < boxes.length; i++) boxes[i].rotateAngleY += yawArray[i];
        else System.err.println("[LLibrary] Wrong array length being used in the buffer! (Y axis)");
    }

    public void applyChainWaveBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == pitchArray.length)
            for (int i = 0; i < boxes.length; i++) boxes[i].rotateAngleX += pitchArray[i];
        else System.out.println("[LLibrary] Wrong array length being used in the buffer! (X axis)");
    }
}
