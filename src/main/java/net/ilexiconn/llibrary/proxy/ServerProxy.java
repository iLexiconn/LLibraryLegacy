package net.ilexiconn.llibrary.proxy;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.entity.EntityHelper;
import net.ilexiconn.llibrary.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.event.ServerEventHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.ilexiconn.llibrary.update.UpdateHelper;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);
        UpdateHelper.registerUpdateChecker(LLibrary.instance, "TGiS6kuk", "https://ilexiconn.net/");
    }
    
    public void postInit()
    {
        
    }
    
    public void openChangelogGui(ModUpdateContainer mod, String version)
    {
        
    }
}
