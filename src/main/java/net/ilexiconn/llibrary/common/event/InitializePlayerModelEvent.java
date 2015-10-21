package net.ilexiconn.llibrary.common.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.model.ModelBiped;

public class InitializePlayerModelEvent extends Event
{
    public final ModelBiped model;

    public InitializePlayerModelEvent(ModelBiped m)
    {
        model = m;
    }
}
