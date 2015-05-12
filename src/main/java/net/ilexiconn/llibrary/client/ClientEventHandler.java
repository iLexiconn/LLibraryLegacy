package net.ilexiconn.llibrary.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.block.IHighlightedBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
    private EntityRenderer renderer, prevRenderer;

    @SubscribeEvent
    public void blockHighlight(DrawBlockHighlightEvent event)
    {
        if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            int x = event.target.blockX;
            int y = event.target.blockY;
            int z = event.target.blockZ;

            Block block = event.player.worldObj.getBlock(x, y, z);

            if (block instanceof IHighlightedBlock)
            {
                List<AxisAlignedBB> bounds = ((IHighlightedBlock) block).getHighlightedBoxes(event.player.worldObj, x, y, z, event.player);

                Vec3 pos = event.player.getPosition(event.partialTicks);

                GL11.glEnable(GL11.GL_BLEND);

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(0f, 0f, 0f, 0.4f);
                GL11.glLineWidth(2f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                for (AxisAlignedBB box : bounds)
                {
                    RenderGlobal.drawOutlinedBoundingBox(box.copy().offset(x, y, z).offset(-pos.xCoord, -pos.yCoord, -pos.zCoord), -1);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void itemTooltip(ItemTooltipEvent event)
    {
        if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
        {
            event.toolTip.add(EnumChatFormatting.DARK_GRAY + "" + Item.itemRegistry.getNameForObject(event.itemStack.getItem()));
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        /*Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                if (renderer == null) renderer = new PlayerOffsetRenderer(mc);
                if (mc.entityRenderer != renderer)
                {
                    prevRenderer = mc.entityRenderer;
                    mc.entityRenderer = renderer;
                }
            }
            else if (prevRenderer != null && mc.entityRenderer != prevRenderer) mc.entityRenderer = prevRenderer;
        }
        else if (prevRenderer != null && mc.entityRenderer != prevRenderer) mc.entityRenderer = prevRenderer;*/
    }

    @SubscribeEvent
    public void renderPlayerPre(RenderPlayerEvent.Pre event)
    {
        /*if (PlayerOffsetRenderer.getOffsetY(Minecraft.getMinecraft().thePlayer) != 1.62f)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0f, -6.325f, 0f);
        }*/
    }

    @SubscribeEvent
    public void renderPlayerPost(RenderPlayerEvent.Post event)
    {
        /*if (PlayerOffsetRenderer.getOffsetY(Minecraft.getMinecraft().thePlayer) != 1.62f) GL11.glPopMatrix();*/
    }
}
