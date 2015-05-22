package net.ilexiconn.llibrary.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

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
        modelBipedMain.bipedRightArm.showModel = true;
        super.renderFirstPersonArm(player);
    }
}
