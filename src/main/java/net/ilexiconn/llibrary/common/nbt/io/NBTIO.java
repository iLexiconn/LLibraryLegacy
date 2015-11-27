package net.ilexiconn.llibrary.common.nbt.io;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.nbt.NbtHelper;
import net.minecraft.nbt.NBTTagCompound;

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
                        Object value = NbtHelper.getValueFromNbtTag(tag.getTag(field.getName()));
                        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                            value = (Byte) value != 0;
                        }
                        field.setAccessible(true);
                        field.set(t, value);
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
                tag.setTag(field.getName(), NbtHelper.getNbtTagFromValue(field.get(object)));
            }
        } catch (Exception e) {
            LLibrary.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to save " + type + " to nbt " + tag));
        }
    }

}
