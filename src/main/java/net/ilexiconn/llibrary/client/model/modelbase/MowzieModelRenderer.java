package net.ilexiconn.llibrary.client.model.modelbase;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author BobMowzie, gegy1000, FiskFille
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class MowzieModelRenderer extends ModelRenderer {
    public float initRotateAngleX;
    public float initRotateAngleY;
    public float initRotateAngleZ;

    public float initOffsetX;
    public float initOffsetY;
    public float initOffsetZ;

    public float initRotationPointX;
    public float initRotationPointY;
    public float initRotationPointZ;

    public float scaleX = 1f;
    public float scaleY = 1f;
    public float scaleZ = 1f;
    public ModelRenderer parent;
    public boolean hasInitPose;
    private boolean compiled;
    private int displayList;

    public MowzieModelRenderer(ModelBase modelBase, String name) {
        super(modelBase, name);
    }

    public MowzieModelRenderer(ModelBase modelBase, int x, int y) {
        super(modelBase, x, y);

        if (modelBase instanceof MowzieModelBase) {
            MowzieModelBase mowzieModelBase = (MowzieModelBase) modelBase;

            mowzieModelBase.addPart(this);
        }
    }

    public MowzieModelRenderer(ModelBase modelBase) {
        super(modelBase);
    }

    @Override
    public void addChild(ModelRenderer renderer) {
        super.addChild(renderer);

        if (renderer instanceof MowzieModelRenderer) {
            ((MowzieModelRenderer) renderer).setParent(this);
        }
    }

    public void postRenderParentChain(float par1) {
        if (parent instanceof MowzieModelRenderer) {
            ((MowzieModelRenderer) parent).postRenderParentChain(par1);
        } else if (parent != null) {
            parent.postRender(par1);
        }

        postRender(par1);
    }

    /**
     * Returns the parent of this ModelRenderer
     */
    public ModelRenderer getParent() {
        return parent;
    }

    /**
     * Sets the parent of this ModelRenderer
     */
    private void setParent(ModelRenderer modelRenderer) {
        parent = modelRenderer;
    }

    /**
     * Set the initialization pose to the current pose
     */
    public void setInitValuesToCurrentPose() {
        initRotateAngleX = rotateAngleX;
        initRotateAngleY = rotateAngleY;
        initRotateAngleZ = rotateAngleZ;

        initRotationPointX = rotationPointX;
        initRotationPointY = rotationPointY;
        initRotationPointZ = rotationPointZ;

        initOffsetX = offsetX;
        initOffsetY = offsetY;
        initOffsetZ = offsetZ;

        hasInitPose = true;
    }

    /**
     * Resets the pose to init pose
     */
    public void setCurrentPoseToInitValues() {
        if (hasInitPose) {
            rotateAngleX = initRotateAngleX;
            rotateAngleY = initRotateAngleY;
            rotateAngleZ = initRotateAngleZ;

            rotationPointX = initRotationPointX;
            rotationPointY = initRotationPointY;
            rotationPointZ = initRotationPointZ;

            offsetX = initOffsetX;
            offsetY = initOffsetY;
            offsetZ = initOffsetZ;
        }
    }

    public void setRotationAngles(float x, float y, float z) {
        rotateAngleX = x;
        rotateAngleY = y;
        rotateAngleZ = z;
    }

    /**
     * Resets all rotation points.
     */
    public void resetAllRotationPoints() {
        rotationPointX = initRotationPointX;
        rotationPointY = initRotationPointY;
        rotationPointZ = initRotationPointZ;
    }

    /**
     * Resets X rotation point.
     */
    public void resetXRotationPoints() {
        rotationPointX = initRotationPointX;
    }

    /**
     * Resets Y rotation point.
     */
    public void resetYRotationPoints() {
        rotationPointY = initRotationPointY;
    }

    /**
     * Resets Z rotation point.
     */
    public void resetZRotationPoints() {
        rotationPointZ = initRotationPointZ;
    }

    /**
     * Resets all rotations.
     */
    public void resetAllRotations() {
        rotateAngleX = initRotateAngleX;
        rotateAngleY = initRotateAngleY;
        rotateAngleZ = initRotateAngleZ;
    }

    /**
     * Resets X rotation.
     */
    public void resetXRotations() {
        rotateAngleX = initRotateAngleX;
    }

    /**
     * Resets Y rotation.
     */
    public void resetYRotations() {
        rotateAngleY = initRotateAngleY;
    }

    /**
     * Resets Z rotation.
     */
    public void resetZRotations() {
        rotateAngleZ = initRotateAngleZ;
    }

    /**
     * Copies the rotation point coordinates.
     */
    public void copyAllRotationPoints(MowzieModelRenderer target) {
        rotationPointX = target.rotationPointX;
        rotationPointY = target.rotationPointY;
        rotationPointZ = target.rotationPointZ;
    }

    /**
     * Copies X rotation point.
     */
    public void copyXRotationPoint(MowzieModelRenderer target) {
        rotationPointX = target.rotationPointX;
    }

    /**
     * Copies Y rotation point.
     */
    public void copyYRotationPoint(MowzieModelRenderer target) {
        rotationPointY = target.rotationPointY;
    }

    /**
     * Copies Z rotation point.
     */
    public void copyZRotationPoint(MowzieModelRenderer target) {
        rotationPointZ = target.rotationPointZ;
    }

    public void renderWithParents(float partialTicks) {
        if (parent instanceof MowzieModelRenderer) {
            ((MowzieModelRenderer) parent).renderWithParents(partialTicks);
        } else if (parent != null) {
            parent.render(partialTicks);
        }

        render(partialTicks);
    }

    public void setScale(float x, float y, float z) {
        scaleX = x;
        scaleY = y;
        scaleZ = z;
    }

    @Deprecated
    public void setOpacity(float o) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks) {
        GlStateManager.pushMatrix();

        if (!this.isHidden) {
            if (this.showModel) {
                if (!this.compiled) {
                    this.compileDisplayList(partialTicks);
                }

                float pixelSize = 0.0625F;
                GlStateManager.translate(this.rotationPointX * pixelSize, this.rotationPointY * pixelSize, this.rotationPointZ * pixelSize);
                GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                GlStateManager.scale(scaleX, scaleY, scaleZ);
                GlStateManager.translate(-this.rotationPointX * pixelSize, -this.rotationPointY * pixelSize, -this.rotationPointZ * pixelSize);
                int i;

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                        GlStateManager.callList(this.displayList);

                        if (this.childModels != null) {
                            for (i = 0; i < this.childModels.size(); ++i) {
                                this.childModels.get(i).render(partialTicks);
                            }
                        }
                    } else {
                        GlStateManager.translate(this.rotationPointX * partialTicks, this.rotationPointY * partialTicks, this.rotationPointZ * partialTicks);
                        GlStateManager.callList(this.displayList);

                        if (this.childModels != null) {
                            for (i = 0; i < this.childModels.size(); ++i) {
                                this.childModels.get(i).render(partialTicks);
                            }
                        }

                        GlStateManager.translate(-this.rotationPointX * partialTicks, -this.rotationPointY * partialTicks, -this.rotationPointZ * partialTicks);
                    }
                } else {
                    GlStateManager.translate(this.rotationPointX * partialTicks, this.rotationPointY * partialTicks, this.rotationPointZ * partialTicks);

                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                    }

                    GlStateManager.callList(this.displayList);

                    if (this.childModels != null) {
                        for (i = 0; i < this.childModels.size(); ++i) {
                            this.childModels.get(i).render(partialTicks);
                        }
                    }
                }
            }
        }

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    private void compileDisplayList(float partialTicks) {
        displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        for (ModelBox box : cubeList) {
            box.render(Tessellator.getInstance().getWorldRenderer(), partialTicks);
        }
        GL11.glEndList();
        compiled = true;
    }
}