package net.ilexiconn.llibrary.common.nbt;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

/**
 * @author iLexiconn
 * @since 0.1.0
 */
public class NbtHelper {
    /**
     * @param compound  the tag to save the data to.
     * @param inventory the inventory to save to the compound.
     */
    public static void writeInventoryToNbt(NBTTagCompound compound, IInventory inventory) {
        NBTTagList items = new NBTTagList();
        for (byte slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("slot", slot);
                stack.writeToNBT(item);
                items.appendTag(item);
            }
        }
        compound.setTag("inventory", items);
    }

    /**
     * @param compound  the tag to read the data from.
     * @param inventory the inventory to fill with the data from the compound.
     */
    public static void readInventoryFromNbt(NBTTagCompound compound, IInventory inventory) {
        NBTTagList items = compound.getTagList("inventory", 10);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("slot");
            inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
        }
    }

    public static void writeStackToNbt(NBTTagCompound nbtTag, String name, ItemStack stack) {
        nbtTag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
    }

    public static ItemStack readStackFromNbt(NBTTagCompound nbtTag, String name) {
        return ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag(name));
    }

    /**
     * @since 0.6.1
     */
    public static Object getValueFromNbtTag(NBTBase type) {
        if (type instanceof NBTTagByte) {
            return ((NBTTagByte) type).func_150290_f();
        } else if (type instanceof NBTTagShort) {
            return ((NBTTagShort) type).func_150289_e();
        } else if (type instanceof NBTTagInt) {
            return ((NBTTagInt) type).func_150287_d();
        } else if (type instanceof NBTTagLong) {
            return ((NBTTagLong) type).func_150291_c();
        } else if (type instanceof NBTTagFloat) {
            return ((NBTTagFloat) type).func_150288_h();
        } else if (type instanceof NBTTagDouble) {
            return ((NBTTagDouble) type).func_150286_g();
        } else if (type instanceof NBTTagString) {
            return type.toString();
        } else if (type instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) type).func_150292_c();
        } else if (type instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) type).func_150302_c();
        } else {
            return null;
        }
    }

    /**
     * @since 0.6.1
     */
    public static NBTBase getNbtTagFromValue(Object value) {
        if (value instanceof Boolean) {
            return new NBTTagByte((Boolean) value ? (byte) 1 : 0);
        } else if (value instanceof Byte) {
            return new NBTTagByte((Byte) value);
        } else if (value instanceof Short) {
            return new NBTTagShort((Short) value);
        } else if (value instanceof Integer) {
            return new NBTTagInt((Integer) value);
        } else if (value instanceof Long) {
            return new NBTTagLong((Long) value);
        } else if (value instanceof Float) {
            return new NBTTagFloat((Float) value);
        } else if (value instanceof Double) {
            return new NBTTagDouble((Double) value);
        } else if (value instanceof String) {
            return new NBTTagString((String) value);
        } else if (value instanceof byte[]) {
            return new NBTTagByteArray((byte[]) value);
        } else if (value instanceof int[]) {
            return new NBTTagIntArray((int[]) value);
        } else {
            return null;
        }
    }
}
