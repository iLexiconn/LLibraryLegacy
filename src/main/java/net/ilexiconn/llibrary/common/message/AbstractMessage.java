package net.ilexiconn.llibrary.common.message;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * {@link net.minecraftforge.fml.common.network.simpleimpl.IMessage} and {@link net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler} implemented in the same class.
 *
 * @param <M> The message to receive data from. This should be the same class. Make sure to use this, otherwise you can't access your variables in the message!
 * @author iLexiconn
 * @since 0.5.0
 */
public abstract class AbstractMessage<M extends AbstractMessage<?>> implements IMessage, IMessageHandler<M, IMessage> {
    protected MessageContext messageContext;

    /**
     * Register the message to the {@link net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper}.
     *
     * @param networkWrapper The {@link net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper} instance.
     * @param clazz          The AbstractMessage class to register.
     * @param id             The id to use for the registration of the message.
     * @param side           The {@link net.minecraftforge.fml.relauncher.Side} to register the message on.
     */
    public static <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(SimpleNetworkWrapper networkWrapper, Class<T> clazz, int id, Side side) {
        networkWrapper.registerMessage(clazz, clazz, id, side);
    }

    @Override
    public IMessage onMessage(final M message, final MessageContext ctx) {
        messageContext = ctx;

        LLibrary.proxy.scheduleTask(ctx, new Runnable() {
            @Override
            public void run() {
                if (ctx.side.isClient()) {
                    handleClientMessage(message, LLibrary.proxy.getClientPlayer());
                } else {
                    handleServerMessage(message, ctx.getServerHandler().playerEntity);
                }
            }
        });

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
