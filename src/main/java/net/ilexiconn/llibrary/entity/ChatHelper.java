package net.ilexiconn.llibrary.entity;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * @author FiskFille
 */
public class ChatHelper
{
	public static void chatTo(ICommandSender commandSender, String message)
	{
		commandSender.addChatMessage(new ChatComponentText(message));
	}
}