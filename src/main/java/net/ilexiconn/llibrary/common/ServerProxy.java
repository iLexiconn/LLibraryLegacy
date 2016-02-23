package net.ilexiconn.llibrary.common;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;

public class ServerProxy {
    public void preInit(File config) {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class, 0, LLibrary.instance);
        ConfigHelper.registerConfigHandler("llibrary", config, new LLibraryConfigHandler());

        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://pastebin.com/raw/8sEVXE8p");
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

    public void scheduleTask(MessageContext ctx, Runnable runnable) {
        WorldServer worldObj = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        worldObj.addScheduledTask(runnable);
    }
}
