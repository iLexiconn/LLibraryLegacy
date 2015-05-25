package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.client.gui.GuiSurvivalTab;
import net.ilexiconn.llibrary.common.block.IHighlightedBlock;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.ilexiconn.llibrary.common.survivaltab.TabHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public void blockHighlight(DrawBlockHighlightEvent event)
    {
        if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            BlockPos blockPos = event.target.func_178782_a();
            Block block = event.player.worldObj.getBlockState(blockPos).getBlock();

            if (block instanceof IHighlightedBlock)
            {
                List<AxisAlignedBB> bounds = ((IHighlightedBlock) block).getHighlightedBoxes(event.player.worldObj, blockPos, event.player);

                BlockPos pos = event.player.getPosition();

                GL11.glEnable(GL11.GL_BLEND);

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(0f, 0f, 0f, 0.4f);
                GL11.glLineWidth(2f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                for (AxisAlignedBB box : bounds)
                {
                    RenderGlobal.drawOutlinedBoundingBox(box.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()).offset(-pos.getX(), -pos.getY(), -pos.getZ()), -1);
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
    public void initGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        for (SurvivalTab survivalTab : TabHelper.getSurvivalTabs())
        {
            if (survivalTab.getSurvivalTab().getContainerGuiClass() != null && survivalTab.getSurvivalTab().getContainerGuiClass().isInstance(event.gui))
            {
                int count = 2;

                for (SurvivalTab tab : TabHelper.getSurvivalTabs())
                {
                    event.buttonList.add(new GuiSurvivalTab(count, tab));
                    count++;
                }
            }
        }
    }
}