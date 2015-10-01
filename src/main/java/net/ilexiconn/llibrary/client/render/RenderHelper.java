package net.ilexiconn.llibrary.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

/**
 * Render helper class for basic render operations and the IModelExtension.
 * 
 * @author iLexiconn
 * @author Gegy1000
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class RenderHelper
{
    private static Map<Class<? extends ModelBase>, List<IExtension>> modelExtensions = Maps.newHashMap();

    /**
     * Registers the given {@link net.ilexiconn.llibrary.client.render.IModelExtension}.
     * 
     * @see net.ilexiconn.llibrary.client.render.IModelExtension
     * @since 0.1.0
     */
    public static void registerModelExtension(IModelExtension modelExtension)
    {
        registerModelExtension(ModelBiped.class, modelExtension);
    }

    /**
     * Registers the given {@link net.ilexiconn.llibrary.client.render.IModelExtension} to a specific model.
     * 
     * @see net.ilexiconn.llibrary.client.render.IModelExtension
     * @since 0.1.0
     */
    private static void registerModelExtension(Class<? extends ModelBase> modelClazz, IExtension modelExtension)
    {
        List<IExtension> extensionsForModel = modelExtensions.get(modelClazz);

        if (extensionsForModel == null)
        {
            extensionsForModel = Lists.newArrayList();
        }

        extensionsForModel.add(modelExtension);

        modelExtensions.put(modelClazz, extensionsForModel);
    }

    /**
     * @return a list of {@link net.ilexiconn.llibrary.client.render.IModelExtension} for the given model class.
     * @see net.ilexiconn.llibrary.client.render.IModelExtension
     * @since 0.1.0
     */
    public static List<IExtension> getModelExtensionsFor(Class<? extends ModelBase> clazz)
    {
        return modelExtensions.get(clazz);
    }

    /**
     * @since 0.2.0
     */
    public static void setColorFromInt(int color)
    {
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        GL11.glColor4f(r, g, b, 1f);
    }
}
