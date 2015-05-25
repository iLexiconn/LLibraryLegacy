package net.ilexiconn.llibrary.client;

import net.ilexiconn.llibrary.client.gui.GuiChangelog;
import net.ilexiconn.llibrary.client.gui.GuiHelper;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryMainMenu;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.ilexiconn.llibrary.client.render.entity.RenderLLibraryPlayer;
import net.ilexiconn.llibrary.common.ServerProxy;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

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

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();

        for (final Map.Entry<Item, String> item : RenderHelper.getItemModels().entrySet())
        {
            itemModelMesher.register(item.getKey(), new ItemMeshDefinition()
            {
                public ModelResourceLocation getModelLocation(ItemStack itemStack)
                {
                    return new ModelResourceLocation(item.getValue(), "inventory");
                }
            });
        }

        Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityPlayer.class, new RenderLLibraryPlayer());
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
