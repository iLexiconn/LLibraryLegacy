package net.ilexiconn.llibrary.common.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

public class RenderStuckArrowEvent extends Event
{
    public final EntityLivingBase entity;
    public final RenderPlayer renderPlayer;
    public final float partialTicks;

    public RenderStuckArrowEvent(EntityLivingBase e, RenderPlayer r, float t)
    {
        entity = e;
        renderPlayer = r;
        partialTicks = t;
    }

    @Cancelable
    public static class Pre extends RenderStuckArrowEvent
    {
        public Pre(EntityLivingBase e, RenderPlayer r, float t)
        {
            super(e, r, t);
        }
    }

    public static class Post extends RenderStuckArrowEvent
    {
        public Post(EntityLivingBase e, RenderPlayer r, float t)
        {
            super(e, r, t);
        }
    }
}
