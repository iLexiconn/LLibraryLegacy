package net.ilexiconn.llibrary.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ilexiconn.llibrary.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.entity.multipart.IEntityMultiPart;
import net.ilexiconn.llibrary.update.UpdateCheckerThread;
import net.ilexiconn.llibrary.update.VersionHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ServerEventHandler
{
	private boolean checkedForUpdates;
	
    @SubscribeEvent
    public void entityTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof IEntityMultiPart)
        {
            for (EntityPart part : ((IEntityMultiPart) event.entityLiving).getParts()) part.onUpdate();
        }
    }
    
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
    	if(event.world.isRemote)
    	{
    		if(!checkedForUpdates)
    		{
    			new UpdateCheckerThread().start();
    			
    			checkedForUpdates = true;
    		}
    	}
    }
}
