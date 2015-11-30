package net.ilexiconn.llibrary.common.message;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class MessageLLibrarySurvivalTab extends AbstractMessage<MessageLLibrarySurvivalTab> {
    public int tabIndex;

    public MessageLLibrarySurvivalTab() {

    }

    public MessageLLibrarySurvivalTab(int index) {
        tabIndex = index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageLLibrarySurvivalTab message, EntityPlayer player) {

    }

    @Override
    public void handleServerMessage(MessageLLibrarySurvivalTab message, EntityPlayer player) {
        for (SurvivalTab survivalTab : SurvivalTab.getSurvivalTabList()) {
            if (survivalTab.getIndex() == message.tabIndex) {
                MinecraftForge.EVENT_BUS.post(new SurvivalTab.ClickEvent(survivalTab, player));
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tabIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tabIndex);
    }
}
