package net.ilexiconn.llibrary.common.nbt.io;

import com.google.common.base.CaseFormat;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.nbt.tag.NBTTagBoolean;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;

public class NBTIO {
    public static <T> T fromNbt(NBTTagCompound tag, Class<T> type) {
        try {
            T t = type.newInstance();
            if (tag.hasNoTags()) {
                return t;
            } else {
                Field[] fields = type.getFields();
                for (Field field : fields) {
                    if (tag.hasKey(field.getName())) {
                        field.setAccessible(true);
                        field.set(t, getValue(tag.getTag(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName()))));
                    }
                }
                return t;
            }
        } catch (Exception e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to load " + type + " from nbt " + tag));
            return null;
        }
    }

    public static void toNbt(NBTTagCompound tag, Object object) {
        Class<?> type = object.getClass();
        try {
            Field[] fields = type.getFields();
            for (Field field : fields) {
                tag.setTag(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName()), getType(field.get(object)));
            }
        } catch (Exception e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to save " + type + " to nbt " + tag));
        }
    }

    private static Object getValue(NBTBase type) {
        if (type instanceof NBTTagBoolean) {
            return ((NBTTagBoolean) type).func_150290_f() == 1;
        } else if (type instanceof NBTTagByte) {
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
            return type;
        }
    }

    private static NBTBase getType(Object value) {
        if (value instanceof Boolean) {
            return new NBTTagBoolean((Boolean) value);
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
