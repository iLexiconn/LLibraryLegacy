package net.ilexiconn.llibrary.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.survivaltab.SurvivalTab;
import net.ilexiconn.llibrary.survivaltab.TabRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

public class MessageClickSurvivalTab extends AbstractMessage<MessageClickSurvivalTab>
{
	public MessageClickSurvivalTab() {}

	private int id;

	public MessageClickSurvivalTab(int idClicked)
	{
		this.id = idClicked;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(id);
	}

	@Override
	public void handleClientMessage(MessageClickSurvivalTab message, EntityPlayer player) {}

	@Override
	public void handleServerMessage(MessageClickSurvivalTab message, EntityPlayer player) 
	{
		TabRegistry.getSurvivalTab(id).getSurvivalTab().openContainerGui(player);
	}
}
