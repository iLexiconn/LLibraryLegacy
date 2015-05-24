package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.model.ModelBase;

/**
 * Interface for rendering extra models to existing models.
 *
 * @author iLexiconn, Gegy1000
 */
public interface IExtension
{
    /**
     * Initialize the custom model(s).
     *
     * @param model the parent model
     */
    void init(ModelBase model);
}
