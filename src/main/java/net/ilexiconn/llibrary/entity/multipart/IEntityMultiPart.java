package net.ilexiconn.llibrary.entity.multipart;

/**
 * Interface for entities using the EntityPart class.
 *
 * @see net.ilexiconn.llibrary.entity.multipart.EntityPart
 *
 * @author iLexiconn
 */
public interface IEntityMultiPart
{
    /**
     * Get the array of parts.
     *
     * @return the array of parts
     */
    public EntityPart[] getParts();
}
