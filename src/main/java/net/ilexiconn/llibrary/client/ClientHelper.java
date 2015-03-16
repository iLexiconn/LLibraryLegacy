package net.ilexiconn.llibrary.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.item.Item3dRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientHelper
{
    public static void registerItem3dRenderer(Item item, ModelBase model, ResourceLocation texture)
    {
        MinecraftForgeClient.registerItemRenderer(item, new Item3dRenderer(item, model, texture));
    }
}
