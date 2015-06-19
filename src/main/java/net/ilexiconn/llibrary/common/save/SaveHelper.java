package net.ilexiconn.llibrary.common.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.ilexiconn.llibrary.common.save.ISaveHandler.SaveType;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;

/**
 * @author DanielHuisman
 * @since 0.2.0
 */
public class SaveHelper
{
	public static ArrayList<ISaveHandler> saveHandlers = new ArrayList<ISaveHandler>();
	
	public static void registerSaveHandler(ISaveHandler handler)
	{
		saveHandlers.add(handler);
	}
	
	public static void unregisterSaveHandler(ISaveHandler handler)
	{
		saveHandlers.remove(handler);
	}

	public static void load(net.minecraft.world.storage.ISaveHandler saveHandler, World world)
	{
		if((world.provider.getDimensionId() == 0))
		{
			for(ISaveHandler handler : saveHandlers)
			{
				String[] saveFileNames = handler.getSaveFiles();
				for(int i = 0; i < saveFileNames.length; i++)
				{
					SaveType saveFileType = handler.getSaveFileType(saveFileNames[i]);
					if(saveFileType == SaveType.OBJECT)
					{
						try {
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							if(file != null)
							{
								try
								{
									loadFile(handler, saveFileNames[i], file);
								} catch(Exception e)
								{
									File fileBackup = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", true);
									if(fileBackup.exists())
									{
										loadFile(handler, saveFileNames[i], fileBackup);
									} else
									{
										file.createNewFile();
										saveFile(handler, saveFileNames[i], file, true);
									}
								}
							}
						} catch(Exception e)
						{
							e.printStackTrace();
						}
					} else if(saveFileType == SaveType.NBT)
					{
						try
						{
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							if(file != null)
							{
								try
								{
									loadFileNBT(handler, saveFileNames[i], file);
								} catch(Exception e)
								{
									e.printStackTrace();
									File fileBackup = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", true);
									if(fileBackup.exists())
									{
										loadFileNBT(handler, saveFileNames[i], fileBackup);
									} else
									{
										file.createNewFile();
										saveFileNBT(handler, saveFileNames[i], file, true);
									}
								}
							}
						} catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static void loadFile(ISaveHandler saveHandler, String fileName, File file) throws Exception
	{
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		GZIPInputStream gzis = new GZIPInputStream(fis);
		ObjectInputStream in = new ObjectInputStream(gzis);

		saveHandler.load(fileName, file, in);

		in.close();
		gzis.close();
		fis.close();
	}

	private static void loadFileNBT(ISaveHandler saveHandler, String fileName, File file) throws Exception
	{
		FileInputStream fis = new FileInputStream(file);
		NBTTagCompound nbt = CompressedStreamTools.readCompressed(fis);
		fis.close();
		saveHandler.loadNBT(fileName, file, nbt);
	}

	public static void save(net.minecraft.world.storage.ISaveHandler saveHandler, World world)
	{
		if((world.provider.getDimensionId() == 0))
		{
			for(ISaveHandler handler : saveHandlers)
			{
				String[] saveFileNames = handler.getSaveFiles();
				for(int i = 0; i < saveFileNames.length; i++)
				{
					SaveType saveFileType = handler.getSaveFileType(saveFileNames[i]);
					if(saveFileType == SaveType.OBJECT)
					{
						try
						{
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							saveFile(handler, saveFileNames[i], file, false);
						} catch(Exception e)
						{
							e.printStackTrace();
						}
					} else if(saveFileType == SaveType.NBT)
					{
						try
						{
							File file = getSaveFile(saveHandler, world, saveFileNames[i] + ".dat", false);
							saveFileNBT(handler, saveFileNames[i], file, false);
						} catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void saveFile(ISaveHandler saveHandler, String fileName, File file, boolean isNewFile) throws Exception
	{
		FileOutputStream fos = new FileOutputStream(file);
		GZIPOutputStream gzos = new GZIPOutputStream(fos);
		ObjectOutputStream out = new ObjectOutputStream(gzos);

		saveHandler.save(fileName, file, out);

		out.flush();
		out.close();
		gzos.close();
		fos.close();
		copyFile(file, new File(new StringBuilder().append(file.getAbsolutePath()).append(".bak").toString()));
	}

	public static void saveFileNBT(ISaveHandler saveHandler, String fileName, File file, boolean isNewFile) throws Exception
	{
		NBTTagCompound nbt = new NBTTagCompound();

		saveHandler.saveNBT(fileName, file, nbt);

		FileOutputStream fos = new FileOutputStream(file);
		CompressedStreamTools.writeCompressed(nbt, fos);
		fos.close();

		copyFile(file, new File(new StringBuilder().append(file.getAbsolutePath()).append(".bak").toString()));
	}

	public static File getSaveFile(net.minecraft.world.storage.ISaveHandler saveHandler, World world, String name, boolean backup)
	{
		File worldDir = new File(saveHandler.getWorldDirectoryName());
		IChunkLoader loader = saveHandler.getChunkLoader(world.provider);
		if((loader instanceof AnvilChunkLoader))
		{
			worldDir = ((AnvilChunkLoader) loader).chunkSaveLocation;
		}
		File file = new File(worldDir, name + (backup ? ".bak" : ""));
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void copyFile(File sourceFile, File destFile)
	{
		FileChannel source = null;
		FileChannel destination = null;
		try
		{
			if(!destFile.exists())
			{
				destFile.createNewFile();
			}
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0L, source.size());
			source.close();
			destination.close();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
