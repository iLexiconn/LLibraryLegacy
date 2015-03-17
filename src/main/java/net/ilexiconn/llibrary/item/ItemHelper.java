package net.ilexiconn.llibrary.item;

import java.util.Iterator;

import com.sun.org.apache.bcel.internal.generic.NEW;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public class ItemHelper
{
    public static ItemStack getStackFromNBT(NBTTagCompound nbtTag, String name)
    {
        return ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag(name));
    }

    public static void saveStackToNBT(NBTTagCompound nbtTag, String name, ItemStack stack)
    {
        nbtTag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
    }
}
