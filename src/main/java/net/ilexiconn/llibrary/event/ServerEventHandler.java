package net.ilexiconn.llibrary.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ilexiconn.llibrary.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.entity.multipart.IEntityMultiPart;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ServerEventHandler
{
    @SubscribeEvent
    public void entityTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof IEntityMultiPart)
        {
            for (EntityPart part : ((IEntityMultiPart) event.entityLiving).getParts()) part.onUpdate();
        }
    }
}
