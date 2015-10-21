package net.ilexiconn.llibrary.common.event;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderFirstPersonEvent extends Event
{
    public final EntityPlayer player;
    public final RenderPlayer renderPlayer;
    public final ModelBiped model;

    public RenderFirstPersonEvent(EntityPlayer p, RenderPlayer r, ModelBiped m)
    {
        player = p;
        renderPlayer = r;
        model = m;
    }

    @Cancelable
    public static class Pre extends RenderFirstPersonEvent
    {
        public Pre(EntityPlayer p, RenderPlayer r, ModelBiped m)
        {
            super(p, r, m);
        }
    }

    public static class Post extends RenderFirstPersonEvent
    {
        public Post(EntityPlayer p, RenderPlayer r, ModelBiped m)
        {
            super(p, r, m);
        }
    }
}
