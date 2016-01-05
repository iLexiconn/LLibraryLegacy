package net.ilexiconn.llibrary.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

/**
 * @author iLexiconn
 * @since 0.4.0
 */
@SideOnly(Side.CLIENT)
public class GuiLLibraryConfigFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft mc) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiLLibraryConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
