package net.ilexiconn.llibrary.common.survivaltab;

import com.google.common.collect.Lists;

import java.util.List;

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
            System.err.println("[LLibrary] Can't register more than 11 survival tabs! Not registering " + survivalTab.getClass().getCanonicalName());
            return;
        }
        survivalTabs.add(new SurvivalTab(survivalTabs.size(), survivalTab));
    }

    public static List<SurvivalTab> getSurvivalTabs()
    {
        return survivalTabs;
    }
}