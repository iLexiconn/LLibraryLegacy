package test.proxy;

import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.ilexiconn.llibrary.client.survivaltab.SurvivalTabHelper;
import net.minecraft.client.model.ModelBiped;
import cpw.mods.fml.client.registry.RenderingRegistry;
import test.EntityTest;
import test.render.RenderEntityTest;
import test.render.TestModelExtension;
import test.tab.SurvivalTabTest;

public class ClientProxy extends ServerProxy
{
    public void init()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new RenderEntityTest());
        RenderHelper.registerModelExtension(ModelBiped.class, new TestModelExtension());
        SurvivalTabHelper.registerSurvivalTab(new SurvivalTabTest());
    }
}
