package net.ilexiconn.llibrary.client.render;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderHelper
{
	private static Map<Class<? extends ModelBase>, List<IModelExtention>> modelExtentions = Maps.newHashMap();

    public static ModelBiped modelBipedMain;
	
    private static ResourceLocation glintTexture = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public static void registerModelExtention(Class<? extends ModelBase> modelClazz, IModelExtention modelExtention)
    {
    	List<IModelExtention> extentionsForModel = modelExtentions.get(modelClazz);
    	
    	if(extentionsForModel == null)
    	{
    		extentionsForModel = new ArrayList<IModelExtention>();
    	}
    	
    	extentionsForModel.add(modelExtention);
    	
    	modelExtentions.put(modelClazz, extentionsForModel);
    }
    
    public static List<IModelExtention> getModelExtentionsFor(Class<? extends ModelBase> clazz)
    {
    	return modelExtentions.get(clazz);
    }
    
    public static void renderItemIn3d(ItemStack stack)
    {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        if (textureManager == null) return;
        Item item = stack.getItem();
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(180f, 0f, 0f, 1f);
        GL11.glTranslatef(-0.5f, -0.5f, 1 / 32f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        int passes = item.getRenderPasses(stack.getItemDamage());

        for (int pass = 0; pass < passes; pass++)
        {
            if (stack.getItemSpriteNumber() == 0) textureManager.bindTexture(TextureMap.locationBlocksTexture);
            else textureManager.bindTexture(TextureMap.locationItemsTexture);
            IIcon icon = item.getIcon(stack, pass);
            float minU = icon.getMinU();
            float maxU = icon.getMaxU();
            float minV = icon.getMinV();
            float maxV = icon.getMaxV();
            setColorFromInt(item.getColorFromItemStack(stack, pass));
            ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0625f);
        }

        if (stack.hasEffect(0))
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            textureManager.bindTexture(glintTexture);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float f7 = 0.76f;
            GL11.glColor4f(0.5f * f7, 0.25f * f7, 0.8f * f7, 1f);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float f8 = 0.125f;
            GL11.glScalef(f8, f8, f8);
            float f9 = Minecraft.getSystemTime() % 3000l / 3000f * 8f;
            GL11.glTranslatef(f9, 0f, 0f);
            GL11.glRotatef(-50f, 0f, 0f, 1f);
            ItemRenderer.renderItemIn2D(tessellator, 0f, 0f, 1f, 1f, 256, 256, 0625f);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = Minecraft.getSystemTime() % 4873l / 4873f * 8f;
            GL11.glTranslatef(-f9, 0f, 0f);
            GL11.glRotatef(10f, 0f, 0f, 1f);
            ItemRenderer.renderItemIn2D(tessellator, 0f, 0f, 1f, 1f, 256, 256, 0625f);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private static void setColorFromInt(int color)
    {
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        GL11.glColor4f(r, g, b, 1f);
    }
}
