package net.ilexiconn.llibrary.common.animation;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class Animator
{
    private int tempTick, prevTempTick;
    private boolean correctAnimation;
    private ModelBase mainModel;
    private IAnimated animatedEntity;
    private HashMap<ModelRenderer, Transform> transformMap, prevTransformMap;

    public Animator(ModelBase model)
    {
        tempTick = 0;
        correctAnimation = false;
        mainModel = model;
        transformMap = new HashMap<ModelRenderer, Transform>();
        prevTransformMap = new HashMap<ModelRenderer, Transform>();
    }

    public IAnimated getEntity()
    {
        return animatedEntity;
    }

    public void update(IAnimated entity)
    {
        tempTick = prevTempTick = 0;
        correctAnimation = false;
        this.animatedEntity = entity;
        transformMap.clear();
        prevTransformMap.clear();
        for (int i = 0; i < mainModel.boxList.size(); i++)
        {
            ModelRenderer box = (ModelRenderer) mainModel.boxList.get(i);
            box.rotateAngleX = 0F;
            box.rotateAngleY = 0F;
            box.rotateAngleZ = 0F;
        }
    }

    public boolean setAnimationId(int id)
    {
        tempTick = prevTempTick = 0;
        correctAnimation = animatedEntity.getAnimation().animationId == id;
        return correctAnimation;
    }

    public void startPhase(int duration)
    {
        if (!correctAnimation)
        {
            return;
        }
        prevTempTick = tempTick;
        tempTick += duration;
    }

    public void setStationaryPhase(int duration)
    {
        startPhase(duration);
        endPhase(true);
    }

    public void resetPhase(int duration)
    {
        startPhase(duration);
        endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z)
    {
        if (!correctAnimation)
        {
            return;
        }
        if (!transformMap.containsKey(box))
        {
            transformMap.put(box, new Transform(x, y, z));
        }
        else
        {
            transformMap.get(box).addRotation(x, y, z);
        }
    }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        if (!correctAnimation)
        {
            return;
        }
        if (!transformMap.containsKey(box))
        {
            transformMap.put(box, new Transform(x, y, z, 0F, 0F, 0F));
        }
        else
        {
            transformMap.get(box).addOffset(x, y, z);
        }
    }

    public void endPhase()
    {
        endPhase(false);
    }

    private void endPhase(boolean stationary)
    {
        if (!correctAnimation)
        {
            return;
        }
        int animationTick = animatedEntity.getAnimationTick();

        if (animationTick >= prevTempTick && animationTick < tempTick)
        {
            if (stationary)
            {
                for (ModelRenderer box : prevTransformMap.keySet())
                {
                    Transform transform = prevTransformMap.get(box);
                    box.rotateAngleX += transform.rotX;
                    box.rotateAngleY += transform.rotY;
                    box.rotateAngleZ += transform.rotZ;
                    box.rotationPointX += transform.offsetX;
                    box.rotationPointY += transform.offsetY;
                    box.rotationPointZ += transform.offsetZ;
                }
            }
            else
            {
                float tick = (animationTick - prevTempTick + LLibrary.proxy.getPartialTicks()) / (tempTick - prevTempTick);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2F)), dec = 1F - inc;
                for (ModelRenderer box : prevTransformMap.keySet())
                {
                    Transform transform = prevTransformMap.get(box);
                    box.rotateAngleX += dec * transform.rotX;
                    box.rotateAngleY += dec * transform.rotY;
                    box.rotateAngleZ += dec * transform.rotZ;
                    box.rotationPointX += dec * transform.offsetX;
                    box.rotationPointY += dec * transform.offsetY;
                    box.rotationPointZ += dec * transform.offsetZ;
                }
                for (ModelRenderer box : transformMap.keySet())
                {
                    Transform transform = transformMap.get(box);
                    box.rotateAngleX += inc * transform.rotX;
                    box.rotateAngleY += inc * transform.rotY;
                    box.rotateAngleZ += inc * transform.rotZ;
                    box.rotationPointX += inc * transform.offsetX;
                    box.rotationPointY += inc * transform.offsetY;
                    box.rotationPointZ += inc * transform.offsetZ;
                }
            }
        }

        if (!stationary)
        {
            prevTransformMap.clear();
            prevTransformMap.putAll(transformMap);
            transformMap.clear();
        }
    }
}
