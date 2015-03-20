package test.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.model.ModelBiped;
import test.EntityTest;
import test.render.RenderEntityTest;
import test.render.TestModelExtension;

public class ClientProxy extends ServerProxy
{
    public void init()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new RenderEntityTest());
        RenderHelper.registerModelExtension(new TestModelExtension());
    }
}
