package net.ilexiconn.llibrary.client.book;

import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.common.book.BookWikiContainer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public interface IRecipe {
    String getType();

    void render(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY);

    void renderTooltip(Minecraft mc, BookWiki bookWiki, BookWikiContainer.Recipe recipe, BookWikiGui gui, int x, int y, int mouseX, int mouseY);

    int getHeight();
}
