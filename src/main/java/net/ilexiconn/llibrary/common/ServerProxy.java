package net.ilexiconn.llibrary.common;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.update.UpdateHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class ServerProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);

        try
        {
            UpdateHelper.registerUpdateChecker(LLibrary.instance, "http://pastebin.com/raw.php?i=TGiS6kuk");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void postInit()
    {

    }

    public void openChangelogGui(JsonModUpdate mod, String version)
    {

    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }
}
