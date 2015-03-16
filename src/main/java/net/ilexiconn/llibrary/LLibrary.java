package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.proxy.ServerProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "llibrary", name = "LLibrary", version = "${version}")
public class LLibrary
{
    @Mod.Instance("llibrary")
    public static LLibrary instance;

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.proxy.ServerProxy", clientSide = "net.ilexiconn.llibrary.proxy.ClientProxy")
    private static ServerProxy proxy;

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
    
    @Mod.EventHandler
    private void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }
}
