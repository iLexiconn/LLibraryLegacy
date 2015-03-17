package net.ilexiconn.llibrary;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayList to handle IContentHandlers
 *
 * @see net.ilexiconn.llibrary.IContentHandler
 * @author iLexiconn
 */
public class ContentHandlerList extends ArrayList<IContentHandler>
{
    /**
     * Initialize all the IContentHandlers in this list.
     *
     * @see net.ilexiconn.llibrary.IContentHandler
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

    /**
     * Create a new list with IContentHandlers
     *
     * @param contentHandlers the list of IContentHandlers
     * @return a new instance of the list
     *
     * @see net.ilexiconn.llibrary.IContentHandler
     */
    public static ContentHandlerList createList(IContentHandler... contentHandlers)
    {
        ContentHandlerList list = new ContentHandlerList();
        list.addAll(Arrays.asList(contentHandlers));
        return list;
    }
}
