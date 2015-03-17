package net.ilexiconn.llibrary.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

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

    public static void removeRecipes(String toDelete)
    {
        ItemStack resultItem = new ItemStack((Item) Item.itemRegistry.getObject(toDelete));
        resultItem.stackSize = 1;
        resultItem.setItemDamage(0);

        for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++)
        {
            IRecipe tmpRecipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);

            ItemStack recipeResult = tmpRecipe.getRecipeOutput();
            if (recipeResult != null)
            {
                recipeResult.stackSize = 1;
                recipeResult.setItemDamage(0);
            }

            if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
            {
                CraftingManager.getInstance().getRecipeList().remove(i--);
            }
        }
    }
}
