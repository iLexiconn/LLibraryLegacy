package net.ilexiconn.llibrary;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.ilexiconn.llibrary.command.CommandLLibrary;
import net.ilexiconn.llibrary.config.LLibraryConfig;
import net.ilexiconn.llibrary.proxy.ServerProxy;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = "llibrary", name = "LLibrary", version = "${version}")
public class LLibrary
{
    @Instance("llibrary")
    public static LLibrary instance;

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.proxy.ServerProxy", clientSide = "net.ilexiconn.llibrary.proxy.ClientProxy")
	public static ServerProxy proxy;

    public static LLibraryConfig config;
    
    @EventHandler
    private void preInit(FMLPreInitializationEvent event)
    {
    	Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
    	configuration.load();
    	config = new LLibraryConfig();
    	config.init(configuration);
    	configuration.save();
    	
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