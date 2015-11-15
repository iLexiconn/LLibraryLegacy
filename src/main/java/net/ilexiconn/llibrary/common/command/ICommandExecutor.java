package net.ilexiconn.llibrary.common.command;

import net.minecraft.command.ICommandSender;

/**
 * Command executor. The {@link ICommandExecutor#execute(ICommandSender, CommandArguments)} method will be called
 * when a player executes the command and filled in all the required arguments.
 *
 * @see CommandBuilder
 * @author iLexiconn
 * @since 0.5.3
 */
public interface ICommandExecutor {

    /**
     * Execute the command. This method will be called when a player executes the command and filled in all the required arguments.
     *
     * @param sender The command sender. For example, this can be a EntityPlayer instance, or the console.
     * @param arguments The argument container class.
     */
    void execute(ICommandSender sender, CommandArguments arguments);
}
