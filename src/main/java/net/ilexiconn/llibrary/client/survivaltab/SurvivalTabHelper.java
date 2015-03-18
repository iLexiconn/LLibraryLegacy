package net.ilexiconn.llibrary.client.survivaltab;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SurvivalTabHelper
{
    private static List<SurvivalTab> survivalTabs = Lists.newArrayList();

    static
    {
        registerVanillaTabs();
    }

    private static void registerVanillaTabs()
    {
        registerSurvivalTab(new SurvivalTabSurvival());
    }

    public static void registerSurvivalTab(SurvivalTab survivalTab)
    {
        survivalTabs.add(survivalTab);
    }

    public static void updateTabValues(int cornerX, int cornerY, Class<?> selectedButton)
    {
        int count = 2;
        for (SurvivalTab survivalTab : survivalTabs)
        {
            survivalTab.id = count;
            survivalTab.xPosition = cornerX + (count - 2) * 28;
            survivalTab.yPosition = cornerY - 28;
            survivalTab.enabled = !survivalTab.getClass().equals(selectedButton);
            count++;
        }
    }

    public static List<SurvivalTab> getSurvivalTabs()
    {
        return survivalTabs;
    }
}
