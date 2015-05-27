package net.ilexiconn.llibrary.common.survivaltab;

/**
 * @see         net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab
 * @author      iLexiconn
 * @since       0.2.0
 */
public class SurvivalTab
{
    private int tabIndex;
    private ISurvivalTab survivalTab;

    public SurvivalTab(int index, ISurvivalTab tab)
    {
        tabIndex = index;
        survivalTab = tab;
    }

    public int getTabColumn()
    {
        if (tabIndex > 11)
            return ((tabIndex - 12) % 10) % 5;
        return tabIndex % 6;
    }

    public boolean isTabInFirstRow()
    {
        if (tabIndex > 11)
            return ((tabIndex - 12) % 10) < 5;
        return tabIndex < 6;
    }

    public int getTabIndex()
    {
        return tabIndex;
    }

    public int getTabPage()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) / 10) + 1;
        }
        return 0;
    }

    /**
     * @return ISurvivalTab instance of this container
     * @see net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab
     */
    public ISurvivalTab getSurvivalTab()
    {
        return survivalTab;
    }
}