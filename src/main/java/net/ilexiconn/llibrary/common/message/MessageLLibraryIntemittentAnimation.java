package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.animation.IntermittentAnimatableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLLibraryIntemittentAnimation extends AbstractMessage<MessageLLibraryIntemittentAnimation> {
    private int entityId;
    private byte intermittentAnimationId;

    public MessageLLibraryIntemittentAnimation() {

    }

    public MessageLLibraryIntemittentAnimation(Entity entity, byte id) {
        entityId = entity.getEntityId();
        intermittentAnimationId = id;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(intermittentAnimationId);
    }

    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        intermittentAnimationId = buf.readByte();
    }

    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageLLibraryIntemittentAnimation message, EntityPlayer player) {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);
        if (entity instanceof IntermittentAnimatableEntity) {
            ((IntermittentAnimatableEntity) entity).startIntermittentAnimation(message.intermittentAnimationId);
        }
    }

    public void handleServerMessage(MessageLLibraryIntemittentAnimation message, EntityPlayer player) {

    }
}
