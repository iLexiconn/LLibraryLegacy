package net.ilexiconn.llibrary;

/**
 * Interface to handle the content of a mod, activated by a ContentHandlerList.
 *
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.ContentHandlerList
 */
public interface IContentHandler
{
    /**
     * Initializing the items/blocks/entities/etc
     */
    void init();

    /**
     * Registering the content to the game.
     *
     * @throws Exception
     */
    void gameRegistry() throws Exception;
}