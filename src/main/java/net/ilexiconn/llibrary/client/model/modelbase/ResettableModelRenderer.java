package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ResettableModelRenderer extends ModelRenderer
{
    public float firstRotateAngleX;
    public float firstRotateAngleY;
    public float firstRotateAngleZ;

    public float firstRotationPointX;
    public float firstRotationPointY;
    public float firstRotationPointZ;

    public float firstOffsetX;
    public float firstOffsetY;
    public float firstOffsetZ;

    public ResettableModelRenderer(ModelBase modelBase, int textureOffsetX, int textureOffsetY)
    {
        super(modelBase, textureOffsetX, textureOffsetY);
    }

    public void savefirstParameters()
    {
        firstRotationPointX = rotationPointX;
        firstRotationPointY = rotationPointY;
        firstRotationPointZ = rotationPointZ;
        firstRotateAngleX = rotateAngleX;
        firstRotateAngleY = rotateAngleY;
        firstRotateAngleZ = rotateAngleZ;
        firstOffsetX = offsetX;
        firstOffsetY = offsetY;
        firstOffsetZ = offsetZ;
    }

    public void resetAllParameters()
    {
        rotationPointX = firstRotationPointX;
        rotationPointY = firstRotationPointY;
        rotationPointZ = firstRotationPointZ;
        rotateAngleX = firstRotateAngleX;
        rotateAngleY = firstRotateAngleY;
        rotateAngleZ = firstRotateAngleZ;
        offsetX = firstOffsetX;
        offsetY = firstOffsetY;
        offsetZ = firstOffsetZ;
    }

    public void resetAllRotationPoints()
    {
        rotationPointX = firstRotationPointX;
        rotationPointY = firstRotationPointY;
        rotationPointZ = firstRotationPointZ;
    }

    public void resetXRotationPoints()
    {
        rotationPointX = firstRotationPointX;
    }

    public void resetYRotationPoints()
    {
        rotationPointY = firstRotationPointY;
    }

    public void resetZRotationPoints()
    {
        rotationPointZ = firstRotationPointZ;
    }

    public void resetAllRotations()
    {
        rotateAngleX = firstRotateAngleX;
        rotateAngleY = firstRotateAngleY;
        rotateAngleZ = firstRotateAngleZ;
    }

    public void resetXRotations()
    {
        rotateAngleX = firstRotateAngleX;
    }

    public void resetYRotations()
    {
        rotateAngleY = firstRotateAngleY;
    }

    public void resetZRotations()
    {
        rotateAngleZ = firstRotateAngleZ;
    }

    public void resetAllOffsets()
    {
        offsetX = firstOffsetX;
        offsetY = firstOffsetY;
        offsetZ = firstOffsetZ;
    }

    public void resetXOffsets()
    {
        offsetX = firstOffsetX;
    }

    public void resetYOffsets()
    {
        offsetY = firstOffsetY;
    }

    public void resetZOffsets()
    {
        offsetZ = firstOffsetZ;
    }

    public void copyAllRotationPoints(ResettableModelRenderer target)
    {
        rotationPointX = target.rotationPointX;
        rotationPointY = target.rotationPointY;
        rotationPointZ = target.rotationPointZ;
    }

    public void copyXRotationPoint(ResettableModelRenderer target)
    {
        rotationPointX = target.rotationPointX;
    }

    public void copyYRotationPoint(ResettableModelRenderer target)
    {
        rotationPointY = target.rotationPointY;
    }

    public void copyZRotationPoint(ResettableModelRenderer target)
    {
        rotationPointZ = target.rotationPointZ;
    }

    public void pinLegPlaneYZ(ResettableModelRenderer parentBox, float radius, float rotationX)
    {
        rotationPointY = parentBox.rotationPointY + radius * (float) Math.cos(rotationX);
        rotationPointZ = parentBox.rotationPointZ + radius * (float) Math.sin(rotationX);
    }

    public void pinTailPlaneYZ(ResettableModelRenderer parentBox, float radius, float rotationX)
    {
        rotationPointY = parentBox.rotationPointY + radius * (float) Math.cos(rotationX + Math.PI / 2);
        rotationPointZ = parentBox.rotationPointZ + radius * (float) Math.sin(rotationX + Math.PI / 2);
    }

    public float calculateDistanceYZ(ResettableModelRenderer target)
    {
        return (float) Math.sqrt(Math.pow((target.firstRotationPointY - firstRotationPointY), 2) + Math.pow((target.firstRotationPointZ - firstRotationPointZ), 2));
    }

    public float calculateRadius(ResettableModelRenderer target)
    {
        return (float) Math.sqrt(Math.pow((target.firstRotationPointX - firstRotationPointX), 2) + Math.pow((target.firstRotationPointY - firstRotationPointY), 2) + Math.pow((target.firstRotationPointZ - firstRotationPointZ), 2));
    }
}