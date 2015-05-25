package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for rendering extra models to existing models.
 *
 * @author iLexiconn, Gegy1000
 */
@SideOnly(Side.CLIENT)
public interface IExtension
{
    /**
     * Initialize the custom model(s).
     *
     * @param model the parent model
     */
    void init(ModelBase model);
}
