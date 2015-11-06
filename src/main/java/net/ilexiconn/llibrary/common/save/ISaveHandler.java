package net.ilexiconn.llibrary.common.save;

import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author DanielHuisman
 * @since 0.3.0
 */
public interface ISaveHandler {
    /**
     * @return An array of file names
     */
    String[] getSaveFiles();

    /**
     * @param fileName The name of the save file
     * @return The SaveType for this save file
     */
    SaveType getSaveFileType(String fileName);

    /**
     * @param fileName The name of the save file being loaded
     * @param file     The save file being loaded
     * @param in       The data stream to read from
     */
    void load(String fileName, File file, ObjectInputStream in);

    /**
     * @param fileName The name of the save file being saved
     * @param file     The save file being saved
     * @param out      The data stream to write to
     */
    void save(String fileName, File file, ObjectOutputStream out);

    /**
     * @param fileName The name of the save file being loaded
     * @param file     The save file being loaded
     * @param nbt      The NBT tag to read from
     */
    void loadNBT(String fileName, File file, NBTTagCompound nbt);

    /**
     * @param fileName The name of the save file being saved
     * @param file     The save file being saved
     * @param nbt      The NBT tag to write to
     */
    void saveNBT(String fileName, File file, NBTTagCompound nbt);

    enum SaveType {
        OBJECT,
        NBT
    }
}
