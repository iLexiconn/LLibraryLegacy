package net.ilexiconn.llibrary.common.event;

import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.common.eventhandler.Event;

public class InitializePlayerModelEvent extends Event
{
    public final ModelBiped model;

    public InitializePlayerModelEvent(ModelBiped m)
    {
        model = m;
    }
}
