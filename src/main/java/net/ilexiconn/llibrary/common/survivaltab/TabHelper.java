package net.ilexiconn.llibrary.common.survivaltab;

/**
 * @deprecated Use {@link SurvivalTab} instead.
 */
public class TabHelper
{
    public static void registerSurvivalTab(ISurvivalTab survivalTab)
    {
        SurvivalTab tab = SurvivalTab.create(survivalTab.getTabName()).setIcon(survivalTab.getTabIcon()).setContainer(survivalTab.getContainerGuiClass());
        if (survivalTab instanceof ICustomSurvivalTabTexture)
        {
            tab.setTexture(((ICustomSurvivalTabTexture) survivalTab).getTabTexture().getResourcePath());
        }
    }
}