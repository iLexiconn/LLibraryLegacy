package net.ilexiconn.llibrary.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.block.IHighlightedBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public void blockHighlight(DrawBlockHighlightEvent e)
    {
        if (e.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            int x = e.target.blockX;
            int y = e.target.blockY;
            int z = e.target.blockZ;
            Block block = e.player.worldObj.getBlock(x, y, z);
            if (block instanceof IHighlightedBlock)
            {
                List<AxisAlignedBB> bounds = ((IHighlightedBlock) block).getHighlightedBoxes(e.player.worldObj, x, y, z, e.player);
                Vec3 pos = e.player.getPosition(e.partialTicks);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(0f, 0f, 0f, 0.4f);
                GL11.glLineWidth(2f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);
                for (AxisAlignedBB box : bounds) RenderGlobal.drawOutlinedBoundingBox(box.copy().offset(x, y, z).offset(-pos.xCoord, -pos.yCoord, -pos.zCoord), -1);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                e.setCanceled(true);
            }
        }
    }
}
