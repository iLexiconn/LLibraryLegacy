package net.ilexiconn.llibrary.common.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

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