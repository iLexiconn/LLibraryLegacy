package net.ilexiconn.llibrary.common.command;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.color.EnumChatColor;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.ilexiconn.llibrary.common.update.ModUpdateContainer;
import net.ilexiconn.llibrary.common.update.UpdateHelper;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandLLibrary extends CommandBase
{
    public String getCommandName()
    {
        return "llibrary";
    }

    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/llibrary list OR /llibrary update <modid> OR /llibrary changelog <modid> <version>";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        String title = "[LLibHelper]" + EnumChatFormatting.YELLOW + " ";
        List<ModUpdateContainer> outdatedMods = VersionHandler.getOutdatedMods();

        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("list"))
            {
                if (args.length > 1)
                {
                    throw new WrongUsageException("/llibrary list");
                }

                ChatHelper.chatTo(sender, new ChatMessage("--- Showing a list of outdated mods ---", EnumChatColor.DARK_GREEN));

                for (ModUpdateContainer mod : outdatedMods)
                {
                    ChatHelper.chatTo(sender, new ChatMessage("(" + mod.modid + ") ", EnumChatColor.BLUE), new ChatMessage(mod.name + " version " + mod.version + " - Latest version: " + mod.latestVersion, EnumChatColor.WHITE));
                }

                ChatHelper.chatTo(sender, new ChatMessage("Use ", EnumChatColor.GREEN), new ChatMessage("/llibrary update <modid>", EnumChatColor.YELLOW), new ChatMessage(" to update the desired mod, ", EnumChatColor.GREEN), new ChatMessage("or", EnumChatColor.RED));
                ChatHelper.chatTo(sender, new ChatMessage("Use ", EnumChatColor.GREEN), new ChatMessage("/llibrary changelog <modid> <version>", EnumChatColor.YELLOW), new ChatMessage(" to see its version changelog.", EnumChatColor.GREEN));

                return;
            }

            if (args[0].equalsIgnoreCase("update"))
            {
                if (args.length != 2)
                {
                    throw new WrongUsageException("/llibrary update <modid>");
                }

                for (ModUpdateContainer mod : outdatedMods)
                {
                    if (args[1].equalsIgnoreCase(mod.modid))
                    {
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                        {
                            try
                            {
                                desktop.browse(mod.website.toURI());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                return;
            }

            if (args[0].equalsIgnoreCase("changelog"))
            {
                if (args.length != 3)
                {
                    throw new WrongUsageException("/llibrary changelog <modid> <version>");
                }

                for (int i = 0; i < UpdateHelper.modList.size(); ++i)
                {
                    ModUpdateContainer mod = UpdateHelper.modList.get(i);

                    if (args[1].equalsIgnoreCase(mod.modid))
                    {
                        boolean hasChangelogForVersion = false;

                        try
                        {
                            hasChangelogForVersion = ChangelogHandler.hasModGotChangelogForVersion(mod, args[2]);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (hasChangelogForVersion)
                        {
                            LLibrary.proxy.openChangelogGui(mod, args[2]);
                        }
                        else
                        {
                            ChatHelper.chatTo(sender, new ChatMessage("There is no changelog for mod '" + mod.modid + "' version " + args[2] + "!", EnumChatColor.RED));
                        }
                    }
                }

                return;
            }
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        if (astring.length == 1)
        {
            return getListOfStringsMatchingLastWord(astring, "list", "update", "changelog");
        }
        else
        {
            if (astring[0].equalsIgnoreCase("update") && astring.length == 2)
            {
                return getListOfStringsFromIterableMatchingLastWord(astring, getAllModIDs(VersionHandler.getOutdatedMods()));
            }
            if (astring[0].equalsIgnoreCase("changelog") && astring.length == 2)
            {
                return getListOfStringsFromIterableMatchingLastWord(astring, getAllModIDs(UpdateHelper.modList));
            }
            if (astring[0].equalsIgnoreCase("changelog") && astring.length == 3)
            {
                return getListOfStringsFromIterableMatchingLastWord(astring, getAllModChangelogs(UpdateHelper.getModContainerById(astring[1])));
            }
        }
        return null;
    }

    protected List getAllModIDs(List list)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (Object aCollection : list)
        {
            ModUpdateContainer mod = (ModUpdateContainer) aCollection;
            arraylist.add(mod.modid);
        }

        return arraylist;
    }

    protected List getAllModChangelogs(ModUpdateContainer mod)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (String string : mod.updateFile)
        {
            String s = mod.modid + "Log|";

            if (string.startsWith(s))
            {
                String s1 = string.substring(s.length()).split(":")[0];
                arraylist.add(s1);
            }
        }

        return arraylist;
    }
}