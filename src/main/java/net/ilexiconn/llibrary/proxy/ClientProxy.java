package net.ilexiconn.llibrary.proxy;

import net.ilexiconn.llibrary.client.ClientEventHandler;
import net.ilexiconn.llibrary.client.render.player.RenderCustomPlayer;
import net.ilexiconn.llibrary.gui.GuiChangelog;
import net.ilexiconn.llibrary.update.ChangelogHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    
    public void postInit()
    {
    	super.postInit();
    	
        RenderManager.instance.entityRenderMap.put(EntityPlayer.class, new RenderCustomPlayer());
    }
    
    public void openChangelogGui(ModUpdateContainer mod, String version)
    {
		String[] astring = null;
		try {astring = ChangelogHandler.getChangelog(mod, version);} catch (Exception e) {e.printStackTrace();}
		
		Minecraft.getMinecraft().displayGuiScreen(new GuiChangelog(mod, version, astring));
    }
}
