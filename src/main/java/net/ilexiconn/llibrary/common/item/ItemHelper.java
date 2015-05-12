package net.ilexiconn.llibrary.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.List;

/**
 * Helper class to save ItemStacks to NBT, and removing item/block recipes.
 *
 * @author iLexiconn
 */
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

    public static void removeRecipe(Block block)
    {
        removeRecipe(Item.getItemFromBlock(block));
    }

    public static void removeRecipe(Item item)
    {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        Iterator<IRecipe> iterator = recipes.iterator();

        while (iterator.hasNext())
        {
            ItemStack stack = iterator.next().getRecipeOutput();
            if (stack != null && stack.getItem() == item) iterator.remove();
        }
    }
}
