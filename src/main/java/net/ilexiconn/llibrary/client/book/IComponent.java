package net.ilexiconn.llibrary.client.book;

import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public interface IComponent {
    char getID();

    String init(String string, String arg, String group, BookWiki bookWiki);

    void render(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY);

    void renderTooltip(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY);
}
