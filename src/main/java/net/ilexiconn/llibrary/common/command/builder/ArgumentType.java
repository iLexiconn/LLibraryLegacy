package net.ilexiconn.llibrary.common.command.builder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public abstract class ArgumentType<T> {
    public static ArgumentType<String> STRING = new ArgumentType<String>() {
        @Override
        public String parse(ICommandSender sender, String argument) throws CommandException {
            return argument;
        }
    };

    public static ArgumentType<Integer> INT = new ArgumentType<Integer>() {
        @Override
        public Integer parse(ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseInt(argument);
        }
    };

    public static ArgumentType<EntityPlayer> PLAYER = new ArgumentType<EntityPlayer>() {
        @Override
        public EntityPlayer parse(ICommandSender sender, String argument) throws CommandException {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(argument);
            if (player == null) {
                throw new PlayerNotFoundException();
            } else {
                return player;
            }
        }
    };

    public static ArgumentType<ItemStack> STACK = new ArgumentType<ItemStack>() {
        @Override
        public ItemStack parse(ICommandSender sender, String argument) throws CommandException {
            return new ItemStack(CommandBase.getItemByText(sender, argument));
        }
    };

    public static ArgumentType<Boolean> BOOLEAN = new ArgumentType<Boolean>() {
        @Override
        public Boolean parse(ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseBoolean(argument);
        }
    };

    public abstract T parse(ICommandSender sender, String argument) throws CommandException;
}
