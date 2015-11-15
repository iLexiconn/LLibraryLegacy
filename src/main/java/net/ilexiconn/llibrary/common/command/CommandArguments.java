package net.ilexiconn.llibrary.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author iLexiconn
 * @see CommandBuilder
 * @since 0.5.3
 */
public class CommandArguments {
    private List<Argument> arguments;
    private ICommandSender commandSender;

    public CommandArguments(List<Argument> arguments, ICommandSender commandSender) {
        this.arguments = arguments;
        this.commandSender = commandSender;
    }

    public boolean has(String name) {
        for (Argument argument : arguments) {
            if (argument.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public <T> T get(String name) {
        for (Argument argument : arguments) {
            if (argument.getName().equals(name)) {
                return (T) argument.getType().parse(commandSender, argument.getValue());
            }
        }
        return null;
    }

    public ArgumentType type(String name) {
        for (Argument argument : arguments) {
            if (argument.getName().equals(name)) {
                return argument.getType();
            }
        }
        return null;
    }

    public String asString(String name) {
        return get(name);
    }

    public int asInt(String name) {
        return (Integer) get(name);
    }

    public EntityPlayer asPlayer(String name) {
        return (EntityPlayer) get(name);
    }

    public ItemStack asStack(String name) {
        return (ItemStack) get(name);
    }

    public boolean asBoolean(String name) {
        return (Boolean) get(name);
    }
}
