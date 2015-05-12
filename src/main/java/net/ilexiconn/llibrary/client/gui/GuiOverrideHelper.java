package net.ilexiconn.llibrary.client.gui;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.MouseEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author FiskFille
 */
@SideOnly(Side.CLIENT)
public class GuiOverrideHelper
{
	private Minecraft mc = Minecraft.getMinecraft();
	private static Map<GuiOverride, Class<? extends GuiScreen>> overrideMap = Maps.newHashMap();
	
	private static long initialTime = System.nanoTime();
	private static final double timeU = 1000000000 / 20;
	private static double deltaU = 0;
	private static int ticks = 0;
	private static long timer = System.currentTimeMillis();
	
	public static void addOverride(Class<? extends GuiScreen> clazz, GuiOverride gui)
	{
		overrideMap.put(gui, clazz);
	}
	
	public static List<GuiOverride> getOverridesForGui(Class<? extends GuiScreen> clazz)
	{
		List<GuiOverride> list = Lists.newArrayList();
		
		for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : overrideMap.entrySet())
		{
			if (e.getValue() == clazz)
			{
				list.add(e.getKey());
			}
		}
		
		return list;
	}
	
	public static Map<GuiOverride, Class<? extends GuiScreen>> getOverrides()
	{
		return overrideMap;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDrawScreen(DrawScreenEvent.Post event)
	{
		for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : GuiOverrideHelper.getOverrides().entrySet())
		{
			if (event.gui.getClass() == e.getValue())
			{
				GuiOverride gui = e.getKey();
				long currentTime = System.nanoTime();
		        deltaU += (currentTime - initialTime) / timeU;
		        initialTime = currentTime;
		        
		        gui.buttonList.clear();
		        gui.initGui();
		        gui.width = event.gui.width;
		        gui.height = event.gui.height;
				gui.overriddenScreen = event.gui;
				
		        if (deltaU >= 1)
		        {
		        	gui.updateScreen();
		            ticks++;
		            deltaU--;
		        }

		        if (System.currentTimeMillis() - timer > 1000)
		        {
		            ticks = 0;
		            timer += 1000;
		        }
				
				gui.drawScreen(event.mouseX, event.mouseY, event.renderPartialTicks);
				
				if (!gui.buttonList.isEmpty())
				{
					List<GuiButton> buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, event.gui, "buttonList", "field_146292_n");
					
					for (GuiButton button : (List<GuiButton>)gui.buttonList)
					{
						for (int i = 0; i < buttonList.size(); ++i)
						{
							GuiButton button1 = buttonList.get(i);
							
//							if (button.xPosition == button1.xPosition && button.yPosition == button1.yPosition && button.width == button1.width && button.height == button1.height)
							if (button.id == button1.id)
							{
								buttonList.remove(button1);
							}
						}
					}
					
					buttonList.addAll(gui.buttonList);
					ObfuscationReflectionHelper.setPrivateValue(GuiScreen.class, event.gui, buttonList, "buttonList", "field_146292_n");
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onInitGui(InitGuiEvent.Pre event)
	{
		for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : GuiOverrideHelper.getOverrides().entrySet())
		{
			if (event.gui.getClass() == e.getValue())
			{
				e.getKey().setWorldAndResolution(mc, event.gui.width, event.gui.height);
			}
		}
	}
	
	@SubscribeEvent
	public void onMouseClick(MouseEvent event)
	{
		System.out.println(event.button);
	}
}