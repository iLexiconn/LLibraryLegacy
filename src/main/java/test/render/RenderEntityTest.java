package test.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.entity.RenderMultiPart;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import test.model.ModelTest;

@SideOnly(Side.CLIENT)
public class RenderEntityTest extends RenderMultiPart
{
    public ResourceLocation texture = new ResourceLocation("textures/leaellynasaura.png");

    public RenderEntityTest()
    {
        super(new ModelTest(), 0f);
    }

    public ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
