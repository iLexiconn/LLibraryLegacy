package net.ilexiconn.llibrary.entity;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ChatHelper
{
	public static void chat(ICommandSender commandSender, String message)
	{
		commandSender.addChatMessage(new ChatComponentText(message));
	}
}