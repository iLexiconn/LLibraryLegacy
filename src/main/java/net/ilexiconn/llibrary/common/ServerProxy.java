package net.ilexiconn.llibrary.common;

import cpw.mods.fml.common.registry.GameData;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.entity.EntityHelper;
import net.ilexiconn.llibrary.common.entity.EntityMountableBlock;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.survivaltab.ISurvivalTab;
import net.ilexiconn.llibrary.common.survivaltab.TabHelper;
import net.ilexiconn.llibrary.common.update.UpdateHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class ServerProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        EntityHelper.registerEntity("mountableBlock", EntityMountableBlock.class);

        for (int i = 0; i < 11; i++)
        {
            final int index = i;
            TabHelper.registerSurvivalTab(new ISurvivalTab()
            {
                public String getTabName()
                {
                    return "Ender Chest";
                }

                public ItemStack getTabIcon()
                {
                    return new ItemStack(GameData.getBlockRegistry().getObjectById(index));
                }

                public void openContainer(Minecraft mc, EntityPlayer player)
                {
                    player.displayGUIChest(player.getInventoryEnderChest());
                }

                public Class<? extends GuiContainer> getContainerGuiClass()
                {
                    return GuiChest.class;
                }
            });
        }

        try
        {
            UpdateHelper.registerUpdateChecker(LLibrary.instance, "http://pastebin.com/raw.php?i=TGiS6kuk");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void postInit()
    {

    }

    public void openChangelogGui(JsonModUpdate mod, String version)
    {

    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }
}
