package net.ilexiconn.llibrary.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
