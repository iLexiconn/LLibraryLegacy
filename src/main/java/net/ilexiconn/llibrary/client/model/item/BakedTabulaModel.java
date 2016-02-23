package net.ilexiconn.llibrary.client.model.item;

import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class BakedTabulaModel implements IFlexibleBakedModel, IPerspectiveAwareModel {
    private final ImmutableList<BakedQuad> quads;

    private final TextureAtlasSprite particle;

    private final VertexFormat format;

    private final ImmutableMap<TransformType, TRSRTransformation> transforms;

    public BakedTabulaModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format,
        ImmutableMap<TransformType, TRSRTransformation> transforms) {
        this.quads = quads;
        this.particle = particle;
        this.format = format;
        this.transforms = transforms;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return ImmutableList.<BakedQuad> of();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(TransformType type) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type);
    }
}
