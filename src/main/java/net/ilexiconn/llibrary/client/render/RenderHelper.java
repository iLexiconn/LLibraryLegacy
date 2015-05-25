package net.ilexiconn.llibrary.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * Render helper class for basic render operations and the IModelExtension.
 *
 * @author iLexiconn & Gegy1000
 */
@SideOnly(Side.CLIENT)
public class RenderHelper
{
    private static Map<Item, String> itemModels = Maps.newHashMap();
    private static Map<Class<? extends ModelBase>, List<IExtension>> modelExtensions = Maps.newHashMap();

    /**
     * Registers the given IModelExtension.
     *
     * @param modelExtension
     */
    public static void registerModelExtension(IModelExtension modelExtension)
    {
        registerModelExtension(ModelBiped.class, modelExtension);
    }

    /**
     * Registers the given IModelExtension to a specific model.
     *
     * @param modelClazz
     * @param modelExtension
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
     * @param clazz
     * @returns a list of ModelExtensions for the given model class.
     */
    public static List<IExtension> getModelExtensionsFor(Class<? extends ModelBase> clazz)
    {
        return modelExtensions.get(clazz);
    }

    public static void registerModel(Block block, String name)
    {
        registerModel(Item.getItemFromBlock(block), name);
    }

    public static void registerModel(Item item, String name)
    {
        itemModels.put(item, name);
    }

    public static Map<Item, String> getItemModels()
    {
        return itemModels;
    }
}
