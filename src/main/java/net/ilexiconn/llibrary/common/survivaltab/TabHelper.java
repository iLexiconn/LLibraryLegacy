package net.ilexiconn.llibrary.common.survivaltab;

import net.ilexiconn.llibrary.api.SurvivalTab;

/**
 * @deprecated Use {@link net.ilexiconn.llibrary.api.SurvivalTab} instead.
 */
public class TabHelper
{
    public static void registerSurvivalTab(ISurvivalTab survivalTab)
    {
        SurvivalTab tab = SurvivalTab.create(survivalTab.getTabName()).setIcon(survivalTab.getTabIcon()).setContainer(survivalTab.getContainerGuiClass());
        if (survivalTab instanceof ICustomSurvivalTabTexture)
            tab.setTexture(((ICustomSurvivalTabTexture) survivalTab).getTabTexture().getResourcePath());
    }
}