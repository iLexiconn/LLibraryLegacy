package net.ilexiconn.llibrary.common.content;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayList to handle IContentHandlers. Use {@link net.ilexiconn.llibrary.common.content.ContentHelper} instead.
 *
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.content.IContentHandler
 * @since 0.1.0
 */
@Deprecated
public class ContentHandlerList extends ArrayList<IContentHandler>
{
    /**
     * Create a new list with IContentHandlers. Use {@link net.ilexiconn.llibrary.common.content.ContentHelper#init(IContentHandler...)} instead.
     *
     * @param contentHandlers the list of IContentHandlers
     * @return a new instance of the list
     * @see net.ilexiconn.llibrary.common.content.IContentHandler
     * @since 0.1.0
     */
    @Deprecated
    public static ContentHandlerList createList(IContentHandler... contentHandlers)
    {
        ContentHandlerList list = new ContentHandlerList();
        list.addAll(Arrays.asList(contentHandlers));
        return list;
    }

    /**
     * Initialize all the IContentHandlers in this list. Use {@link net.ilexiconn.llibrary.common.content.ContentHelper#init(IContentHandler...)} instead.
     *
     * @see net.ilexiconn.llibrary.common.content.IContentHandler
     * @since 0.1.0
     */
    @Deprecated
    public void init()
    {
        ContentHelper.init(toArray(new DummyContentHandler[size()]));
    }
}
