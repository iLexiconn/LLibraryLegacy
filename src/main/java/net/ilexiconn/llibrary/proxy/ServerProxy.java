package net.ilexiconn.llibrary.proxy;

import net.ilexiconn.llibrary.entity.EntityHelper;
import net.ilexiconn.llibrary.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.event.ServerEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy
{
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);
    }
}
