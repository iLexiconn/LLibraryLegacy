package net.ilexiconn.llibrary.survivaltab;

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
        survivalTabs.add(new SurvivalTab(survivalTabs.size(), survivalTab));
    }

    public static List<SurvivalTab> getSurvivalTabs()
    {
        return survivalTabs;
    }
}
