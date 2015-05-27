package net.ilexiconn.llibrary.common.content;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayList to handle IContentHandlers
 *
 * @see         net.ilexiconn.llibrary.common.content.IContentHandler
 * @author      iLexiconn
 * @since       0.1.0
 */
public class ContentHandlerList extends ArrayList<IContentHandler>
{
    /**
     * Create a new list with IContentHandlers
     *
     * @see         net.ilexiconn.llibrary.common.content.IContentHandler
     *
     * @param       contentHandlers the list of IContentHandlers
     * @return      a new instance of the list
     * @since       0.1.0
     */
    public static ContentHandlerList createList(IContentHandler... contentHandlers)
    {
        ContentHandlerList list = new ContentHandlerList();
        list.addAll(Arrays.asList(contentHandlers));
        return list;
    }

    /**
     * Initialize all the IContentHandlers in this list.
     *
     * @see         net.ilexiconn.llibrary.common.content.IContentHandler
     * @since       0.1.0
     */
    public void init()
    {
        for (IContentHandler contentHandler : this)
        {
            try
            {
                contentHandler.init();
                contentHandler.gameRegistry();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
