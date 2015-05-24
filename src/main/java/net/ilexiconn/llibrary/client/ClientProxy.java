package net.ilexiconn.llibrary.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.gui.GuiChangelog;
import net.ilexiconn.llibrary.client.gui.GuiHelper;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryMainMenu;
import net.ilexiconn.llibrary.client.render.entity.RenderLLibraryPlayer;
import net.ilexiconn.llibrary.common.ServerProxy;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new GuiHelper());
        FMLCommonHandler.instance().bus().register(new GuiHelper());

        GuiHelper.addOverride(GuiMainMenu.class, new GuiLLibraryMainMenu());
    }

    public void postInit()
    {
        super.postInit();

        RenderManager.instance.entityRenderMap.put(EntityPlayer.class, new RenderLLibraryPlayer());
    }

    public void openChangelogGui(JsonModUpdate mod, String version)
    {
        String[] changelog = null;

        try
        {
            changelog = ChangelogHandler.getChangelog(mod, version);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Minecraft.getMinecraft().displayGuiScreen(new GuiChangelog(mod, version, changelog));
    }

    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }
}
