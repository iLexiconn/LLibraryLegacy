package net.ilexiconn.llibrary.common.message;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractMessage<REQ extends AbstractMessage> implements IMessage, IMessageHandler<REQ, IMessage>
{
    public IMessage onMessage(REQ message, MessageContext ctx)
    {
        if (ctx.side.isClient()) handleClientMessage(message, LLibrary.proxy.getClientPlayer());
        else handleServerMessage(message, ctx.getServerHandler().playerEntity);

        return null;
    }

    public abstract void handleClientMessage(REQ message, EntityPlayer player);

    public abstract void handleServerMessage(REQ message, EntityPlayer player);
}
