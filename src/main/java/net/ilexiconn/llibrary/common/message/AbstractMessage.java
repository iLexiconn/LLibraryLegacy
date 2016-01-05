package net.ilexiconn.llibrary.common.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.player.EntityPlayer;

/**
 * {@link IMessage} and {@link IMessageHandler} implemented in the same class.
 *
 * @param <M> The message to receive data from. This should be the same class. Make sure to use this, otherwise you can't access your variables in the message!
 * @author iLexiconn
 * @since 0.5.0
 */
public abstract class AbstractMessage<M extends AbstractMessage> implements IMessage, IMessageHandler<M, IMessage> {
    /**
     * Register the message to the {@link cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper}.
     *
     * @param networkWrapper The {@link cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper} instance.
     * @param clazz          The AbstractMessage class to register.
     * @param id             The id to use for the registration of the message.
     * @param side           The {@link cpw.mods.fml.relauncher.Side} to register the message on.
     */
    public static <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz, int id, Side side) {
        networkWrapper.registerMessage(clazz, clazz, id, side);
    }

    protected MessageContext messageContext;

    @Override
    public IMessage onMessage(M message, MessageContext ctx) {
        messageContext = ctx;
        if (ctx.side.isClient()) {
            handleClientMessage(message, LLibrary.proxy.getClientPlayer());
        } else {
            handleServerMessage(message, ctx.getServerHandler().playerEntity);
        }
        return null;
    }

    /**
     * Executes when message received on CLIENT side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message The message instance with all variables.
     * @param player  The client player entity.
     */
    @SideOnly(Side.CLIENT)
    public abstract void handleClientMessage(M message, EntityPlayer player);

    /**
     * Executes when message received on SERVER side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message The message instance with all variables.
     * @param player  The player who sent the message to the server.
     */
    public abstract void handleServerMessage(M message, EntityPlayer player);
}
