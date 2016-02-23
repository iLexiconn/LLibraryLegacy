package net.ilexiconn.llibrary.client.component;

import net.ilexiconn.llibrary.client.book.IComponent;
import net.ilexiconn.llibrary.client.gui.BookWikiGui;
import net.ilexiconn.llibrary.common.book.BookWiki;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class EntityComponent extends Gui implements IComponent {
    @Override
    public char getID() {
        return 'e';
    }

    @Override
    public String init(String string, String arg, String group, BookWiki bookWiki) {
        return string.replace(group, group + "\n\n\n\n\n\n\n");
    }

    @Override
    public void render(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", arg);
        Entity entity = EntityList.createEntityFromNBT(nbt, mc.theWorld);
        mc.getTextureManager().bindTexture(gui.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        x += 14;
        y += mc.fontRendererObj.FONT_HEIGHT;
        Gui.drawModalRectWithCustomSizedTexture(x, y, 346, 23, 54, 54, 512.0F, 512.0F);
        gui.startGlScissor(x, y, 54, 54);
        if (entity instanceof EntityLivingBase) {
            GuiInventory.drawEntityOnScreen(x + 27, y + 47, 20, (float) (x + 27) - mouseX, (float) (y + 31) - mouseY, (EntityLivingBase) entity);
        }
        gui.endGlScissor();
    }

    @Override
    public void renderTooltip(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", arg);
        Entity entity = EntityList.createEntityFromNBT(nbt, mc.theWorld);
        x += 14;
        y += mc.fontRendererObj.FONT_HEIGHT;
        if (mouseX + 1 >= x + 1 && mouseY + 1 >= y + 1 && mouseX + 1 < x + 1 + 54 && mouseY + 1 < y + 1 + 54) {
            gui.drawHoveringText(entity.getName(), mouseX, mouseY, mc.fontRendererObj);
        }
    }
}
