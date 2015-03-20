package net.ilexiconn.llibrary.survivaltab;

import com.google.common.collect.Lists;

import java.util.List;

public class TabRegistry
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
    
    public static SurvivalTab getSurvivalTab(int id)
    {
    	return survivalTabs.get(id + 1); //TODO -1?
    }
}
