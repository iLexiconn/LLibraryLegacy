package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

/**
 * @author iLexiconn
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiLLibraryConfig extends GuiConfig
{
    public GuiLLibraryConfig(GuiScreen p)
    {
        super(p, getConfigElements(), "llibrary", false, false, "LLibrary Config");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = Lists.newArrayList();
        list.add(new DummyConfigElement.DummyCategoryElement("General", "General", ConfigLLibraryGeneral.class));
        return list;
    }

    public static class ConfigLLibraryGeneral extends GuiConfigEntries.CategoryEntry
    {
        public ConfigLLibraryGeneral(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        public GuiScreen buildChildScreen()
        {
            return new GuiConfig(this.owningScreen, new ConfigElement(ConfigHelper.getConfigContainer("llibrary").getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "llibrary", false, false, "LLibrary Config", "General");
        }
    }
}
