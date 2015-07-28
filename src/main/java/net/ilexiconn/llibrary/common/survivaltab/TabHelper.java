package net.ilexiconn.llibrary.common.survivaltab;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;

import java.util.List;

/**
 * @author iLexiconn
 * @since 0.2.0
 */
public class TabHelper
{
    private static List<SurvivalTab> survivalTabs = Lists.newArrayList();

    static
    {
        registerSurvivalTab(new SurvivalTabInventory());
    }

    public static void registerSurvivalTab(ISurvivalTab survivalTab)
    {
        if (survivalTabs.size() > 11)
        {
            LLibrary.logger.error("Can't register more than 11 survival tabs! Not registering " + survivalTab.getClass().getCanonicalName());
            return;
        }
        survivalTabs.add(new SurvivalTab(survivalTabs.size(), survivalTab));
    }

    public static List<SurvivalTab> getSurvivalTabs()
    {
        return survivalTabs;
    }
}