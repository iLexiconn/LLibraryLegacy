package net.ilexiconn.llibrary.common;

import cpw.mods.fml.client.GuiModList;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.common.event.ServerEventHandler;
import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.ilexiconn.llibrary.common.update.UpdateHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);
        
        UpdateHelper.registerUpdateChecker(LLibrary.instance, "http://pastebin.com/raw.php?i=TGiS6kuk", "https://ilexiconn.net/", null, "https://ilexiconn.net/llibrary/logo.png");
    }

    public void postInit()
    {

    }

    public void openChangelogGui(ModUpdateContainer mod, String version)
    {

    }
}
