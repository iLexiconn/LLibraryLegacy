package net.ilexiconn.llibrary.common.creativetab;

import net.minecraft.creativetab.CreativeTabs;

/**
 * @author      iLexiconn
 * @since       0.1.0
 */
public abstract class CreativeTabSearch extends CreativeTabs
{
    public CreativeTabSearch(String label)
    {
        super(label);
        setBackgroundImageName("item_search.png");
    }

    public boolean hasSearchBar()
    {
        return true;
    }
}
