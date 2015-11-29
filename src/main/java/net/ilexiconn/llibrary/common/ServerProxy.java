package net.ilexiconn.llibrary.common;

import java.io.File;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ServerProxy {
    public void preInit(File config) {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class, 0, LLibrary.instance);
        ConfigHelper.registerConfigHandler("llibrary", config, new LLibraryConfigHandler());

        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://github.com/iLexiconn/LLibrary/raw/version/versions.json");
    }

    public void postInit() {
        VersionHandler.searchForOutdatedMods();
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public float getPartialTicks() {
        return 0f;
    }
}
