package net.ilexiconn.llibrary.common;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.ilexiconn.llibrary.common.config.ConfigContainer;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.multipart.EntityPart;
import net.ilexiconn.llibrary.common.entity.multipart.IEntityMultiPart;
import net.ilexiconn.llibrary.common.save.SaveHelper;
import net.ilexiconn.llibrary.common.update.UpdateCheckerThread;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;

import javax.vecmath.Vector2f;
import java.util.Map;

public class ServerEventHandler
{
    private Map<Entity, Vector2f> sizeCache = Maps.newHashMap();
    private boolean checkedForUpdates;

    @SubscribeEvent
    public void onEntityTick(LivingEvent.LivingUpdateEvent event) throws ReflectiveOperationException
    {
        if (event.entityLiving instanceof IEntityMultiPart)
        {
            for (EntityPart part : ((IEntityMultiPart) event.entityLiving).getParts())
            {
                part.onUpdate();
            }
        }

        float scale = EntityHelper.getScale(event.entity);

        if (sizeCache.containsKey(event.entity))
        {
            Vector2f size = sizeCache.get(event.entity);
            EntityHelper.setSize(event.entity, size.x * scale, size.y * scale);
        }
        else
        {
            sizeCache.put(event.entity, new Vector2f(event.entity.width, event.entity.height));
            EntityHelper.setSize(event.entity, event.entity.width * scale, event.entity.height * scale);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        float scale = EntityHelper.getScale(event.player);
        event.player.eyeHeight = (scale - 1) * 1.62f + event.player.getDefaultEyeHeight() * scale - event.player.getDefaultEyeHeight() * (scale - 1);
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event)
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

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (ConfigHelper.hasConfiguration(event.modID))
        {
            ConfigContainer container = ConfigHelper.getConfigContainer(event.modID);
            if (container != null)
            {
                container.getConfigHandler().loadConfig(container.getConfiguration());
                container.getConfiguration().save();
            }
        }
    }
}
