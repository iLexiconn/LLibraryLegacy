package net.ilexiconn.llibrary.common.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import java.util.List;
import java.util.Map;

/**
 * Command builder. Use this to easily create and register server commands.
 *
 * @author iLexiconn
 * @since 0.5.3
 */
public class CommandBuilder extends CommandBase {
    private String commandName;
    private String commandUsage;
    private String generatedUsage;
    private int requiredPermissionLevel = 4;
    private ICommandExecutor commandExecutor;

    private List<String> commandAliases = Lists.newArrayList();
    private List<String> requiredArguments = Lists.newArrayList();
    private List<String> optionalArguments = Lists.newArrayList();

    private CommandBuilder(String name, String usage) {
        commandName = name;
        commandUsage = usage;
    }

    /**
     * Initialize a new builder instance. If you want your command to have multiple names,
     * use {@link CommandBuilder#withAlias(String)}.
     *
     * @param name The primary name of this command.
     * @param usage The usage of this command. If null, an auto-generated usage message will be generated.
     * @return A new instance of a {@link CommandBuilder}
     */
    public static CommandBuilder create(String name, String usage) {
        return new CommandBuilder(name, usage);
    }

    /**
     * Initialize a new builder instance. If you keep the field 'commandUsage' null, an auto-generated usage message
     * will be generated. If you want your command to have multiple names, use {@link CommandBuilder#withAlias(String)}.
     *
     * @param name The primary name of this command.
     * @return A new instance of a {@link CommandBuilder}.
     */
    public static CommandBuilder create(String name) {
        return new CommandBuilder(name, null);
    }

    /**
     * Add an alias to the command.
     *
     * @param alias The alias to be added.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withAlias(String alias) {
        commandAliases.add(alias);
        return this;
    }

    /**
     * Set the command usage. If null, an auto-generated usage message will be generated.
     *
     * @param usage The usage of this command.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withUsage(String usage) {
        commandUsage = usage;
        return this;
    }

    /**
     * Set required permission level of his command. The default level is '4'.
     *
     * @param permissionLevel The required permission level of this command.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withRequiredPermissionLevel(int permissionLevel) {
        requiredPermissionLevel = permissionLevel;
        return this;
    }

    /**
     * Add a REQUIRED argument to the argument. There come always before the optional arguments. If the user didn't
     * fill in the argument, an error will be shown.
     *
     * @param argument The required argument.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withRequiredArgument(String argument) {
        requiredArguments.add(argument);
        return this;
    }

    /**
     * Add a OPTIONAL argument to the argument. There come always after the required arguments. If the user didn't
     * fill in the argument, the command will be executed anyway.
     *
     * @param argument The optional argument.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withOptionalArgument(String argument) {
        optionalArguments.add(argument);
        return this;
    }

    /**
     * Register this command to the server.
     *
     * @param event The event to register the command to.
     * @param executor The {@link ICommandExecutor} instance. The
     *                 {@link ICommandExecutor#execute(ICommandSender, CommandArguments)} method will be called when a
     *                 player executes the command and filled in all the required arguments.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder register(FMLServerStartingEvent event, ICommandExecutor executor) {
        commandExecutor = executor;
        event.registerServerCommand(this);
        return this;
    }

    /* =========================================== FOR INTERNAL USE ONLY =========================================== */

    /**
     * For internal use only.
     */
    @Deprecated
    public String getCommandName() {
        return commandName;
    }

    /**
     * For internal use only.
     */
    @Deprecated
    public String getCommandUsage(ICommandSender sender) {
        if (commandUsage == null) {
            if (generatedUsage == null) {
                StringBuilder builder = new StringBuilder();
                builder.append("/");
                builder.append(getCommandName());
                for (String requiredArgument : requiredArguments) {
                    builder.append(" ");
                    builder.append("<");
                    builder.append(requiredArgument);
                    builder.append(">");
                }
                for (String optionalArgument : optionalArguments) {
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

    /**
     * For internal use only.
     */
    @Deprecated
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < requiredArguments.size()) {
            throw new WrongUsageException(getCommandUsage(sender));
        } else if (args.length > requiredArguments.size() + optionalArguments.size()) {
            throw new WrongUsageException(getCommandUsage(sender));
        } else {
            Map<String, String> arguments = Maps.newHashMap();
            arguments.clear();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (i < requiredArguments.size()) {
                    arguments.put(requiredArguments.get(i), arg);
                } else {
                    arguments.put(optionalArguments.get(i - requiredArguments.size()), arg);
                }
            }
            commandExecutor.execute(sender, new CommandArguments(arguments));
        }
    }

    /**
     * For internal use only.
     */
    @Deprecated
    public List getCommandAliases() {
        return commandAliases;
    }

    /**
     * For internal use only.
     */
    @Deprecated
    public int getRequiredPermissionLevel()
    {
        return requiredPermissionLevel;
    }
}
