package net.ilexiconn.llibrary.client.render;

import net.minecraft.client.model.ModelBase;

/**
 * Interface for rendering extra models to existing models.
 *
 * @author iLexiconn
 * @author Gegy1000
 * @since 0.2.0
 */
@Deprecated
public interface IExtension {
    /**
     * Initialize the custom model(s).
     *
     * @param model the parent model
     * @since 0.2.0
     */
    void init(ModelBase model);
}
