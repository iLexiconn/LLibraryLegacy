package net.ilexiconn.llibrary.common.content;

/**
 * Interface to handle the content of a mod, activated by a ContentHandlerList.
 *
 * @author iLexiconn
 * @see ContentHandlerList
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