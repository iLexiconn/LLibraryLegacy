package net.ilexiconn.llibrary.command;

import net.ilexiconn.llibrary.LLibHelper;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.update.ChangelogHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.ilexiconn.llibrary.update.UpdateHelper;
import net.ilexiconn.llibrary.update.VersionHandler;
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
	
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		String title = "[LLibHelper]" + EnumChatFormatting.YELLOW + " ";
		List<ModUpdateContainer> outdatedMods = VersionHandler.getOutdatedMods();
        
		if (astring.length >= 1)
        {
            if (astring[0].equalsIgnoreCase("list"))
            {
                if (astring.length > 1)
                {
                	throw new WrongUsageException("/llibrary list");
                }
                
                LLibHelper.chat(icommandsender, EnumChatFormatting.DARK_GREEN + "--- Showing a list of outdated mods ---");

                for (ModUpdateContainer mod : outdatedMods)
                {
                    LLibHelper.chat(icommandsender, EnumChatFormatting.BLUE + "(" + mod.modid + ") " + EnumChatFormatting.WHITE + mod.name + " version " + mod.version + " - Latest version: " + VersionHandler.getVersion(mod));
                }
    			
    			LLibHelper.chat(icommandsender, EnumChatFormatting.GREEN + "Use " + EnumChatFormatting.YELLOW + "/llibrary update <modid>" + EnumChatFormatting.GREEN + " to update the desired mod, " + EnumChatFormatting.RED + "or");
    			LLibHelper.chat(icommandsender, EnumChatFormatting.GREEN + "Use " + EnumChatFormatting.YELLOW + "/llibrary changelog <modid> <version>" + EnumChatFormatting.GREEN + " to see its version changelog.");
    			return;
            }
            
            if (astring[0].equalsIgnoreCase("update"))
            {
                if (astring.length != 2)
                {
                	throw new WrongUsageException("/llibrary update <modid>");
                }

                for (ModUpdateContainer mod : outdatedMods)
                {
                    if (astring[1].equalsIgnoreCase(mod.modid))
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
            
            if (astring[0].equalsIgnoreCase("changelog"))
            {
                if (astring.length != 3)
                {
                	throw new WrongUsageException("/llibrary changelog <modid> <version>");
                }
                
            	for (int i = 0; i < UpdateHelper.modList.size(); ++i)
    			{
    				ModUpdateContainer mod = UpdateHelper.modList.get(i);
    				
    				if (astring[1].equalsIgnoreCase(mod.modid))
    				{
        				boolean flag = false;
        				try {flag = ChangelogHandler.hasModGotChangelogForVersion(mod, astring[2]);} catch (Exception e) {e.printStackTrace();}
        				
                        if (flag)
                        {
                        	LLibHelper.chat(icommandsender, "Opening changelog...");
            				LLibrary.proxy.openChangelogGui(mod, astring[2]);
//                        	LLibHelper.chat(icommandsender, EnumChatFormatting.GREEN + "Use " + EnumChatFormatting.YELLOW + "/llibrary update <modid>" + EnumChatFormatting.GREEN + " to update the desired mod.");
                        }
                        else
                        {
                        	LLibHelper.chat(icommandsender, EnumChatFormatting.RED + "There is no changelog for mod '" + mod.modid + "' version " + astring[2] + "!");
                        }
    				}
    			}
    			
    			return;
            }
        }
        throw new WrongUsageException(getCommandUsage(icommandsender));
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
            	return getListOfStringsFromIterableMatchingLastWord(astring, this.getAllModIDs(VersionHandler.getOutdatedMods()));
            }
            if (astring[0].equalsIgnoreCase("changelog") && astring.length == 2)
            {
                return getListOfStringsFromIterableMatchingLastWord(astring, this.getAllModIDs(UpdateHelper.modList));
            }
        }
        return null;
    }
    
    protected List getAllModIDs(List list)
    {
        ArrayList arraylist = new ArrayList();

        for (Object aCollection : list)
        {
            ModUpdateContainer mod = (ModUpdateContainer) aCollection;
            arraylist.add(mod.modid);
        }

        return arraylist;
    }
}