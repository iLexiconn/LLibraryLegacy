package net.ilexiconn.llibrary.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * This is a buffer used to delay a chain of parented boxes by using the yaw and pitch
 * of the entity.
 *
 * @author RafaMv & iLexiconn
 */
@SideOnly(Side.CLIENT)
public class ChainBuffer
{
    /**
     * Used to delay the tail animation when the entity rotates.
     */
    private int yawTimer;

    /**
     * Rotation amount (rotateY) of the tail buffer. Added when the entity rotates.
     */
    private float yawVariation;

    /**
     * Used to delay the tail animation when the entity rotates.
     */
    private int pitchTimer;

    /**
     * Rotation amount (rotateY) of the tail buffer. Added when the entity rotates.
     */
    private float pitchVariation;

    /**
     * Array that contains the right rotations to be applied in the Y axis.
     */
    private float[] yawArray;

    /**
     * Array that contains the right rotations to be applied in the X axis.
     */
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

    /**
     * Sets both yaw and pitch variations to zero.
     */
    public void resetRotations()
    {
        yawVariation = 0f;
        pitchVariation = 0f;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleY).
     *
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40f to 90f degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3f degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5f.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
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
            yawArray[i] = 0.01745329251F * yawVariation / pitchArray.length;
    }

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleX).
     *
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40f to 90f degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3f degrees;
     * @param divider        reduces the amount of angle added to the buffer.
     *                       Try values about 5f.
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
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

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleY).
     *
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40f to 90f degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3f degrees;
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
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

    /**
     * Adds a specific rotation, depending on the entity rotation, to an array that can be later
     * used to animate a chain of parented boxes. (rotateAngleX).
     *
     * @param maxAngle       is the maximum angle that the tail can have.
     *                       Try values about 40f to 90f degrees;
     * @param bufferTime     is the number of ticks necessary to start reducing the tail angle.
     *                       Try values about 5 to 30 ticks;
     * @param angleDecrement is the amount of angle that will be reduced each tick.
     *                       Try values about 3f degrees;
     * @param entity         is the EntityLivingBase that will be used to animate the tail;
     */
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

    /**
     * Adds a rotations in the Y axis depending on the entity rotation using a previous set array. (rotateAngleY).
     *
     * @param boxes are the chain of parented boxes to be animated;
     */
    public void applyChainSwingBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == yawArray.length) for (int i = 0; i < boxes.length; i++) boxes[i].rotateAngleY += yawArray[i];
        else System.err.println("Wrong array length being used in the buffer! (Y axis)");
    }

    /**
     * Adds a rotations in the X axis depending on the entity rotation using a previous set array. (rotateAngleX).
     *
     * @param boxes are the chain of parented boxes to be animated;
     */
    public void applyChainWaveBuffer(MowzieModelRenderer[] boxes)
    {
        if (boxes.length == pitchArray.length) for (int i = 0; i < boxes.length; i++) boxes[i].rotateAngleX += pitchArray[i];
        else System.err.println("Wrong array length being used in the buffer! (X axis)");
    }
}