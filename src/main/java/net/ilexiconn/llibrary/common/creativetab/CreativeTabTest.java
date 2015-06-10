package net.ilexiconn.llibrary.common.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CreativeTabTest extends CreativeTabs implements ITexturedCreativeTab
{
    public CreativeTabTest()
    {
        super("test");
    }

    public static final CreativeTabTest test = new CreativeTabTest();

    @Override
    public ResourceLocation getCustomIcon()
    {
        return new ResourceLocation("textures/items/apple.png");
    }

    @Override
    public Item getTabIconItem()
    {
        return null;
    }
}
