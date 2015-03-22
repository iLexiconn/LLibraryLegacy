package net.ilexiconn.llibrary.config;

import cpw.mods.fml.client.IModGuiFactory;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class LLibraryConfigFactory implements IModGuiFactory
{
    public void initialize(Minecraft mc)
    {

    }

    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return GuiLLibraryConfig.class;
    }

    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }
}
