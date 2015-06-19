package net.ilexiconn.llibrary.common.save;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author DanielHuisman
 * @since 0.2.0
 */
public interface ISaveHandler
{

	public static enum SaveType
	{
		OBJECT,
		NBT
	}
	
	/**
	 * @return An array of file names
	 */
	public String[] getSaveFiles();
	
	/**
	 * @param fileName The name of the save file
	 * @return The SaveType for this save file
	 */
	public SaveType getSaveFileType(String fileName);
	
	/**
	 * @param fileName The name of the save file being loaded
	 * @param file The save file being loaded
	 * @param in The data stream to read from
	 */
	public void load(String fileName, File file, ObjectInputStream in);
	
	/**
	 * @param fileName The name of the save file being saved
	 * @param file The save file being saved
	 * @param out The data stream to write to
	 */
	public void save(String fileName, File file, ObjectOutputStream out);
	
	/**
	 * @param fileName The name of the save file being loaded
	 * @param file The save file being loaded
	 * @param nbt The NBT tag to read from
	 */
	public void loadNBT(String fileName, File file, NBTTagCompound nbt);
	
	/**
	 * @param fileName The name of the save file being saved
	 * @param file The save file being saved
	 * @param nbt The NBT tag to write to
	 */
	public void saveNBT(String fileName, File file, NBTTagCompound nbt);
}
