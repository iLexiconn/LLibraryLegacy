package net.ilexiconn.llibrary.common.content;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Map;

/**
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.content.IContentHandler
 * @see net.ilexiconn.llibrary.common.content.IClientOnlyHandler
 * @since 0.2.0
 */
public class ContentHelper {
    private static Map<InitializationState, IContentHandler> timedHandlers = Maps.newHashMap();

    /**
     * @param contentHandlers the list of {@link net.ilexiconn.llibrary.common.content.IContentHandler} to initialize.
     * @since 0.2.0
     */
    public static void init(IContentHandler... contentHandlers) {
        init(false, contentHandlers);
    }

    /**
     * @param ignoreTimed     ignore the timed state of the content handler.
     * @param contentHandlers the list of {@link net.ilexiconn.llibrary.common.content.IContentHandler} to initialize.
     * @since 0.2.0
     */
    public static void init(boolean ignoreTimed, IContentHandler... contentHandlers) {
        for (IContentHandler contentHandler : contentHandlers) {
            if (!ignoreTimed && contentHandler instanceof ITimedHandler && ((ITimedHandler) contentHandler).getTimedState() != InitializationState.PREINIT) /** Just run PREINIT now */ {
                timedHandlers.put(((ITimedHandler) contentHandler).getTimedState(), contentHandler);
            } else {
                if (contentHandler instanceof IClientOnlyHandler && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    try {
                        contentHandler.init();
                        contentHandler.gameRegistry();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        contentHandler.init();
                        contentHandler.gameRegistry();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Map<InitializationState, IContentHandler> getTimedHandlers() {
        return timedHandlers;
    }
}
