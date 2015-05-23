package net.ilexiconn.llibrary.common.survivaltab;

import com.google.common.collect.Lists;

import java.util.List;

public class TabHelper
{
    private static List<SurvivalTab> survivalTabs = Lists.newArrayList();

    static
    {
        registerSurvivalTab(new SurvivalTabInventory());
        for (int i = 0; i < 16; i++) registerSurvivalTab(new SurvivalTabEnderChest());
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