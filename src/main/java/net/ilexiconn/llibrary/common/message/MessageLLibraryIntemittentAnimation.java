package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.animation.IntermittentAnimatableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageLLibraryIntemittentAnimation extends AbstractMessage<MessageLLibraryIntemittentAnimation>
{
    private int entityId;
    private byte intermittentAnimationId;

    public MessageLLibraryIntemittentAnimation()
    {
    }

    public MessageLLibraryIntemittentAnimation(Entity entity, byte intermittentAnimationId)
    {
        entityId = entity.getEntityId();
        this.intermittentAnimationId = intermittentAnimationId;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeByte(intermittentAnimationId);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        intermittentAnimationId = buf.readByte();
    }

    @Override
    public void handleClientMessage(MessageLLibraryIntemittentAnimation message, EntityPlayer player)
    {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);
        if (entity instanceof IntermittentAnimatableEntity)
        {
            ((IntermittentAnimatableEntity) entity).startIntermittentAnimation(message.intermittentAnimationId);
        }
    }

    @Override
    public void handleServerMessage(MessageLLibraryIntemittentAnimation message, EntityPlayer player)
    {
    }
}
