package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class MowzieModelRenderer extends ModelRenderer
{
    public float initRotateAngleX;
    public float initRotateAngleY;
    public float initRotateAngleZ;

    public float initRotationPointX;
    public float initRotationPointY;
    public float initRotationPointZ;

    public MowzieModelRenderer(ModelBase modelBase, String name)
    {
        super(modelBase, name);
    }

    public MowzieModelRenderer(ModelBase modelBase, int x, int y)
    {
        super(modelBase, x, y);

        if (modelBase instanceof MowzieModelBase)
        {
            MowzieModelBase mowzieModelBase = (MowzieModelBase) modelBase;
            mowzieModelBase.parts.add(this);
        }
    }

    public MowzieModelRenderer(ModelBase modelBase)
    {
        super(modelBase);
    }

    public void setInitValuesToCurrentPose()
    {
        initRotateAngleX = rotateAngleX;
        initRotateAngleY = rotateAngleY;
        initRotateAngleZ = rotateAngleZ;

        initRotationPointX = rotationPointX;
        initRotationPointY = rotationPointY;
        initRotationPointZ = rotationPointZ;
    }

    public void setCurrentPoseToInitValues()
    {
        rotateAngleX = initRotateAngleX;
        rotateAngleY = initRotateAngleY;
        rotateAngleZ = initRotateAngleZ;

        rotationPointX = initRotationPointX;
        rotationPointY = initRotationPointY;
        rotationPointZ = initRotationPointZ;
    }

    public void setRotationAngles(float x, float y, float z)
    {
        rotateAngleX = x;
        rotateAngleY = y;
        rotateAngleZ = z;
    }

    public void resetAllRotationPoints()
    {
        rotationPointX = initRotationPointX;
        rotationPointY = initRotationPointY;
        rotationPointZ = initRotationPointZ;
    }

    public void resetXRotationPoints()
    {
        rotationPointX = initRotationPointX;
    }

    public void resetYRotationPoints()
    {
        rotationPointY = initRotationPointY;
    }

    public void resetZRotationPoints()
    {
        rotationPointZ = initRotationPointZ;
    }

    public void resetAllRotations()
    {
        rotateAngleX = initRotateAngleX;
        rotateAngleY = initRotateAngleY;
        rotateAngleZ = initRotateAngleZ;
    }

    public void resetXRotations()
    {
        rotateAngleX = initRotateAngleX;
    }

    public void resetYRotations()
    {
        rotateAngleY = initRotateAngleY;
    }

    public void resetZRotations()
    {
        rotateAngleZ = initRotateAngleZ;
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
}