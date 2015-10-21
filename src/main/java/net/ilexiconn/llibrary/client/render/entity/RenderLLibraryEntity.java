package net.ilexiconn.llibrary.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class RenderLLibraryEntity extends EntityRenderer
{
    public Minecraft mc;

    public RenderLLibraryEntity(Minecraft minecraft)
    {
        super(minecraft, minecraft.getResourceManager());
        mc = minecraft;
    }

    public void updateCameraAndRender(float partialTick)
    {
        EntityPlayer player = mc.thePlayer;

        if (player == null || player.isPlayerSleeping())
        {
            super.updateCameraAndRender(partialTick);
            return;
        }

        player.yOffset = -(EntityHelper.getScale(player) - 1) * 1.62f + 1.62f;
        super.updateCameraAndRender(partialTick);
        player.yOffset = 1.62f;
    }
}
