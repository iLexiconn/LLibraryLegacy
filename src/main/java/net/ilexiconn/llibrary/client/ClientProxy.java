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
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public static RenderLLibraryPlayer renderCustomPlayer;

    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new GuiHelper());
        FMLCommonHandler.instance().bus().register(new GuiHelper());

        GuiHelper.addOverride(GuiMainMenu.class, new GuiLLibraryMainMenu());

        KeyBinding[] internalKeybindings = Minecraft.getMinecraft().gameSettings.keyBindings;
        KeyBinding targetKeybinding = Minecraft.getMinecraft().gameSettings.keyBindScreenshot;
        ClientEventHandler.screenshotKeyBinding = new KeyBinding(targetKeybinding.getKeyDescription(), targetKeybinding.getKeyCode(), targetKeybinding.getKeyCategory());
        for (int i = 0; i < internalKeybindings.length; ++i)
        {
            KeyBinding kb = internalKeybindings[i];
            if (kb.getKeyDescription().equals(targetKeybinding.getKeyDescription()))
            {
                internalKeybindings[i] = ClientEventHandler.screenshotKeyBinding;
                targetKeybinding.setKeyCode(-1);
            }
        }
    }

    public void postInit()
    {
        super.postInit();

        renderCustomPlayer = new RenderLLibraryPlayer();
        RenderManager.instance.entityRenderMap.put(EntityPlayer.class, renderCustomPlayer);
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
