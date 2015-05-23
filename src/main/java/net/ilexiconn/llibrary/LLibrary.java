package net.ilexiconn.llibrary;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llibrary.common.ServerProxy;
import net.ilexiconn.llibrary.common.command.CommandLLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;

@Mod(modid = "llibrary", name = "LLibrary", version = "${version}")
public class LLibrary
{
    @Mod.Instance("llibrary")
    public static LLibrary instance;

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.common.ServerProxy", clientSide = "net.ilexiconn.llibrary.client.ClientProxy")
    public static ServerProxy proxy;

    public static SimpleNetworkWrapper networkWrapper;

    @Mod.EventHandler
    private void preInit(FMLPreInitializationEvent event)
    {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("llibrary");
        networkWrapper.registerMessage(MessageLLibrarySurvivalTab.class, MessageLLibrarySurvivalTab.class, 0, Side.SERVER);

        proxy.preInit();
    }

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandLLibrary());
    }
}
