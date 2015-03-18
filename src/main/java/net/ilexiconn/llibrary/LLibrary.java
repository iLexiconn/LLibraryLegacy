package net.ilexiconn.llibrary;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import net.ilexiconn.llibrary.command.CommandLLibrary;
import net.ilexiconn.llibrary.proxy.ClientProxy;
import net.ilexiconn.llibrary.proxy.ServerProxy;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class LLibrary extends DummyModContainer
{
	public LLibrary()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "llibrary";
		meta.name = "LLibrary";
		meta.version = "${version}";
		meta.credits = "";
		meta.authorList = Arrays.asList("iLexiconn, gegy1000, FiskFille, BobMowzie, Rafa_MV");
		meta.description = "A lightweight library for Minecraft mods.";
		meta.url = "https://ilexiconn.net/";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
		meta.dependencies = new ArrayList<ArtifactVersion>();

		instance = this;
	}

	public static ServerProxy proxy;

	public static LLibrary instance;

	@Override
	public Class<?> getCustomResourcePackClass()
	{
		return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
	}

	@Override
	public File getSource()
	{
		File modfile = LLibraryLoadingPlugin.modFile;

		if (modfile != null)
		{
			return modfile;
		}
		else
		{
			try
			{
				return new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}

			return null;
		}
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;

		if (event.getSide().isClient())
		{
			proxy = new ClientProxy();
		}
		else
		{
			proxy = new ServerProxy();
		}

		proxy.preInit();
	}

	@Subscribe
	public void serverStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandLLibrary());
	}
	
	@Subscribe
	public void initialize(FMLInitializationEvent event)
	{
	}

	@Subscribe
	public void postInitialize(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
}
