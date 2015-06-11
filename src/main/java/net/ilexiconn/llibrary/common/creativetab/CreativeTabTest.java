package net.ilexiconn.llibrary.common.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CreativeTabTest extends CreativeTabs implements ITexturedCreativeTab
{
    public CreativeTabTest()
    {
        super("llibrarytest");
    }

    @Override
    public ResourceLocation getCustomIcon()
    {
        return new ResourceLocation("textures/items/apple.png");
    }

    @Override
    public Item getTabIconItem()
    {
        return Items.baked_potato;
    }
}
