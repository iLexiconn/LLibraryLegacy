package net.ilexiconn.llibrary.common.command.builder;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.map.ListHashMap;
import net.minecraft.command.*;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.Map;

public class Command extends CommandBase {
    String commandName;
    String commandUsage;
    String generatedUsage;
    int requiredPermissionLevel = 4;
    ICommandExecutor commandExecutor;

    List<String> commandAliases = Lists.newArrayList();
    ListHashMap<String, ArgumentType<?>> requiredArguments = new ListHashMap<String, ArgumentType<?>>();
    ListHashMap<String, ArgumentType<?>> optionalArguments = new ListHashMap<String, ArgumentType<?>>();

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        if (commandUsage == null) {
            if (generatedUsage == null) {
                StringBuilder builder = new StringBuilder();
                builder.append("/");
                builder.append(getCommandName());
                for (String requiredArgument : requiredArguments.keySet()) {
                    builder.append(" ");
                    builder.append("<");
                    builder.append(requiredArgument);
                    builder.append(">");
                }
                for (String optionalArgument : optionalArguments.keySet()) {
                    builder.append(" ");
                    builder.append("[");
                    builder.append(optionalArgument);
                    builder.append("]");
                }
                generatedUsage = builder.toString();
                return generatedUsage;
            } else {
                return generatedUsage;
            }
        } else {
            return commandUsage;
        }
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < requiredArguments.size()) {
            throw new WrongUsageException(getCommandUsage(sender));
        } else if (args.length > requiredArguments.size() + optionalArguments.size()) {
            throw new WrongUsageException(getCommandUsage(sender));
        } else {
            List<Argument> arguments = Lists.newArrayList();
            for (int i = 0; i < args.length; i++) {
                if (i < requiredArguments.size()) {
                    Map.Entry<String, ArgumentType<?>> entry = requiredArguments.getEntry(i);
                    arguments.add(new Argument(entry.getKey(), args[i], entry.getValue()));
                } else {
                    Map.Entry<String, ArgumentType<?>> entry = optionalArguments.getEntry(i - requiredArguments.size());
                    arguments.add(new Argument(entry.getKey(), args[i], entry.getValue()));
                }
            }
            commandExecutor.execute(sender, new CommandArguments(arguments, sender));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (requiredArguments.size() >= args.length - 1) {
            if (requiredArguments.getValue(args.length - 1) == ArgumentType.PLAYER) {
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            } else if (requiredArguments.getValue(args.length - 1) == ArgumentType.STACK) {
                return getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys());
            }
        }
        if (optionalArguments.size() >= args.length - requiredArguments.size()) {
            if (optionalArguments.getValue(args.length - 1 - requiredArguments.size()) == ArgumentType.PLAYER) {
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            } else if (optionalArguments.getValue(args.length - 1 - requiredArguments.size()) == ArgumentType.STACK) {
                return getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys());
            }
        }
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return commandAliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return requiredPermissionLevel;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (requiredArguments.size() >= index) {
            if (requiredArguments.getValue(index) == ArgumentType.PLAYER) {
                return true;
            }
        }
        if (optionalArguments.size() < index && optionalArguments.size() + requiredArguments.size() >= index) {
            if (optionalArguments.getValue(index - requiredArguments.size()) == ArgumentType.PLAYER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(ICommand other) {
        return 0;
    }
}
