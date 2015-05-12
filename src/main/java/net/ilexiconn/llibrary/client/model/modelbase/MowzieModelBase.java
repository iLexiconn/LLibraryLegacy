package net.ilexiconn.llibrary.client.model.modelbase;

import com.google.common.collect.Lists;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;

import java.util.List;

public class MowzieModelBase extends ModelBase
{
    public List<MowzieModelRenderer> parts = Lists.newArrayList();

    protected void setInitPose()
    {
        for (MowzieModelRenderer part : parts) part.setInitValuesToCurrentPose();
    }

    protected void setToInitPose()
    {
        for (MowzieModelRenderer part : parts) part.setCurrentPoseToInitValues();
    }

    protected void addChildTo(ModelRenderer child, ModelRenderer parent)
    {
        float distance = (float) Math.sqrt(Math.pow((child.rotationPointZ - parent.rotationPointZ), 2) + Math.pow((child.rotationPointY - parent.rotationPointY), 2));
        float oldRotateAngleX = parent.rotateAngleX;
        float parentToChildAngle = (float) Math.atan((child.rotationPointZ - parent.rotationPointZ) / (child.rotationPointY - parent.rotationPointY));
        float childRelativeRotation = parentToChildAngle - parent.rotateAngleX;
        float newRotationPointY = (float) (distance * (Math.cos(childRelativeRotation)));
        float newRotationPointZ = (float) (distance * (Math.sin(childRelativeRotation)));
        parent.rotateAngleX = 0f;
        child.setRotationPoint(child.rotationPointX - parent.rotationPointX, newRotationPointY, newRotationPointZ);
        parent.addChild(child);
        parent.rotateAngleX = oldRotateAngleX;
        child.rotateAngleX -= parent.rotateAngleX;
        child.rotateAngleY -= parent.rotateAngleY;
        child.rotateAngleZ -= parent.rotateAngleZ;
    }

    protected void newAddChildTo(ModelRenderer child, ModelRenderer parent)
    {
        float distance = (float) Math.sqrt(Math.pow((child.rotationPointZ - parent.rotationPointZ), 2) + Math.pow((child.rotationPointY - parent.rotationPointY), 2));
        float angle = (float) Math.atan2(child.rotationPointY - parent.rotationPointY, child.rotationPointZ - parent.rotationPointZ);
        float newRotationPointZ = (float) (distance * (Math.cos(angle)));
        float newRotationPointY = (float) (distance * (Math.sin(angle)));
        parent.addChild(child);
        child.rotateAngleX -= parent.rotateAngleX;
        child.rotateAngleY -= parent.rotateAngleY;
        child.rotateAngleZ -= parent.rotateAngleZ;
    }

    public void faceTarget(MowzieModelRenderer box, float f, float f3, float f4)
    {
        box.rotateAngleY += (f3 / (180f / (float) Math.PI)) / f;
        box.rotateAngleX += (f4 / (180f / (float) Math.PI)) / f;
    }

    public float rotateBox(float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
    {
        if (invert) return -MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
        else return MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
    }

    public float moveBox(float speed, float degree, boolean bounce, float f, float f1)
    {
        if (bounce) return -MathHelper.abs((MathHelper.sin(f * speed) * f1 * degree));
        else return MathHelper.sin(f * speed) * f1 * degree - f1 * degree;
    }

    public void walk(MowzieModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
    {
        int inverted = 1;
        if (invert) inverted = -1;
        box.rotateAngleX += MathHelper.cos(f * speed + offset) * degree * inverted * f1 + weight * f1;
    }

    public void flap(MowzieModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
    {
        int inverted = 1;
        if (invert) inverted = -1;
        box.rotateAngleZ += MathHelper.cos(f * speed + offset) * degree * inverted * f1 + weight * f1;
    }

    public void bob(MowzieModelRenderer box, float speed, float degree, boolean bounce, float f, float f1)
    {
        float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
        if (bounce) bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
        box.rotationPointY += bob;
    }

    public void chainSwing(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float f, float f1)
    {
        int numberOfSegments = boxes.length;
        float offset = (float) ((rootOffset * Math.PI) / (2 * numberOfSegments));
        for (int i = 0; i < numberOfSegments; i++)
            boxes[i].rotateAngleY += MathHelper.cos(f * speed + offset * i) * f1 * degree;
    }

    public void chainWave(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float f, float f1)
    {
        int numberOfSegments = boxes.length;
        float offset = (float) ((rootOffset * Math.PI) / (2 * numberOfSegments));
        for (int i = 0; i < numberOfSegments; i++)
            boxes[i].rotateAngleX += MathHelper.cos(f * speed + offset * i) * f1 * degree;
    }
}
