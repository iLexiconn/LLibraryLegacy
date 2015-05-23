package net.ilexiconn.llibrary.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.survivaltab.SurvivalTab;
import net.ilexiconn.llibrary.common.survivaltab.TabHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MessageLLibrarySurvivalTab extends AbstractMessage<MessageLLibrarySurvivalTab>
{
    public int tabIndex;

    public MessageLLibrarySurvivalTab()
    {

    }

    public MessageLLibrarySurvivalTab(int index)
    {
        tabIndex = index;
    }

    public void handleClientMessage(MessageLLibrarySurvivalTab message, EntityPlayer player)
    {

    }

    public void handleServerMessage(MessageLLibrarySurvivalTab message, EntityPlayer player)
    {
        for (SurvivalTab survivalTab : TabHelper.getSurvivalTabs())
        {
            if (survivalTab.getTabIndex() == message.tabIndex)
            {
                survivalTab.getSurvivalTab().openContainer(Minecraft.getMinecraft(), player);
            }
        }
    }

    public void fromBytes(ByteBuf buf)
    {
        tabIndex = buf.readInt();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(tabIndex);
    }
}
