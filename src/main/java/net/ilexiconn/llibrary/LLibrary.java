package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.common.ServerProxy;
import net.ilexiconn.llibrary.common.command.CommandLLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
