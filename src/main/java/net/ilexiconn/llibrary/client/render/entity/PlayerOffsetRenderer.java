package net.ilexiconn.llibrary.client.render.entity;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class PlayerOffsetRenderer extends EntityRenderer
{
    private static Map<EntityPlayer, Float> offsetY = Maps.newHashMap();
    public Minecraft mc;

    public PlayerOffsetRenderer(Minecraft minecraft)
    {
        super(minecraft, minecraft.getResourceManager());
        mc = minecraft;
    }

    public static void setOffsetY(EntityPlayer player, float f)
    {
        offsetY.put(player, f);
    }

    public static float getOffsetY(EntityPlayer entityPlayer)
    {
        return offsetY.get(entityPlayer);
    }

    public void updateCameraAndRender(float partialTick)
    {
        if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping())
        {
            super.updateCameraAndRender(partialTick);
            return;
        }

        Float offsetForPlayer = offsetY.get(mc.thePlayer);

        if (offsetForPlayer == null)
        {
            offsetForPlayer = 1.62f;
            offsetY.put(mc.thePlayer, 1.62f);
        }

        mc.thePlayer.yOffset -= offsetForPlayer;
        super.updateCameraAndRender(partialTick);
        mc.thePlayer.yOffset = 1.62f;
    }

    public void getMouseOver(float partialTick)
    {
        if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping())
        {
            super.getMouseOver(partialTick);
            return;
        }

        Float offsetForPlayer = offsetY.get(mc.thePlayer);

        if (offsetForPlayer == null)
        {
            offsetForPlayer = 1.62f;
            offsetY.put(mc.thePlayer, 1.62f);
        }

        mc.thePlayer.posY += offsetForPlayer;
        mc.thePlayer.prevPosY += offsetForPlayer;
        mc.thePlayer.lastTickPosY += offsetForPlayer;
        super.getMouseOver(partialTick);
        mc.thePlayer.posY -= offsetForPlayer;
        mc.thePlayer.prevPosY -= offsetForPlayer;
        mc.thePlayer.lastTickPosY -= offsetForPlayer;
    }
}
