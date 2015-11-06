package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLLibraryAnimation extends AbstractMessage<MessageLLibraryAnimation> {
    public int animationId;
    public int entityId;

    public MessageLLibraryAnimation() {

    }

    public MessageLLibraryAnimation(int animation, int entity) {
        animationId = animation;
        entityId = entity;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(animationId);
        buffer.writeInt(entityId);
    }

    public void fromBytes(ByteBuf buffer) {
        animationId = buffer.readInt();
        entityId = buffer.readInt();
    }

    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageLLibraryAnimation message, EntityPlayer player) {
        IAnimated entity = (IAnimated) player.worldObj.getEntityByID(message.entityId);
        if (entity != null && message.animationId != 0) {
            entity.setAnimation(entity.animations()[message.animationId]);
            entity.setAnimationTick(0);
        }
    }

    public void handleServerMessage(MessageLLibraryAnimation message, EntityPlayer player) {

    }
}
