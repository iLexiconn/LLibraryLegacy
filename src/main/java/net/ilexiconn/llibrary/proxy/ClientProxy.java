package net.ilexiconn.llibrary.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.ClientEventHandler;
import net.ilexiconn.llibrary.client.render.player.RenderCustomPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    
    public void postInit()
    {
    	super.postInit();
    	
        RenderManager.instance.entityRenderMap.put(EntityPlayer.class, new RenderCustomPlayer());
    }
}
