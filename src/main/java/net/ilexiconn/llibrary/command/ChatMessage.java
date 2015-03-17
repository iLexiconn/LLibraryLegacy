package net.ilexiconn.llibrary.command;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatMessage
{
	public String message;
	public String color;
	
	public ChatMessage(String message, String color)
	{
		this.message = message;
		this.color = color;
	}
}