package net.ilexiconn.llibrary.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

/**
 * Helper class for deserializing chat {@link net.minecraft.util.IChatComponent}.
 *
 * @author FiskFille
 * @see net.minecraft.util.IChatComponent
 * @since 0.1.0
 */
public class ChatHelper
{
    /**
     * @see net.minecraft.util.IChatComponent
     * @since 0.1.0
     */
    public static void chatToFromJson(ICommandSender commandSender, String json)
    {
        IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(json);
        commandSender.addChatMessage(ichatcomponent);
    }

    /**
     * @see net.minecraft.util.IChatComponent
     * @see net.ilexiconn.llibrary.common.command.ChatMessage
     * @since 0.1.0
     */
    public static void chatTo(ICommandSender commandSender, ChatMessage... chatMessages)
    {
        String json = "{text:\"\",extra:[";

        for (int i = 0; i < chatMessages.length; ++i)
        {
            json += (i == 0 ? "" : ",") + "{text:\"" + chatMessages[i].message + "\",color:" + chatMessages[i].color.colorCode + "}";
        }

        json += "]}";
        chatToFromJson(commandSender, json);
    }
}