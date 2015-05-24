package net.ilexiconn.llibrary.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.ilexiconn.llibrary.client.render.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

@SideOnly(Side.CLIENT)
public final class RenderLLibraryPlayer extends RenderPlayer
{
    public RenderLLibraryPlayer()
    {
        setRenderManager(RenderManager.instance);
        mainModel = new ModelLLibraryBiped();
        modelBipedMain = (ModelBiped) mainModel;
    }

    public void renderFirstPersonArm(EntityPlayer player)
    {
        List<IExtension> modelExtensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);

        if (modelExtensions != null)
        {
            boolean flag = true;
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IFirstPersonExtension)
                    if (!((IFirstPersonExtension) modelExtension).preRenderFirstPerson(player, this, modelBipedMain))
                        flag = false;
            if (flag) super.renderFirstPersonArm(player);
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IFirstPersonExtension)
                    ((IFirstPersonExtension) modelExtension).postRenderFirstPerson(player, this, modelBipedMain);
        }
        else super.renderFirstPersonArm(player);
    }

    protected void renderArrowsStuckInEntity(EntityLivingBase entity, float partialTicks)
    {
        List<IExtension> modelExtensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);

        if (modelExtensions != null)
        {
            boolean flag = true;
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IArrowStuckExtension)
                    if (!((IArrowStuckExtension) modelExtension).preRenderArrowsStuckInEntity(entity, this, partialTicks))
                        flag = false;
            if (flag) super.renderArrowsStuckInEntity(entity, partialTicks);
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IArrowStuckExtension)
                    ((IArrowStuckExtension) modelExtension).postRenderArrowsStuckInEntity(entity, this, partialTicks);
        }
        else super.renderArrowsStuckInEntity(entity, partialTicks);
    }

    protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks)
    {
        List<IExtension> modelExtensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);

        if (modelExtensions != null)
        {
            boolean flag = true;
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IEquippedItemsExtension)
                    if (!((IEquippedItemsExtension) modelExtension).preRenderEquippedItems(player, this, partialTicks))
                        flag = false;
            if (flag) super.renderEquippedItems(player, partialTicks);
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IEquippedItemsExtension)
                    ((IEquippedItemsExtension) modelExtension).postRenderEquippedItems(player, this, partialTicks);
        }
        else super.renderEquippedItems(player, partialTicks);
    }
}
