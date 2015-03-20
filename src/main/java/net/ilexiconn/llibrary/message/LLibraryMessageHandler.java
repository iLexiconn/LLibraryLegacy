package net.ilexiconn.llibrary.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llibrary.IContentHandler;

public class LLibraryMessageHandler implements IContentHandler
{
	public static SimpleNetworkWrapper networkWrapper;
	
	private static int messageId;
	
	public void init()
	{
		networkWrapper = new SimpleNetworkWrapper("llibrary");
	}

    public void gameRegistry() throws Exception
    {
        registerMessage(MessageClickSurvivalTab.class, MessageClickSurvivalTab.class);
    }

    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
    {
        networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, Side.CLIENT);
        networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, Side.SERVER);
    }
}
