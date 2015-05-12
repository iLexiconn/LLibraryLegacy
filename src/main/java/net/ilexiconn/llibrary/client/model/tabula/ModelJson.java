package net.ilexiconn.llibrary.client.model.tabula;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.entity.animation.IModelAnimator;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelBase;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author gegy1000
 */
public class ModelJson extends MowzieModelBase
{
    private JsonTabulaModel tabulaModel;
    private Map<MowzieModelRenderer, MowzieModelRenderer> childParentMap = Maps.newHashMap();
    private Map<String, MowzieModelRenderer> nameMap = Maps.newHashMap();
    private Map<String, MowzieModelRenderer> identifierMap = Maps.newHashMap();
    private IModelAnimator animator;

    private ArrayList<Animation> animations = Lists.newArrayList();

    private Animation playingAnimation;
    private int animationTimer;

    private int animationLength;

    public ModelJson(JsonTabulaModel model)
    {
        tabulaModel = model;

        textureWidth = model.getTextureWidth();
        textureHeight = model.getTextureHeight();

        animations = model.getAnimations();

        for (CubeInfo c : model.getCubes())
        {
            cube(c, null);
        }

        for (CubeGroup g : model.getCubeGroups())
        {
            cubeGroup(g);
        }

        setInitPose();
    }

    public ModelJson(JsonTabulaModel model, IModelAnimator animator)
    {
        this(model);
        this.animator = animator;
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);

        double[] scale = tabulaModel.getScale();
        GL11.glScaled(scale[0], scale[1], scale[2]);

        for (Entry<MowzieModelRenderer, MowzieModelRenderer> cube : childParentMap.entrySet())
        {
            if (cube.getValue() == null)
            {
                cube.getKey().render(partialTicks);
            }
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);

        if (!Minecraft.getMinecraft().isGamePaused())
        {
            this.setToInitPose();

            if (playingAnimation != null)
            {
                updateAnimation(entity);
            }

            if (animator != null)
            {
                animator.setRotationAngles(this, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
            }
        }
    }

    private void cubeGroup(CubeGroup group)
    {
        for (CubeInfo cube : group.cubes)
        {
            cube(cube, null);
        }

        for (CubeGroup c : group.cubeGroups)
        {
            cubeGroup(c);
        }
    }

    private void cube(CubeInfo cube, MowzieModelRenderer parent)
    {
        MowzieModelRenderer modelRenderer = createModelRenderer(cube);

        childParentMap.put(modelRenderer, parent);
        nameMap.put(cube.name, modelRenderer);
        identifierMap.put(cube.identifier, modelRenderer);

        if (parent != null)
        {
            parent.addChild(modelRenderer);
        }

        for (CubeInfo c : cube.children)
        {
            cube(c, modelRenderer);
        }
    }

    public void startAnimation(int id)
    {
        if (playingAnimation == null)
        {
            playingAnimation = animations.get(id);

            animationLength = 0;

            for (Entry<String, ArrayList<AnimationComponent>> entry : playingAnimation.sets.entrySet())
            {
                for (AnimationComponent component : entry.getValue())
                {
                    if (component.startKey + component.length > animationLength)
                    {
                        animationLength = component.startKey + component.length;
                    }
                }
            }

            animationTimer = 0;
        }
    }

    public void stopAnimation()
    {
        playingAnimation = null;
    }

    public void updateAnimation(Entity entity)
    {
        for (Entry<String, ArrayList<AnimationComponent>> entry : playingAnimation.sets.entrySet())
        {
            MowzieModelRenderer animating = identifierMap.get(entry.getKey());

            for (AnimationComponent component : entry.getValue())
            {
                if (animationTimer > component.startKey) //&& animationTimer < component.startKey + component.length)
                {
                    int componentTimer = animationTimer - component.startKey;

                    if (componentTimer > component.length)
                    {
                        componentTimer = component.length;
                    }

                    //TODO more?
                    animating.rotationPointX += component.posChange[0] / component.length * componentTimer;
                    animating.rotationPointY += component.posChange[1] / component.length * componentTimer;
                    animating.rotationPointZ += component.posChange[2] / component.length * componentTimer;

                    animating.rotateAngleX += Math.toRadians(component.rotChange[0] / component.length * componentTimer);
                    animating.rotateAngleY += Math.toRadians(component.rotChange[1] / component.length * componentTimer);
                    animating.rotateAngleZ += Math.toRadians(component.rotChange[2] / component.length * componentTimer);
                }
            }
        }

        animationTimer = entity.ticksExisted % animationLength;

        if (animationTimer > animationLength)
        {
            if (playingAnimation.loops)
            {
                animationTimer = 0;
            }
            else
            {
                stopAnimation();
            }
        }
    }

    private MowzieModelRenderer createModelRenderer(CubeInfo cubeInfo)
    {
        MowzieModelRenderer cube = new MowzieModelRenderer(this, cubeInfo.txOffset[0], cubeInfo.txOffset[1]);
        cube.setRotationPoint((float) cubeInfo.position[0], (float) cubeInfo.position[1], (float) cubeInfo.position[2]);
        cube.addBox((float) cubeInfo.offset[0], (float) cubeInfo.offset[1], (float) cubeInfo.offset[2], cubeInfo.dimensions[0], cubeInfo.dimensions[1], cubeInfo.dimensions[2], 0.0F);
        cube.rotateAngleX = (float) Math.toRadians((float) cubeInfo.rotation[0]); //TODO Conversion?
        cube.rotateAngleY = (float) Math.toRadians((float) cubeInfo.rotation[1]);
        cube.rotateAngleZ = (float) Math.toRadians((float) cubeInfo.rotation[2]);

        return cube;
    }

    public MowzieModelRenderer getCube(String name)
    {
        return nameMap.get(name);
    }

    public boolean isAnimationInProgress()
    {
        return playingAnimation != null;
    }
}
