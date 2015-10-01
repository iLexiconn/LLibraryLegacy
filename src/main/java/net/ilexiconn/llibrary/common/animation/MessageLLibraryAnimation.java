package net.ilexiconn.llibrary.common.animation;

import cpw.mods.fml.client.FMLClientHandler;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MessageLLibraryAnimation extends AbstractMessage<MessageLLibraryAnimation>
{
    public byte animationId;
    public int entityId;

    public MessageLLibraryAnimation()
    {

    }

    public MessageLLibraryAnimation(byte animation, int entity)
    {
        animationId = animation;
        entityId = entity;
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeByte(animationId);
        buffer.writeInt(entityId);
    }

    public void fromBytes(ByteBuf buffer)
    {
        animationId = buffer.readByte();
        entityId = buffer.readInt();
    }

    public void handleClientMessage(MessageLLibraryAnimation message, EntityPlayer player)
    {
        World world = FMLClientHandler.instance().getWorldClient();
        IAnimated entity = (IAnimated) world.getEntityByID(message.entityId);
        if (entity != null && message.animationId != 0)
        {
            entity.setAnimation(entity.animations()[0]);
            entity.setAnimationTick(0);
        }
    }

    public void handleServerMessage(MessageLLibraryAnimation message, EntityPlayer player)
    {

    }
}
