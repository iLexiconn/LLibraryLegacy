package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.config.ConfigHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLLibraryConfig extends GuiConfig
{
    public GuiLLibraryConfig(GuiScreen parent)
    {
        super(parent, getConfigElements(), "llibrary", "llibrary", true, false, "LLibrary Config");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = Lists.newArrayList();
        list.add(new DummyCategoryElement("General", "general", LLibraryGeneral.class));
        return list;
    }

    public static class LLibraryGeneral extends GuiConfigEntries.CategoryEntry
    {
        public LLibraryGeneral(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        public GuiScreen buildChildScreen()
        {
            return new GuiConfig(owningScreen, new ConfigElement(ConfigHelper.getConfigContainer("llibrary").getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "llibrary", false, false, "LLibrary Config", "General");
        }
    }
}
