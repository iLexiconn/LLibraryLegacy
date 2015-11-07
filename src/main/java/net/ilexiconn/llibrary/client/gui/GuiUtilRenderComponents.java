package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiUtilRenderComponents {
    public static String func_178909_a(String p_178909_0_, boolean p_178909_1_) {
        return !p_178909_1_ && !Minecraft.getMinecraft().gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }

    public static List func_178908_a(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_) {
        int j = 0;
        ChatComponentText chatcomponenttext = new ChatComponentText("");
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList(p_178908_0_);

        for (int k = 0; k < arraylist1.size(); ++k) {
            IChatComponent ichatcomponent1 = (IChatComponent) arraylist1.get(k);
            String s = ichatcomponent1.getUnformattedTextForChat();
            boolean flag2 = false;
            String s1;

            if (s.contains("\n")) {
                int l = s.indexOf(10);
                s1 = s.substring(l + 1);
                s = s.substring(0, l + 1);
                ChatComponentText chatcomponenttext1 = new ChatComponentText(s1);
                chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                arraylist1.add(k + 1, chatcomponenttext1);
                flag2 = true;
            }

            String s4 = func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s, p_178908_4_);
            s1 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int j1 = p_178908_2_.getStringWidth(s1);
            ChatComponentText chatcomponenttext2 = new ChatComponentText(s1);
            chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());

            if (j + j1 > p_178908_1_) {
                String s2 = p_178908_2_.trimStringToWidth(s4, p_178908_1_ - j, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && s3.length() > 0) {
                    int i1 = s2.lastIndexOf(" ");

                    if (i1 >= 0 && p_178908_2_.getStringWidth(s4.substring(0, i1)) > 0) {
                        s2 = s4.substring(0, i1);

                        if (p_178908_3_) {
                            ++i1;
                        }

                        s3 = s4.substring(i1);
                    } else if (j > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }

                    ChatComponentText chatcomponenttext3 = new ChatComponentText(s3);
                    chatcomponenttext3.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    arraylist1.add(k + 1, chatcomponenttext3);
                }

                j1 = p_178908_2_.getStringWidth(s2);
                chatcomponenttext2 = new ChatComponentText(s2);
                chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag2 = true;
            }

            if (j + j1 <= p_178908_1_) {
                j += j1;
                chatcomponenttext.appendSibling(chatcomponenttext2);
            } else {
                flag2 = true;
            }

            if (flag2) {
                arraylist.add(chatcomponenttext);
                j = 0;
                chatcomponenttext = new ChatComponentText("");
            }
        }

        arraylist.add(chatcomponenttext);
        return arraylist;
    }
}
