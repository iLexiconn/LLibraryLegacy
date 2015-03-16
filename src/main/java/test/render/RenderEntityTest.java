package test.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.entity.RenderMultiPart;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderEntityTest extends RenderMultiPart
{
    public ResourceLocation texture = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderEntityTest()
    {
        super(new ModelCreeper(), 0f);
    }

    public ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
