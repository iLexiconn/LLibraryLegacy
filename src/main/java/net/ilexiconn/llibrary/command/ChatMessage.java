package net.ilexiconn.llibrary.command;

import net.ilexiconn.llibrary.color.EnumChatColor;

/**
 * @author FiskFille
 */
public class ChatMessage
{
	public String message;
	public EnumChatColor color;
	
	public ChatMessage(String m, EnumChatColor c)
	{
		message = m;
		color = c;
	}
}