package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.common.ServerProxy;
import net.ilexiconn.llibrary.common.command.CommandLLibrary;
import net.ilexiconn.llibrary.common.content.ContentHelper;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.ilexiconn.llibrary.common.content.InitializationState;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.llibrary.common.message.MessageLLibrarySurvivalTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = "llibrary", name = "LLibrary", version = "${version}")
public class LLibrary
{
    @Mod.Instance("llibrary")
    public static LLibrary instance;

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.common.ServerProxy", clientSide = "net.ilexiconn.llibrary.client.ClientProxy")
    public static ServerProxy proxy;

    public static SimpleNetworkWrapper networkWrapper;

    public static LoggerHelper logger = new LoggerHelper("llibrary");

    @Mod.EventHandler
    private void preInit(FMLPreInitializationEvent event)
    {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("llibrary");
        networkWrapper.registerMessage(MessageLLibrarySurvivalTab.class, MessageLLibrarySurvivalTab.class, 0, Side.SERVER);

        proxy.preInit(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    private void init(FMLInitializationEvent event)
    {
        for (Map.Entry<InitializationState, IContentHandler> contentHandlerEntry : ContentHelper.getTimedHandlers().entrySet())
        {
            if (contentHandlerEntry.getKey() == InitializationState.INIT) ContentHelper.init(true, contentHandlerEntry.getValue());
        }
    }

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();

        for (Map.Entry<InitializationState, IContentHandler> contentHandlerEntry : ContentHelper.getTimedHandlers().entrySet())
        {
            if (contentHandlerEntry.getKey() == InitializationState.POSTINIT) ContentHelper.init(true, contentHandlerEntry.getValue());
        }
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandLLibrary());
    }
}
