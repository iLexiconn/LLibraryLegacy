package net.ilexiconn.llibrary.common.survivaltab;

/**
 * @author iLexiconn
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

    /**
     * @return index % 6
     */
    public int getTabColumn()
    {
        if (tabIndex > 11) return ((tabIndex - 12) % 10) % 5;
        return tabIndex % 6;
    }

    /**
     * @return tabIndex < 6
     */
    public boolean isTabInFirstRow()
    {
        if (tabIndex > 11) return ((tabIndex - 12) % 10) < 5;
        return tabIndex < 6;
    }

    /**
     * @return tabIndex
     */
    public int getTabIndex()
    {
        return tabIndex;
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