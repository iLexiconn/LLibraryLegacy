package net.ilexiconn.llibrary.common.content;

/**
 * Interface to handle the content of a mod, activated by a ContentHandlerList.
 *
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.content.ContentHandlerList
 * @since 0.1.0
 */
public interface IContentHandler {
    /**
     * Initialize the items/blocks/entities/etc.
     *
     * @since 0.1.0
     */
    void init();

    /**
     * Register the content to the game.
     *
     * @throws java.lang.Exception
     * @since 0.1.0
     */
    void gameRegistry() throws Exception;
}