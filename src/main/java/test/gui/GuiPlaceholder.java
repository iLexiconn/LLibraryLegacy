package test.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;

@SideOnly(Side.CLIENT)
public class GuiPlaceholder extends InventoryEffectRenderer
{
    public GuiPlaceholder(Container container)
    {
        super(container);
    }

    public void drawGuiContainerBackgroundLayer(float i, int d, int k)
    {

    }
}
