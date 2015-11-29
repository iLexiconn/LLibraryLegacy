package net.ilexiconn.llibrary.common.command.builder;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Command builder. Use this to easily create and register server commands.
 *
 * @author iLexiconn
 * @since 0.5.3
 */
public class CommandBuilder {
    private Command command = new Command();

    private CommandBuilder(String commandName, String commandUsage) {
        command.commandName = commandName;
        command.commandUsage = commandUsage;
    }

    /**
     * Initialize a new builder instance. If you want your command to have multiple names,
     * use {@link CommandBuilder#withAlias(String)}.
     *
     * @param commandName  The primary name of this command.
     * @param commandUsage The usage of this command. If null, an auto-generated usage message will be generated.
     * @return A new instance of a {@link CommandBuilder}
     */
    public static CommandBuilder create(String commandName, String commandUsage) {
        return new CommandBuilder(commandName, commandUsage);
    }

    /**
     * Initialize a new builder instance. If you keep the field 'commandUsage' null, an auto-generated usage message
     * will be generated. If you want your command to have multiple names, use {@link CommandBuilder#withAlias(String)}.
     *
     * @param commandName The primary name of this command.
     * @return A new instance of a {@link CommandBuilder}.
     */
    public static CommandBuilder create(String commandName) {
        return new CommandBuilder(commandName, null);
    }

    /**
     * Add an alias to the command.
     *
     * @param alias The alias to be added.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withAlias(String alias) {
        command.commandAliases.add(alias);
        return this;
    }

    /**
     * Set the command usage. If null, an auto-generated usage message will be generated.
     *
     * @param usage The usage of this command.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withUsage(String usage) {
        command.commandUsage = usage;
        return this;
    }

    /**
     * Set required permission level of his command. The default level is '4'.
     *
     * @param permissionLevel The required permission level of this command.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withRequiredPermissionLevel(int permissionLevel) {
        command.requiredPermissionLevel = permissionLevel;
        return this;
    }

    /**
     * Add a REQUIRED argument to the argument. There come always before the optional arguments. If the user didn't
     * fill in the argument, an error will be shown. Defaults the type to STRING.
     *
     * @param argument The required argument.
     * @return The updated {@link CommandBuilder} instance.
     */
    @Deprecated
    public CommandBuilder withRequiredArgument(String argument) {
        return withRequiredArgument(argument, ArgumentType.STRING);
    }

    /**
     * Add a REQUIRED argument to the argument. There come always before the optional arguments. If the user didn't
     * fill in the argument, an error will be shown.
     *
     * @param argument The required argument.
     * @param type     The argument type.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withRequiredArgument(String argument, ArgumentType<?> type) {
        command.requiredArguments.put(argument, type);
        return this;
    }

    /**
     * Add a OPTIONAL argument to the argument. There come always after the required arguments. If the user didn't
     * fill in the argument, the command will be executed anyway. Defaults the type to STRING.
     *
     * @param argument The optional argument.
     * @return The updated {@link CommandBuilder} instance.
     */
    @Deprecated
    public CommandBuilder withOptionalArgument(String argument) {
        return withOptionalArgument(argument, ArgumentType.STRING);
    }

    /**
     * Add a OPTIONAL argument to the argument. There come always after the required arguments. If the user didn't
     * fill in the argument, the command will be executed anyway.
     *
     * @param argument The optional argument.
     * @param type     The argument type.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder withOptionalArgument(String argument, ArgumentType<?> type) {
        command.optionalArguments.put(argument, type);
        return this;
    }

    /**
     * Register this command to the server.
     *
     * @param event    The event to register the command to.
     * @param executor The {@link ICommandExecutor} instance. The
     *                 {@link ICommandExecutor#execute(ICommandSender, CommandArguments)} method will be called when a
     *                 player executes the command and filled in all the required arguments.
     * @return The updated {@link CommandBuilder} instance.
     */
    public CommandBuilder register(FMLServerStartingEvent event, ICommandExecutor executor) {
        command.commandExecutor = executor;
        event.registerServerCommand(command);
        return this;
    }
}
