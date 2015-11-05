package net.ilexiconn.llibrary.common.nbt.tag;

import net.minecraft.nbt.NBTTagByte;

public class NBTTagBoolean extends NBTTagByte {
    public NBTTagBoolean(boolean value) {
        super(value ? (byte) 1 : 0);
    }
}
