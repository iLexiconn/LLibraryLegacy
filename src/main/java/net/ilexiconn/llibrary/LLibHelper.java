package net.ilexiconn.llibrary;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class LLibHelper
{
	public static void chat(ICommandSender commandSender, String message)
	{
		commandSender.addChatMessage(new ChatComponentText(message));
	}
}