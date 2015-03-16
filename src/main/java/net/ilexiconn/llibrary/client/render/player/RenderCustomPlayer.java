package net.ilexiconn.llibrary.client.render.player;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import net.ilexiconn.llibrary.client.model.ModelBipedCustom;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

public class RenderCustomPlayer extends RenderPlayer
{
	public RenderCustomPlayer()
	{
		super();
		this.setRenderManager(RenderManager.instance);
		this.mainModel = new ModelBipedCustom();
		this.modelBipedMain = (ModelBiped) mainModel;
	}
	
    protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks)
    {
        net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre event = new net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre(player, this, partialTicks);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        
        super.renderArrowsStuckInEntity(player, partialTicks);
        
        ItemStack helmetStack = player.inventory.armorItemInSlot(3);

        ItemRenderer itemRenderer = this.renderManager.itemRenderer;
        
		if (helmetStack != null && event.renderHelmet)
        {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            float f1;

            Item helmet = helmetStack.getItem();
            
			if (helmet instanceof ItemBlock)
            {
                net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(helmetStack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
                boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, helmetStack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(helmet).getRenderType()))
                {
                    f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                itemRenderer.renderItem(player, helmetStack, 0);
            }
            else if (helmet == Items.skull)
            {
                f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;

                if (helmetStack.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = helmetStack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10))
                    {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
                    {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, helmetStack.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }

        float scale;

        if (player.getCommandSenderName().equals("deadmau5") && player.func_152123_o())
        {
            this.bindTexture(player.getLocationSkin());

            for (int earIndex = 0; earIndex < 2; ++earIndex)
            {
                float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
                float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
                GL11.glPushMatrix();
                GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float)(earIndex * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
                scale = 1.3333334F;
                GL11.glScalef(scale, scale, scale);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean renderCape = player.func_152122_n();
        renderCape = event.renderCape && renderCape;
        float renderYawOffset;

        if (renderCape && !player.isInvisible() && !player.getHideCape())
        {
            this.bindTexture(player.getLocationCape());
            
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            
            double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * (double)partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks);
            double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * (double)partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks);
            double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * (double)partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks);
     
            renderYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
         
            double d1 = (double)MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
            double d2 = (double)(-MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F));
            float distanceWalkModified = (float)d4 * 10.0F;

            if (distanceWalkModified < -6.0F)
            {
                distanceWalkModified = -6.0F;
            }

            if (distanceWalkModified > 32.0F)
            {
                distanceWalkModified = 32.0F;
            }

            float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F)
            {
                f6 = 0.0F;
            }

            float cameraYaw = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
            distanceWalkModified += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * cameraYaw;

            if (player.isSneaking())
            {
                distanceWalkModified += 25.0F;
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + distanceWalkModified, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            
            this.modelBipedMain.renderCloak(0.0625F);
            
            GL11.glPopMatrix();
        }

        ItemStack heldItemStack = player.inventory.getCurrentItem();

        if (heldItemStack != null && event.renderItem)
        {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (player.fishEntity != null)
            {
                heldItemStack = new ItemStack(Items.stick);
            }

            EnumAction enumaction = null;

            if (player.getItemInUseCount() > 0)
            {
                enumaction = heldItemStack.getItemUseAction();
            }

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(heldItemStack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, heldItemStack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            Item heldItem = heldItemStack.getItem();
		
            if (is3D || heldItem instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(heldItem).getRenderType()))
            {
                scale = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                scale *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-scale, -scale, scale);
            }
            else if (heldItem == Items.bow)
            {
                scale = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (heldItem.isFull3D())
            {
                scale = 0.625F;

                if (heldItem.shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
                {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                scale = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(scale, scale, scale);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f3;
            int k;
            float f12;

            if (heldItem.requiresMultipleRenderPasses())
            {
                for (k = 0; k < heldItem.getRenderPasses(heldItemStack.getItemDamage()); ++k)
                {
                    int i = heldItem.getColorFromItemStack(heldItemStack, k);
                    f12 = (float)(i >> 16 & 255) / 255.0F;
                    f3 = (float)(i >> 8 & 255) / 255.0F;
                    renderYawOffset = (float)(i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, renderYawOffset, 1.0F);
                    itemRenderer.renderItem(player, heldItemStack, k);
                }
            }
            else
            {
                k = heldItem.getColorFromItemStack(heldItemStack, 0);
                float f11 = (float)(k >> 16 & 255) / 255.0F;
                f12 = (float)(k >> 8 & 255) / 255.0F;
                f3 = (float)(k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                itemRenderer.renderItem(player, heldItemStack, 0);
            }

            GL11.glPopMatrix();
        }
        
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Specials.Post(player, this, partialTicks));
    }
}
