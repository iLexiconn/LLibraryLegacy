package net.ilexiconn.llibrary.common.command;

import net.ilexiconn.llibrary.common.color.EnumChatColor;

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