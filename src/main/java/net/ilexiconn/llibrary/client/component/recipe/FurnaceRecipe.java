package net.ilexiconn.llibrary.client.component.recipe;

import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.common.book.BookWikiContainer;
import net.ilexiconn.llibrary.client.book.IRecipe;
import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FurnaceRecipe extends Gui implements IRecipe {
    @Override
    public String getType() {
        return "furnace";
    }

    @Override
    public void render(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(gui.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        drawModalRectWithCustomSizedTexture(x + 26, y, 292, 127, 18, 18, 512.0F, 512.0F);
        drawModalRectWithCustomSizedTexture(x + 26, y + 27, 292, 127, 18, 18, 512.0F, 512.0F);
        drawModalRectWithCustomSizedTexture(x + 72, y + 10, 292, 101, 26, 26, 512.0F, 512.0F);
        drawModalRectWithCustomSizedTexture(x + 49, y + 14, 310, 127, 18, 18, 512.0F, 512.0F);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().zLevel = 100.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(recipe.getResult(), x + 76, y + 15);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, recipe.getResult(), x + 76, y + 15, null);
        ItemStack coal = new ItemStack(Items.coal);
        mc.getRenderItem().renderItemAndEffectIntoGUI(coal, x + 27, y + 28);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, coal, x + 27, y + 28, null);
        mc.getRenderItem().renderItemAndEffectIntoGUI(recipe.getRecipe()[0], x + 27, y + 1);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, recipe.getRecipe()[0], x + 27, y + 1, null);
        mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void renderTooltip(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        ItemStack currentItem = null;
        if (mouseX + 4 >= x + 76 && mouseY + 4 >= y + 15 && mouseX + 4 < x + 76 + 26 && mouseY + 4 < y + 15 + 26) {
            currentItem = recipe.getResult();
        } else if (mouseX + 1 >= x + 27 && mouseY + 1 >= y + 28 && mouseX + 1 < x + 27 + 18 && mouseY + 1 < y + 28 + 18) {
            currentItem = new ItemStack(Items.coal);
        } else if (mouseX + 1 >= x + 27 && mouseY + 1 >= y + 1 && mouseX + 1 < x + 27 + 18 && mouseY + 1 < y + 1 + 18) {
            currentItem = recipe.getRecipe()[0];
        }
        if (currentItem != null) {
            gui.renderToolTip(mc, currentItem, mouseX, mouseY);
        }
    }

    @Override
    public int getHeight() {
        return 5;
    }
}
