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
        
        UpdateHelper.registerUpdateChecker(LLibrary.instance, "http://ilexiconn.net/llibrary/data/versions.txt", "http://ilexiconn.net/llibrary", null, "http://ilexiconn.net/llibrary/data/logo.png");
    }

    public void postInit()
    {

    }

    public void openChangelogGui(ModUpdateContainer mod, String version)
    {

    }
}
