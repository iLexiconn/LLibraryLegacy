package net.ilexiconn.llibrary;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.ilexiconn.llibrary.command.CommandLLibrary;
import net.ilexiconn.llibrary.proxy.ServerProxy;

@Mod(modid = "llibrary", name = "LLibrary", version = "${version}")
public class LLibrary
{
    @Instance("llibrary")
    public static LLibrary instance;

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.proxy.ServerProxy", clientSide = "net.ilexiconn.llibrary.proxy.ClientProxy")
	public static ServerProxy proxy;

    @EventHandler
    private void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }

    @EventHandler
    private void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CommandLLibrary());
    }
}