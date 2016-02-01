package net.ilexiconn.llibrary.common.book;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.common.item.BookWikiItem;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.Reader;
import java.util.List;

/**
 * @author iLexiconn
 */
public class BookWiki {
    public static LoggerHelper logger = new LoggerHelper("BookWiki");

    private Object modInstance;
    private Mod mod;
    private BookWikiContainer container;
    private BookWikiItem item;

    public static BookWiki create(Object mod, Reader reader) {
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            logger.error("Please specify the mod instance!");
            return null;
        }
        BookWiki bookWiki = new BookWiki();
        bookWiki.modInstance = mod;
        bookWiki.mod = mod.getClass().getAnnotation(Mod.class);
        bookWiki.container = JsonFactory.getGson().fromJson(reader, BookWikiContainer.class);
        for (BookWikiContainer.Category category : bookWiki.container.getCategories()) {
            category.setContainer(bookWiki.container);
        }
        for (BookWikiContainer.Page page : bookWiki.container.getPages()) {
            page.setContainer(bookWiki.container);
        }
        for (BookWikiContainer.Recipe recipe : bookWiki.container.getRecipes()) {
            recipe.setContainer(bookWiki.container);
        }
        GameRegistry.registerItem(bookWiki.item = new BookWikiItem(bookWiki), "bookWiki." + bookWiki.getMod().modid());
        return bookWiki;
    }

    public Object getModInstance() {
        return modInstance;
    }

    public Mod getMod() {
        return mod;
    }

    public BookWikiContainer getContainer() {
        return container;
    }

    public BookWikiItem getItem() {
        return item;
    }

    public BookWikiContainer.Category getCategoryByID(String id) {
        for (BookWikiContainer.Category category : getContainer().getCategories()) {
            if (category.getID().equals(id)) {
                return category;
            }
        }
        return null;
    }

    public BookWikiContainer.Page getPageByID(String id) {
        for (BookWikiContainer.Page page : getContainer().getPages()) {
            if (page.getID().equals(id)) {
                return page;
            }
        }
        return null;
    }

    public BookWikiContainer.Page[] getPagesFromCategory(BookWikiContainer.Category category) {
        List<BookWikiContainer.Page> pageList = Lists.newArrayList();
        for (BookWikiContainer.Page page : getContainer().getPages()) {
            if (page.getCategory() == category) {
                pageList.add(page);
            }
        }
        return pageList.toArray(new BookWikiContainer.Page[pageList.size()]);
    }

    public int getPageNumber(BookWikiContainer.Category category, BookWikiContainer.Page page) {
        int i = 0;
        for (BookWikiContainer.Page p : getPagesFromCategory(category)) {
            if (p != page) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }

    public BookWikiContainer.Recipe getRecipeByID(String id) {
        for (BookWikiContainer.Recipe recipe : getContainer().getRecipes()) {
            if (recipe.getID().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public String getGeneralCategory() {
        String generalCategory = container.getGeneralCategory();
        if (generalCategory == null) {
            return "general";
        }
        return generalCategory;
    }
}
