package net.ilexiconn.llibrary.client.component.recipe;

import net.ilexiconn.llibrary.client.book.IRecipe;
import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.common.book.BookWikiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CraftingRecipe extends Gui implements IRecipe {
    @Override
    public String getType() {
        return "crafting";
    }

    @Override
    public void render(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(gui.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        x += 14;
        y += mc.fontRendererObj.FONT_HEIGHT;
        drawModalRectWithCustomSizedTexture(x + 64, y + 14, 292, 101, 26, 26, 512.0F, 512.0F);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().zLevel = 100.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(recipe.getResult(), x + 69, y + 19);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, recipe.getResult(), x + 69, y + 19, null);
        mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        mc.getTextureManager().bindTexture(gui.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        drawModalRectWithCustomSizedTexture(x, y, 292, 23, 54, 54, 512.0F, 512.0F);
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = recipe.getRecipe()[i];
            if (stack != null) {
                int newX = (x + (i % 3) * 18) + 1;
                int newY = (y + (i / 3) * 18) + 1;
                mc.getRenderItem().zLevel = 100.0F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, newX, newY);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, newX, newY, null);
                mc.getRenderItem().zLevel = 0.0F;
            }
        }
        RenderHelper.disableStandardItemLighting();
        if (recipe.isShapeless()) {
            mc.getTextureManager().bindTexture(gui.getTexture());
            drawModalRectWithCustomSizedTexture(x + 54, y, 318, 101, 7, 7, 512.0F, 512.0F);
        }
    }

    @Override
    public void renderTooltip(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        x += 14;
        y += mc.fontRendererObj.FONT_HEIGHT;
        ItemStack currentItem = null;
        if (mouseX >= x + 64 && mouseY >= y + 14 && mouseX < x + 64 + 26 && mouseY < y + 14 + 26) {
            currentItem = recipe.getResult();
        }
        for (int i = 0; i < 9; i++) {
            ItemStack stack = recipe.getRecipe()[i];
            if (stack != null) {
                int newX = (x + (i % 3) * 18) + 1;
                int newY = (y + (i / 3) * 18) + 1;
                if (mouseX + 1 >= newX && mouseY + 1 >= newY && mouseX + 1 < newX + 18 && mouseY + 1 < newY + 18) {
                    currentItem = stack;
                }
            }
        }
        if (recipe.isShapeless()) {
            if (mouseX >= x + 54 && mouseY >= y && mouseX < x + 54 + 7 && mouseY < y + 7) {
                gui.drawHoveringText(StatCollector.translateToLocal("component.recipe.shapeless"), mouseX, mouseY, mc.fontRendererObj);
            }
        }
        if (currentItem != null) {
            gui.renderToolTip(mc, currentItem, mouseX, mouseY);
        }
    }

    @Override
    public int getHeight() {
        return 7;
    }
}
