package net.ilexiconn.llibrary.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

/**
 * @author FiskFille
 */
public class ChatHelper
{
    public static void chatToFromJSON(ICommandSender commandSender, String json)
    {
        IChatComponent ichatcomponent = IChatComponent.Serializer.func_150699_a(json);
        commandSender.addChatMessage(ichatcomponent);
    }

    public static void chatTo(ICommandSender commandSender, ChatMessage... chatMessages)
    {
        String json = "{text:\"\",extra:[";

        for (int i = 0; i < chatMessages.length; ++i)
        {
            json += (i == 0 ? "" : ",") + "{text:\"" + chatMessages[i].message + "\",color:" + chatMessages[i].color.colorCode + "}";
        }

        json += "]}";
        chatToFromJSON(commandSender, json);
    }
}