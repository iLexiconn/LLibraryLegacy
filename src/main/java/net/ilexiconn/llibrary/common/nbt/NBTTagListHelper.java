package net.ilexiconn.llibrary.common.nbt;

import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

/**
 * @author MrIbby
 * @since 0.6.1
 */
public class NBTTagListHelper {
    public static void append(NBTTagList list, Object value) {
        list.appendTag(NbtHelper.getNbtTagFromValue(value));
    }

    public static void set(NBTTagList list, int index, Object value) {
        list.set(index, NbtHelper.getNbtTagFromValue(value));
    }

    public static byte getByteAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_BYTE ? ((NBTTagByte) tag).getByte() : 0;
        } else {
            return (byte) 0;
        }
    }

    public static short getShortAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_SHORT ? ((NBTTagShort) tag).getShort() : 0;
        } else {
            return (short) 0;
        }
    }

    public static int getIntegerAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_INT ? ((NBTTagInt) tag).getInt() : 0;
        } else {
            return 0;
        }
    }

    public static long getLongAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_LONG ? ((NBTTagLong) tag).getLong() : 0L;
        } else {
            return 0L;
        }
    }

    public static byte[] getByteArrayAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_BYTE_ARRAY ? ((NBTTagByteArray) tag).getByteArray() : new byte[0];
        } else {
            return new byte[0];
        }
    }

    public static NBTTagList getTagListAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = list.get(index);
            return tag.getId() == Constants.NBT.TAG_LIST ? (NBTTagList) tag : new NBTTagList();
        } else {
            return new NBTTagList();
        }
    }

    public static NBTTagList getTagListAt(NBTTagList list, int index, int type) {
        NBTTagList tagList = getTagListAt(list, index);
        return tagList.tagCount() > 0 && tagList.getTagType() != type ? new NBTTagList() : tagList;
    }

    public static boolean getBooleanAt(NBTTagList list, int index) {
        return getByteAt(list, index) != 0;
    }
}
