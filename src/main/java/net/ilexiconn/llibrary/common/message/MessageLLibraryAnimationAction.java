package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLLibraryAnimationAction extends net.ilexiconn.llibrary.api.AbstractMessage<MessageLLibraryAnimationAction>
{
    public int animationId;
    public int entityId;
    public int activationTick;

    public MessageLLibraryAnimationAction()
    {

    }

    public MessageLLibraryAnimationAction(int animation, int entity, int tick)
    {
        animationId = animation;
        entityId = entity;
        activationTick = tick;
    }

    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageLLibraryAnimationAction message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        IAnimated animated = (IAnimated) entity;
        if (entity != null && message.animationId != 0)
        {
            animated.getAnimation().actions.get(message.activationTick).execute(message.animationId, entity);
        }
    }

    public void handleServerMessage(MessageLLibraryAnimationAction message, EntityPlayer player)
    {

    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(animationId);
        buffer.writeInt(entityId);
        buffer.writeInt(activationTick);
    }

    public void fromBytes(ByteBuf buffer)
    {
        animationId = buffer.readInt();
        entityId = buffer.readInt();
        activationTick = buffer.readInt();
    }
}