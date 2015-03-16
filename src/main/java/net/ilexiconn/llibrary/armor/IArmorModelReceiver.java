package net.ilexiconn.llibrary.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

public interface IArmorModelReceiver
{
    @SideOnly(Side.CLIENT)
    ModelBiped getArmorModel(int pass);

    String getModelTextureName();
}
