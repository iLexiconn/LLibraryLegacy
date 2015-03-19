package net.ilexiconn.llibrary.potion;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemPotion;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PotionCustom extends Potion
{
	public final ResourceLocation texture;
	
	public PotionCustom(int id, boolean isEffectBad, int liquidColor, ResourceLocation texture, int iconIndexX, int iconIndexY)
	{
		super(id, isEffectBad, liquidColor);
		this.texture = texture;
		this.setIconIndex(iconIndexX, iconIndexY);
	}
	
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("llibrary", "textures/potion/test.png"));
		return super.getStatusIconIndex();
	}
}