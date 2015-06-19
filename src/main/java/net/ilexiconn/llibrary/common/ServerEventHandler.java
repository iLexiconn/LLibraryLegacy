package net.ilexiconn.llibrary.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart;
import net.ilexiconn.llibrary.common.save.SaveHelper;
import net.ilexiconn.llibrary.common.update.UpdateCheckerThread;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;

public class ServerEventHandler
{
    private boolean checkedForUpdates;

    @SubscribeEvent
    public void entityTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof IEntityMultiPart)
        {
            for (EntityPart part : ((IEntityMultiPart) event.entityLiving).getParts())
                part.onUpdate();
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.world.isRemote)
        {
            if (event.entity instanceof EntityPlayer)
            {
                if (!checkedForUpdates)
                {
                    new UpdateCheckerThread().start();

                    checkedForUpdates = true;
                }
            }
        }

        if (EntityHelper.hasEntityBeenRemoved(event.entity.getClass()))
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!event.world.isRemote)
        {
            SaveHelper.load(event.world.getSaveHandler(), event.world);
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            SaveHelper.save(event.world.getSaveHandler(), event.world);
        }
    }
}
