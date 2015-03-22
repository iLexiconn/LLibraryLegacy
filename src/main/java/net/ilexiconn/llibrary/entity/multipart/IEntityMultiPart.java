package net.ilexiconn.llibrary.entity.multipart;

/**
 * Interface for entities using the EntityPart class.
 * 
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.entity.multipart.EntityPart
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
