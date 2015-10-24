package net.ilexiconn.llibrary.common.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @deprecated      Use {@link net.ilexiconn.llibrary.api.AbstractMessage} instead.
 */
@Deprecated
public abstract class AbstractMessage<M extends AbstractMessage> implements IMessage, IMessageHandler<M, IMessage>
{
    public IMessage onMessage(M message, MessageContext ctx)
    {
        if (ctx.side.isClient())
            handleClientMessage(message, LLibrary.proxy.getClientPlayer());
        else
            handleServerMessage(message, ctx.getServerHandler().playerEntity);

        return null;
    }

    public abstract void handleClientMessage(M message, EntityPlayer player);

    public abstract void handleServerMessage(M message, EntityPlayer player);
}
