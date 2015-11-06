package net.ilexiconn.llibrary.common.event;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderPlayerModelEvent extends Event {
    public final ModelBiped model;
    public final float limbSwing;
    public final float limbSwingAmount;
    public final float rotationFloat;
    public final float rotationYaw;
    public final float rotationPitch;
    public final float partialTicks;
    public final Entity entity;

    public RenderPlayerModelEvent(ModelBiped m, float ls, float lsa, float rf, float ry, float rp, float pt, Entity e) {
        model = m;
        limbSwing = ls;
        limbSwingAmount = lsa;
        rotationFloat = rf;
        rotationYaw = ry;
        rotationPitch = rp;
        partialTicks = pt;
        entity = e;
    }

    @Cancelable
    public static class Pre extends RenderPlayerModelEvent {
        public Pre(ModelBiped m, float ls, float lsa, float rf, float ry, float rp, float pt, Entity e) {
            super(m, ls, lsa, rf, ry, rp, pt, e);
        }
    }

    public static class Post extends RenderPlayerModelEvent {
        public Post(ModelBiped m, float ls, float lsa, float rf, float ry, float rp, float pt, Entity e) {
            super(m, ls, lsa, rf, ry, rp, pt, e);
        }
    }
}
