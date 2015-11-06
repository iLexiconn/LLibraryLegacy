package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLLibrarySurvivalTab extends AbstractMessage<MessageLLibrarySurvivalTab> {
    public int tabIndex;

    public MessageLLibrarySurvivalTab() {

    }

    public MessageLLibrarySurvivalTab(int index) {
        tabIndex = index;
    }

    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageLLibrarySurvivalTab message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageLLibrarySurvivalTab message, EntityPlayer player) {
        for (SurvivalTab survivalTab : SurvivalTab.getSurvivalTabList()) {
            if (survivalTab.getIndex() == message.tabIndex) {
                MinecraftForge.EVENT_BUS.post(new SurvivalTab.ClickEvent(survivalTab, player));
            }
        }
    }

    public void fromBytes(ByteBuf buf) {
        tabIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(tabIndex);
    }
}
