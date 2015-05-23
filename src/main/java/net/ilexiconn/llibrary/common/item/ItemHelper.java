package net.ilexiconn.llibrary.common.item;

import net.ilexiconn.llibrary.common.nbt.NbtHelper;
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
    /**
     * Use NbtHelper.readStackFromNbt() instead.
     *
     * @param nbtTag
     * @param name
     * @return
     */
    @Deprecated
    public static ItemStack getStackFromNBT(NBTTagCompound nbtTag, String name)
    {
        return NbtHelper.readStackFromNbt(nbtTag, name);
    }

    /**
     * Use NbtHelper.writeStackToNbt() instead.
     *
     * @param nbtTag
     * @param name
     * @param stack
     * @return
     */
    @Deprecated
    public static void saveStackToNBT(NBTTagCompound nbtTag, String name, ItemStack stack)
    {
        NbtHelper.writeStackToNbt(nbtTag, name, stack);
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
