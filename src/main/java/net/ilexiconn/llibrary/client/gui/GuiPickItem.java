package net.ilexiconn.llibrary.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.GuiSlotModList;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author FiskFille
 */
@SideOnly(Side.CLIENT)
public abstract class GuiPickItem extends GuiScreen
{
    public String title;
    private GuiScreen parentScreen;
    private GuiSlotItemStackList itemList;
    private GuiTextField textField;
    private RenderItem renderItem = new RenderItem();
    private ArrayList<ItemStack> items = Lists.newArrayList();
    public ArrayList<ItemStack> itemsFiltered = Lists.newArrayList(); 
    
    private int selectedIndex;
    private int listWidth;

    public GuiPickItem(String t)
    {
        title = t;
        parentScreen = Minecraft.getMinecraft().currentScreen;
        
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        setWorldAndResolution(mc, w, h);

        textField = new GuiTextField(fontRendererObj, 20, 30, 103, 12);
        textField.setMaxStringLength(40);

        for (Item item : (Iterable<Item>)Item.itemRegistry)
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
    
    public void initGui()
    {
    	super.initGui();
    	
    	for (ItemStack itemstack : items)
    	{
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(StatCollector.translateToLocal(itemstack.getDisplayName())) + 30);
        }
    	
        listWidth = Math.min(listWidth, 300);
        
        if (textField != null)
        {
        	textField.xPosition = 20 + listWidth / 2 - textField.width / 2;
            textField.yPosition = 30;
        }
        
        buttonList.add(new GuiButton(0, 20, height - 40, listWidth, 20, "Select"));
    	itemList = new GuiSlotItemStackList(this, items, listWidth);
    	itemList.registerScrollButtons(buttonList, 7, 8);
    }
    
    public abstract void onSelectEntry(ItemStack itemstack, EntityPlayer player);
    
    protected void actionPerformed(GuiButton button)
    {
    	int id = button.id;
    	
    	if (id == 0)
    	{
    		onSelectEntry(itemsFiltered.get(selectedIndex), Minecraft.getMinecraft().thePlayer);
    	}
    }

    protected void keyTyped(char c, int key)
    {
        Keyboard.enableRepeatEvents(true);
        textField.textboxKeyTyped(c, key);
        
        if (key == Keyboard.KEY_ESCAPE)
        {
        	mc.displayGuiScreen(parentScreen);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void mouseClicked(int mouseX, int mouseY, int button)
    {
    	super.mouseClicked(mouseX, mouseY, button);
        textField.mouseClicked(mouseX, mouseY, button);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	filterItemsBySearch();
    	itemList.drawScreen(mouseX, mouseY, partialTicks);
    	
    	drawCenteredString(fontRendererObj, title, 20 + listWidth / 2, 15, 16777215);
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void filterItemsBySearch()
    {
    	itemsFiltered.clear();
    	
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
					itemsFiltered.add(itemstack);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void drawItemStack(int x, int y, ItemStack itemstack)
    {
        RenderHelper.enableGUIStandardItemLighting();
        zLevel = 100f;
        renderItem.zLevel = 100f;
        GL11.glEnable(2896);
        GL11.glEnable(32826);
        renderItem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);        
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

	public void selectItemIndex(int var1)
	{
		selectedIndex = var1;
	}

	public boolean itemIndexSelected(int var1)
	{
		return selectedIndex == var1;
	}

	public FontRenderer getFontRenderer()
	{
		return fontRendererObj;
	}

	public Minecraft getMinecraftInstance()
	{
		return Minecraft.getMinecraft();
	}
}