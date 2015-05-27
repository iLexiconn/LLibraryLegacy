package net.ilexiconn.llibrary.common.command;

import net.ilexiconn.llibrary.common.color.EnumChatColor;

/**
 * Container class for chat messages.
 * 
 * @see net.ilexiconn.llibrary.common.command.ChatHelper
 * @author FiskFille
 * @since 0.1.0
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