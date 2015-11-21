package net.ilexiconn.llibrary.common.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gegy1000
 */
public class ByteBufHelper
{
    public static void writeObject(ByteBuf buffer, Object object) throws IllegalAccessException
    {
        for (Field field : object.getClass().getDeclaredFields())
        {
            Class<?> type = field.getType();

            writeByType(buffer, object, field, type);
        }
    }

    public static <T> T readObject(ByteBuf buffer, Class<T> clazz) throws InstantiationException, IllegalAccessException
    {
        T object = clazz.newInstance();

        for (Field field : clazz.getDeclaredFields())
        {
            Class<?> type = field.getType();

            field.set(object, readByType(buffer, type));
        }

        return object;
    }

    private static void writeByType(ByteBuf buffer, Object object, Field field, Class<?> type) throws IllegalAccessException
    {
        if (type == Integer.class)
        {
            buffer.writeInt(field.getInt(object));
        }
        else if (type == Double.class)
        {
            buffer.writeDouble(field.getDouble(object));
        }
        else if (type == Float.class)
        {
            buffer.writeFloat(field.getFloat(object));
        }
        else if (type == String.class)
        {
            ByteBufUtils.writeUTF8String(buffer, (String) field.get(object));
        }
        else if (type == Long.class)
        {
            buffer.writeLong((Long) field.get(object));
        }
        else if (type == Short.class)
        {
            buffer.writeShort((Short) field.get(object));
        }
        else if (type == Boolean.class)
        {
            buffer.writeBoolean((Boolean) field.get(object));
        }
        else if (type == Byte.class)
        {
            buffer.writeByte((Byte) field.get(object));
        }
        else if (type.isInstance(List.class))
        {
            List list = (List) field.get(object);

            buffer.writeInt(list.size());

            for (Object obj : list)
            {
                writeObject(buffer, obj);
            }
        }
        else
        {
            writeObject(buffer, field.get(object));
        }
    }

    private static Object readByType(ByteBuf buffer, Class<?> type) throws IllegalAccessException, InstantiationException
    {
        if (type == Integer.class)
        {
            return buffer.readInt();
        }
        else if (type == Double.class)
        {
            return buffer.readDouble();
        }
        else if (type == Float.class)
        {
            return buffer.readFloat();
        }
        else if (type == String.class)
        {
            return ByteBufUtils.readUTF8String(buffer);
        }
        else if (type == Long.class)
        {
            return buffer.readLong();
        }
        else if (type == Short.class)
        {
            return buffer.readShort();
        }
        else if (type == Boolean.class)
        {
            return buffer.readBoolean();
        }
        else if (type == Byte.class)
        {
            return buffer.readByte();
        }
        else if (type.isInstance(List.class)) //TODO we shouldn't replacing this with an array list
        {
            List list = Lists.newArrayList();

            for (int i = 0; i < buffer.readInt(); i++)
            {
                list.add(readObject(buffer, type.getTypeParameters()[0].getGenericDeclaration()));
            }

            return list;
        }
        else
        {
            return readObject(buffer, type);
        }
    }
}
