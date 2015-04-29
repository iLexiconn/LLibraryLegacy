package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FiskFille
 */
@SideOnly(Side.CLIENT)
public abstract class GuiPickItem extends GuiScreen
{
    public String title;
    public GuiVerticalSlider slider = new GuiVerticalSlider(100, 0, 12, 15, 300, 10);
    private GuiTextField textField;
    private RenderItem renderItem = new RenderItem();
    private ArrayList<ItemStack> items = Lists.newArrayList();

    public GuiPickItem(String t)
    {
        title = t;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        setWorldAndResolution(mc, w, h);

        textField = new GuiTextField(fontRendererObj, width / 2 - 45, 30, 103, 12);
        textField.setMaxStringLength(40);

        for (Item item : (Iterable<Item>) Item.itemRegistry)
        {
            ItemStack itemstack = new ItemStack(item);

            if (item != null)
            {
                try
                {
                    items.add(itemstack);

                    List subItems = Lists.newArrayList();

                    item.getSubItems(item, null, subItems);

                    int maxDamage = subItems.size() - 1;

                    while (item.getHasSubtypes() && itemstack.getItemDamage() < maxDamage)
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + 1);

                        if (!(item instanceof ItemDoublePlant) && !(Block.getBlockFromItem(item) instanceof BlockMobSpawner) && !(Block.getBlockFromItem(item) instanceof BlockDoublePlant) && !(item instanceof ItemMonsterPlacer))
                        {
                            items.add(new ItemStack(item, 1, itemstack.getItemDamage()));
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public abstract void onClickEntry(ItemStack itemstack, EntityPlayer player);

    protected void keyTyped(char c, int key)
    {
        Keyboard.enableRepeatEvents(true);
        textField.textboxKeyTyped(c, key);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
    {
        slider.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
    }

    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
        slider.mouseClicked(mouseX, mouseY, button);
        textField.mouseClicked(mouseX, mouseY, button);
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int event)
    {
        slider.mouseMovedOrUp(mouseX, mouseY, event);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        setWorldAndResolution(mc, w, h);

        drawDefaultBackground();
        drawCenteredString(fontRendererObj, title, width / 2, 15, 16777215);
        int x = width / 2 - 45;
        int y = 30;
        int scrollY;
        int i = height - 20;
        slider.minScroll = 40;
        slider.maxScroll = i - 7;
        slider.x = width / 2 - 70;
        slider.drawScreen(mouseX, mouseY, partialTicks);

        boolean selected;

        List<ItemStack> displayItems = Lists.newArrayList();

        for (ItemStack itemstack : items)
        {
            try
            {
                String name = StatCollector.translateToLocal(itemstack.getDisplayName());

                Item item = itemstack.getItem();

                boolean tabEquals = false;

                if (item != null)
                {
                    for (CreativeTabs tab : item.getCreativeTabs())
                    {
                        if (tab != null)
                        {
                            tabEquals = StatCollector.translateToLocal(tab.getTranslatedTabLabel()).toLowerCase().contains(textField.getText().toLowerCase());

                            if (tabEquals)
                            {
                                break;
                            }
                        }
                    }
                }

                if (name.toLowerCase().contains(textField.getText().toLowerCase()) || tabEquals)
                {
                    displayItems.add(itemstack);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        scrollY = ((-slider.y + slider.minScroll) * ((displayItems.size()) / ((i - slider.minScroll) / 19)));

        for (ItemStack itemstack : displayItems)
        {
            try
            {
                String name = StatCollector.translateToLocal(itemstack.getDisplayName());
                selected = mouseX >= x && mouseX < x + fontRendererObj.getStringWidth(name) + 20 && mouseY >= y + 16 + scrollY && mouseY < y + 32 + scrollY;

                y += 18;

                if (y + scrollY <= height && y + scrollY > 42)
                {
                    drawString(fontRendererObj, name + (selected ? " <" : ""), x + 20, y + 4 + scrollY, selected ? 0xFFFF7F : 0xFFFFFF);
                    drawString(fontRendererObj, selected ? ">" : "", x - 10, y + 4 + scrollY, 0xFFFF7F);

                    try
                    {
                        drawItemStack(x, y + scrollY, itemstack);
                    }
                    catch (Exception e)
                    {

                    }
                }

                if (selected)
                {
                    if (Mouse.isButtonDown(0))
                    {
                        playClickSound(mc.getSoundHandler());
                        onClickEntry(itemstack, mc.thePlayer);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawItemStack(int x, int y, ItemStack itemstack)
    {
        RenderHelper.enableGUIStandardItemLighting();
        zLevel = 100f;
        renderItem.zLevel = 100f;
        GL11.glEnable(2896);
        GL11.glEnable(32826);
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
        renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        renderItem.zLevel = 0f;
        zLevel = 0f;
        RenderHelper.disableStandardItemLighting();
    }

    public void playClickSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1f));
    }
}