package net.ilexiconn.llibrary.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fiskfille.tf.common.packet.PacketBroadcastState;
import fiskfille.tf.common.packet.PacketCloudtrapJetpack;
import fiskfille.tf.common.packet.PacketHandleStealthTransformation;
import fiskfille.tf.common.packet.PacketHandleTransformation;
import fiskfille.tf.common.packet.PacketSendFlying;
import fiskfille.tf.common.packet.PacketSyncStates;
import fiskfille.tf.common.packet.PacketTransformersAction;
import fiskfille.tf.common.packet.PacketVehicleNitro;
import fiskfille.tf.common.packet.PacketVurpSniperShoot;

public class LLibraryMessageHandler 
{
	public static SimpleNetworkWrapper networkWrapper;
	
	private static int messageId;
	
	public static void init()
	{
		networkWrapper = new SimpleNetworkWrapper("llibrary");
		
		registerMessage(MessageClickSurvivalTab.class, MessageClickSurvivalTab.class);
	}

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
    {
        networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, Side.CLIENT);
        networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, Side.SERVER);
    }
}
