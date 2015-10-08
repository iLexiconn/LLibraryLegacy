package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.entity.player.EntityPlayer;

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
        IAnimated entity = (IAnimated) player.worldObj.getEntityByID(message.entityId);
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
