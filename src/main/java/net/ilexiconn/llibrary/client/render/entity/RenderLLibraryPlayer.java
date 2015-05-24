package net.ilexiconn.llibrary.client.render.entity;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.entity.ModelLLibraryBiped;
import net.ilexiconn.llibrary.client.render.*;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

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
        RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(player, this, partialTicks);
        if (MinecraftForge.EVENT_BUS.post(event)) return;

        List<IExtension> modelExtensions = RenderHelper.getModelExtensionsFor(ModelBiped.class);

        if (modelExtensions != null)
        {
            boolean flag = true;
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IEquippedItemsExtension)
                    if (!((IEquippedItemsExtension) modelExtension).preRenderEquippedItems(player, this, partialTicks))
                        flag = false;
            GL11.glColor3f(1f, 1f, 1f);
            renderArrowsStuckInEntity(player, partialTicks);
            if (flag) renderEquippedItems(player, partialTicks, event);
            for (IExtension modelExtension : modelExtensions)
                if (modelExtension instanceof IEquippedItemsExtension)
                    ((IEquippedItemsExtension) modelExtension).postRenderEquippedItems(player, this, partialTicks);
        }
        else renderEquippedItems(player, partialTicks, event);

        MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(player, this, partialTicks));
    }

    protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks, RenderPlayerEvent.Specials.Pre event)
    {
        ItemStack itemstack = player.inventory.armorItemInSlot(3);

        if (itemstack != null && event.renderHelmet)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedHead.postRender(0.0625f);
            float scale;

            if (itemstack.getItem() instanceof ItemBlock)
            {
                net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
                boolean is3d = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

                if (is3d || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
                {
                    scale = 0.625f;
                    GL11.glTranslatef(0f, -0.25f, 0f);
                    GL11.glRotatef(90f, 0f, 1f, 0f);
                    GL11.glScalef(scale, -scale, -scale);
                }

                renderManager.itemRenderer.renderItem(player, itemstack, 0);
            }
            else if (itemstack.getItem() == Items.skull)
            {
                scale = 1.0625f;
                GL11.glScalef(scale, -scale, -scale);
                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10)) gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5f, 0f, -0.5f, 1, 180f, itemstack.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }

        float scale;

        if (player.getCommandSenderName().equals("deadmau5") && player.func_152123_o())
        {
            bindTexture(player.getLocationSkin());

            for (int j = 0; j < 2; ++j)
            {
                float f9 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
                float f10 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
                GL11.glPushMatrix();
                GL11.glRotatef(f9, 0f, 1f, 0f);
                GL11.glRotatef(f10, 1f, 0f, 0f);
                GL11.glTranslatef(0.375f * (float) (j * 2 - 1), 0f, 0f);
                GL11.glTranslatef(0f, -0.375f, 0f);
                GL11.glRotatef(-f10, 1f, 0f, 0f);
                GL11.glRotatef(-f9, 0f, 1f, 0f);
                scale = 1.3333334f;
                GL11.glScalef(scale, scale, scale);
                modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean hasCape = player.func_152122_n();
        hasCape = event.renderCape && hasCape;
        float rotationYaw;

        if (hasCape && !player.isInvisible() && !player.getHideCape())
        {
            bindTexture(player.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0f, 0f, 0.125F);
            double x = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * (double) partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks);
            double y = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * (double) partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks);
            double z = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * (double) partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks);
            rotationYaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
            double sin = (double) MathHelper.sin(rotationYaw * (float) Math.PI / 180f);
            double cos = (double) (-MathHelper.cos(rotationYaw * (float) Math.PI / 180f));
            float yOffset = (float) y * 10f;

            if (yOffset < -6f) yOffset = -6f;
            if (yOffset > 32f) yOffset = 32f;

            float rotationX = (float) (x * sin + z * cos) * 100f;
            float rotationZ = (float) (x * cos - z * sin) * 100f;

            if (rotationX < 0f) rotationX = 0f;

            float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
            yOffset += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6f) * 32f * f8;

            if (player.isSneaking()) yOffset += 25f;

            GL11.glRotatef(6f + rotationX / 2f + yOffset, 1f, 0f, 0f);
            GL11.glRotatef(rotationZ / 2f, 0f, 0f, 1f);
            GL11.glRotatef(-rotationZ / 2f, 0f, 1f, 0f);
            GL11.glRotatef(180f, 0f, 1f, 0f);
            modelBipedMain.renderCloak(0.0625f);
            GL11.glPopMatrix();
        }

        ItemStack stack = player.inventory.getCurrentItem();

        if (stack != null && event.renderItem)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedRightArm.postRender(0.0625f);
            GL11.glTranslatef(-0.0625f, 0.4375f, 0.0625f);

            if (player.fishEntity != null) stack = new ItemStack(Items.stick);
            EnumAction enumaction = null;
            if (player.getItemInUseCount() > 0) enumaction = stack.getItemUseAction();

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(stack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3d = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, stack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (is3d || stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType()))
            {
                scale = 0.5f;
                GL11.glTranslatef(0f, 0.1875f, -0.3125f);
                scale *= 0.75f;
                GL11.glRotatef(20f, 1f, 0f, 0f);
                GL11.glRotatef(45f, 0f, 1f, 0f);
                GL11.glScalef(-scale, -scale, scale);
            }
            else if (stack.getItem() == Items.bow)
            {
                scale = 0.625f;
                GL11.glTranslatef(0f, 0.125f, 0.3125f);
                GL11.glRotatef(-20f, 0f, 1f, 0f);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100f, 1f, 0f, 0f);
                GL11.glRotatef(45f, 0f, 1f, 0f);
            }
            else if (stack.getItem().isFull3D())
            {
                scale = 0.625f;

                if (stack.getItem().shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180f, 0f, 0f, 1f);
                    GL11.glTranslatef(0f, -0.125f, 0f);
                }

                if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
                {
                    GL11.glTranslatef(0.05f, 0f, -0.1f);
                    GL11.glRotatef(-50f, 0f, 1f, 0f);
                    GL11.glRotatef(-10f, 1f, 0f, 0f);
                    GL11.glRotatef(-60f, 0f, 0f, 1f);
                }

                GL11.glTranslatef(0f, 0.1875f, 0f);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100f, 1f, 0f, 0f);
                GL11.glRotatef(45f, 0f, 1f, 0f);
            }
            else
            {
                scale = 0.375f;
                GL11.glTranslatef(0.25f, 0.1875f, -0.1875f);
                GL11.glScalef(scale, scale, scale);
                GL11.glRotatef(60f, 0f, 0f, 1f);
                GL11.glRotatef(-90f, 1f, 0f, 0f);
                GL11.glRotatef(20f, 0f, 0f, 1f);
            }

            float green;
            int meta;
            float red;

            if (stack.getItem().requiresMultipleRenderPasses())
            {
                for (meta = 0; meta < stack.getItem().getRenderPasses(stack.getItemDamage()); ++meta)
                {
                    int i = stack.getItem().getColorFromItemStack(stack, meta);
                    red = (float) (i >> 16 & 255) / 255f;
                    green = (float) (i >> 8 & 255) / 255f;
                    rotationYaw = (float) (i & 255) / 255f;
                    GL11.glColor4f(red, green, rotationYaw, 1f);
                    renderManager.itemRenderer.renderItem(player, stack, meta);
                }
            }
            else
            {
                meta = stack.getItem().getColorFromItemStack(stack, 0);
                float f11 = (float) (meta >> 16 & 255) / 255f;
                red = (float) (meta >> 8 & 255) / 255f;
                green = (float) (meta & 255) / 255f;
                GL11.glColor4f(f11, red, green, 1f);
                renderManager.itemRenderer.renderItem(player, stack, 0);
            }

            GL11.glPopMatrix();
        }
    }
}
