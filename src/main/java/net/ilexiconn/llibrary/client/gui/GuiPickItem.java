package net.ilexiconn.llibrary.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author FiskFille
 */
@SideOnly(Side.CLIENT)
public abstract class GuiPickItem extends GuiScreen
{
	private RenderItem renderItem = new RenderItem();
	public String text = "";
	public final String title;

	private ArrayList<ItemStack> items = Lists.newArrayList();
	
	public GuiPickItem(String title)
	{
		super();
		this.title = title;
		
		Iterator<Item> iterator = Item.itemRegistry.iterator();

		while (iterator.hasNext())
		{
			Item item = iterator.next();
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

	protected void keyTyped(char character, int par2)
	{
		super.keyTyped(character, par2);
		Keyboard.enableRepeatEvents(true);

		if (ChatAllowedCharacters.isAllowedCharacter(character) && fontRendererObj.getStringWidth(text + character + "_") < 90)
		{
			text += character;
		}
		else if (text.length() > 0 && character == 8)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
			{
				text = text.substring(0, text.contains(" ") ? text.lastIndexOf(" ") : 0); 
			}
			else
			{
				text = text.substring(0, text.length() - 1);
			}
		}
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, title, width / 2, 15, 16777215);
		int x = width / 2 - 45;
		int y = 30;
		mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png"));
		drawTexturedModalRect(x, y, 80, 4, 90, 12);
		drawString(fontRendererObj, text + (mc.thePlayer.ticksExisted % 20 >= 10 ? "" : "_"), x + 2, y + 2, 0xffffff);

		boolean selected = false;

		for (ItemStack itemstack : items)
		{
			try
			{
				String name = StatCollector.translateToLocal(itemstack.getDisplayName());
				selected = mouseX >= x && mouseX < x + fontRendererObj.getStringWidth(name) + 20 && mouseY >= y + 16 && mouseY < y + 32;

				if (y <= height && name.toLowerCase().contains(text.toLowerCase()))
				{
					y += 16 + 4;
					drawString(fontRendererObj, name, x + 20, y + 4, selected ? 0xFFFF7F : 0xFFFFFF);

					drawItemStack(x, y, itemstack);

					if (selected)
					{					
						if (Mouse.isButtonDown(0))
						{
							onClickEntry(itemstack, mc.thePlayer);
						}
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("[LLibrary] Error while renderering item: " + itemstack.getItem().getUnlocalizedName());
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
		renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
		renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		renderItem.zLevel = 0f;
		zLevel = 0f;
		RenderHelper.disableStandardItemLighting();
	}
}