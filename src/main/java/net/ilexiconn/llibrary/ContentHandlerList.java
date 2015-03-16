package net.ilexiconn.llibrary;

import java.util.ArrayList;
import java.util.Arrays;

public class ContentHandlerList extends ArrayList<IContentHandler>
{	
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

    public static ContentHandlerList createList(IContentHandler... contentHandlers)
    {
        ContentHandlerList list = new ContentHandlerList();
        list.addAll(Arrays.asList(contentHandlers));
        return list;
    }
}
