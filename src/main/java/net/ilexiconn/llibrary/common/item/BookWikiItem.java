package net.ilexiconn.llibrary.common.item;

import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author iLexiconn
 */
public class BookWikiItem extends Item {
    private BookWiki bookWiki;

    public BookWikiItem(BookWiki bookWiki) {
        this.bookWiki = bookWiki;
        setUnlocalizedName("bookWiki." + bookWiki.getMod().modid());
        setCreativeTab(CreativeTabs.tabMisc);
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!player.isSneaking() && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            Minecraft.getMinecraft().displayGuiScreen(new BookWikiGui(bookWiki));
        }
        return itemStack;
    }
}
