package net.ilexiconn.llibrary.proxy;

import net.ilexiconn.llibrary.client.render.player.RenderCustomPlayer;
import net.ilexiconn.llibrary.entity.EntityHelper;
import net.ilexiconn.llibrary.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.event.ServerEventHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);
    }
    
    public void postInit()
    {
    	
    }
    
    public void openChangelogGui(ModUpdateContainer mod, String version)
    {
    	
    }
}
