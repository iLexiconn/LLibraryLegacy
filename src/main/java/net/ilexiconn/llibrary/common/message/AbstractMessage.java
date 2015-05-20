package net.ilexiconn.llibrary.common.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractMessage<REQ extends AbstractMessage> implements IMessage, IMessageHandler<REQ, IMessage>
{
    public IMessage onMessage(REQ message, MessageContext ctx)
    {
        if (ctx.side.isClient())
        {
            Object obj = null;
            try 
            {
        	    obj = Class.forName("cpw.mods.fml.client.FMLClientHandler").getMethod("instance", new Class[0]).invoke(null, new Object[0]);
        	    handleClientMessage(message, (EntityPlayer)obj.getClass().getMethod("getClientPlayerEntity", new Class[0]).invoke(obj, new Object[0]));
	        }
	        catch (Exception e) 
	        {
		        e.printStackTrace();
	        }
        }
        else
        {
            handleServerMessage(message, ctx.getServerHandler().playerEntity);
        }

        return null;
    }

    public abstract void handleClientMessage(REQ message, EntityPlayer player);

    public abstract void handleServerMessage(REQ message, EntityPlayer player);
}
