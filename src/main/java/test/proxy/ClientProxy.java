package test.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import test.EntityTest;
import test.render.RenderEntityTest;

public class ClientProxy extends ServerProxy
{
    public void init()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new RenderEntityTest());
    }
}
