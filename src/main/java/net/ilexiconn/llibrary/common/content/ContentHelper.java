package net.ilexiconn.llibrary.common.content;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.content.IContentHandler
 * @see net.ilexiconn.llibrary.common.content.IClientOnlyHandler
 * @since 0.2.0
 */
public class ContentHelper
{
    /**
     * @param contentHandlers the list of {@link net.ilexiconn.llibrary.common.content.IContentHandler} to initialize.
     * @since 0.2.0
     */
    public static void init(IContentHandler... contentHandlers)
    {
        for (IContentHandler contentHandler : contentHandlers)
        {
            if (contentHandler instanceof IClientOnlyHandler && FMLCommonHandler.instance().getEffectiveSide().isClient())
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
            else
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
}
