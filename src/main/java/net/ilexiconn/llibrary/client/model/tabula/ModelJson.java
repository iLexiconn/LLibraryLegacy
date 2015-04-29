package net.ilexiconn.llibrary.client.model.tabula;

import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.json.container.JsonTabulaModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author gegy1000
 */
public class ModelJson extends ModelBase
{
    private JsonTabulaModel tabulaModel;
    private Map<ModelRenderer, ModelRenderer> cubes = Maps.newHashMap();

    public ModelJson(JsonTabulaModel model)
    {
        tabulaModel = model;

        textureWidth = model.getTextureWidth();
        textureHeight = model.getTextureHeight();

        for (CubeInfo c : model.getCubes())
        {
            cube(c, null);
        }

        for (CubeGroup g : model.getCubeGroups())
        {
            cubeGroup(g);
        }
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);

        double[] scale = tabulaModel.getScale();
        GL11.glScaled(scale[0], scale[1], scale[2]);

        for (Entry<ModelRenderer, ModelRenderer> cube : cubes.entrySet())
        {
            if (cube.getValue() == null)
            {
                cube.getKey().render(partialTicks);
            }
        }
    }

    public void cubeGroup(CubeGroup group)
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

    public void cube(CubeInfo cube, ModelRenderer parent)
    {
        ModelRenderer modelRenderer = createModelRenderer(cube);

        cubes.put(modelRenderer, parent);

        if (parent != null)
        {
            parent.addChild(modelRenderer);
        }

        for (CubeInfo c : cube.children)
        {
            cube(c, modelRenderer);
        }
    }

    public ModelRenderer createModelRenderer(CubeInfo cubeInfo)
    {
        ModelRenderer cube = new ModelRenderer(this, cubeInfo.txOffset[0], cubeInfo.txOffset[1]);
        cube.setRotationPoint((float) cubeInfo.position[0], (float) cubeInfo.position[1], (float) cubeInfo.position[2]);
        cube.addBox((float) cubeInfo.offset[0], (float) cubeInfo.offset[1], (float) cubeInfo.offset[2], cubeInfo.dimensions[0], cubeInfo.dimensions[1], cubeInfo.dimensions[2], 0.0F);
        cube.rotateAngleX = (float) Math.toRadians((float) cubeInfo.rotation[0]); //TODO Conversion?
        cube.rotateAngleY = (float) Math.toRadians((float) cubeInfo.rotation[1]);
        cube.rotateAngleZ = (float) Math.toRadians((float) cubeInfo.rotation[2]);

        return cube;
    }
}
