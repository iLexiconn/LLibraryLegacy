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

    /** Use list.set(index, tag) in 1.8 */
    @Deprecated
    public static void set(NBTTagList list, int index, NBTBase tag) {
        if (tag.getId() != Constants.NBT.TAG_END && index >= 0 && index < list.tagCount()) {
            if (list.func_150303_d() == Constants.NBT.TAG_END) {
                list.tagType = tag.getId();
            }

            if (list.func_150303_d() == tag.getId()) {
                list.tagList.set(index, tag);
            }
        }
    }

    public static void set(NBTTagList list, int index, Object value) {
        set(list, index, NbtHelper.getNbtTagFromValue(value));
    }

    /** Use list.get(index) in 1.8 */
    @Deprecated
    public static NBTBase get(NBTTagList list, int index) {
        return index >= 0 && index < list.tagCount() ? (NBTBase) list.tagList.get(index) : new NBTTagEnd();
    }

    public static byte getByteAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_BYTE ? ((NBTTagByte) tag).func_150290_f() : 0;
        } else {
            return (byte) 0;
        }
    }

    public static short getShortAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_SHORT ? ((NBTTagShort) tag).func_150289_e() : 0;
        } else {
            return (short) 0;
        }
    }

    public static int getIntegerAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_INT ? ((NBTTagInt) tag).func_150287_d() : 0;
        } else {
            return 0;
        }
    }

    public static long getLongAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_LONG ? ((NBTTagLong) tag).func_150291_c() : 0L;
        } else {
            return 0L;
        }
    }

    public static byte[] getByteArrayAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_BYTE_ARRAY ? ((NBTTagByteArray) tag).func_150292_c() : new byte[0];
        } else {
            return new byte[0];
        }
    }

    public static NBTTagList getTagListAt(NBTTagList list, int index) {
        if (index >= 0 && index < list.tagCount()) {
            NBTBase tag = get(list, index);
            return tag.getId() == Constants.NBT.TAG_LIST ? (NBTTagList) tag : new NBTTagList();
        } else {
            return new NBTTagList();
        }
    }

    public static NBTTagList getTagListAt(NBTTagList list, int index, int type) {
        NBTTagList tagList = getTagListAt(list, index);
        return tagList.tagCount() > 0 && tagList.func_150303_d() != type ? new NBTTagList() : tagList;
    }

    public static boolean getBooleanAt(NBTTagList list, int index) {
        return getByteAt(list, index) != 0;
    }
}
