package net.ilexiconn.llibrary.common.nbt;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NbtHelper
{
    public static void writeInventoryToNbt(NBTTagCompound compound, IInventory inventory)
    {
        NBTTagList items = new NBTTagList();
        for (byte slot = 0; slot < inventory.getSizeInventory(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("slot", slot);
                stack.writeToNBT(item);
                items.appendTag(item);
            }
        }
        compound.setTag("inventory", items);
    }

    public static void readInventoryFromNbt(NBTTagCompound compound, IInventory inventory)
    {
        NBTTagList items = compound.getTagList("inventory", 10);
        for (int i = 0; i < items.tagCount(); i++)
        {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("slot");
            inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
        }
    }

    public static void writeStackToNbt(NBTTagCompound nbtTag, String name, ItemStack stack)
    {
        nbtTag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
    }

    public static ItemStack readStackFromNbt(NBTTagCompound nbtTag, String name)
    {
        return ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag(name));
    }
}
