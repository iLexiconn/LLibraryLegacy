package test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.ContentHandlerList;
import net.ilexiconn.llibrary.IContentHandler;
import net.ilexiconn.llibrary.creativetab.CreativeTabSearch;
import net.ilexiconn.llibrary.entity.EntityHelper;
import net.ilexiconn.llibrary.item.ItemHelper;
import net.ilexiconn.llibrary.item.SpawnEgg;
import net.ilexiconn.llibrary.update.UpdateHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import test.proxy.ServerProxy;

@Mod(modid = "test", name = "Test Mod", version = "1.0")
public class Test implements IContentHandler
{
    @SidedProxy(serverSide = "test.proxy.ServerProxy", clientSide = "test.proxy.ClientProxy")
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        ContentHandlerList.createList(this).init();
        UpdateHelper.registerUpdateChecker(this, "r5zKe0UF", "https://twitter.com/");
        CreativeTabs test = new CreativeTabSearch("test")
        {
            public Item getTabIconItem()
            {
                return Items.bone;
            }
        };
        GameRegistry.registerItem(new SpawnEgg(test), "spawnegg");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        EntityHelper.removeLivingEntity(EntityHorse.class); //R.I.P Horse
        ItemHelper.removeRecipe(Blocks.crafting_table); //R.I.P Crafting Table
    }

    public void init()
    {
        proxy.init();
    }

    public void gameRegistry() throws Exception
    {
        EntityHelper.registerEntity("test", EntityTest.class, 0, 0);
    }
}