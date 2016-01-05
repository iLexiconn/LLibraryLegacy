package net.ilexiconn.llibrary.client;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.gui.GuiHelper;
import net.ilexiconn.llibrary.client.gui.GuiOverride;
import net.ilexiconn.llibrary.client.render.entity.RenderLLibraryEntity;
import net.ilexiconn.llibrary.client.render.entity.RenderLLibraryPlayer;
import net.ilexiconn.llibrary.client.screenshot.ScreenshotHelper;
import net.ilexiconn.llibrary.client.toast.Toast;
import net.ilexiconn.llibrary.common.block.IHighlightedBlock;
import net.ilexiconn.llibrary.common.config.LLibraryConfigHandler;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    private static final double timeU = 1000000000 / 20;
    public static KeyBinding screenshotKeyBinding;
    private RenderPlayer prevRenderPlayer;
    private Minecraft mc = Minecraft.getMinecraft();
    private long initialTime = System.nanoTime();
    private double deltaU = 0;
    private long timer = System.currentTimeMillis();
    private EntityRenderer renderer;

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Specials.Post event) {
        if (event.entityPlayer == mc.thePlayer) {
            if (prevRenderPlayer != null) {
                RenderManager.instance.entityRenderMap.put(event.entityPlayer.getClass(), prevRenderPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;

        if (mc.thePlayer == player) {
            Render entityRenderObject = RenderManager.instance.getEntityRenderObject(event.entityPlayer);

            if (!(entityRenderObject instanceof RenderLLibraryPlayer)) {
                prevRenderPlayer = (RenderPlayer) entityRenderObject;
                RenderManager.instance.entityRenderMap.put(player.getClass(), ClientProxy.renderCustomPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = event.target.blockX;
            int y = event.target.blockY;
            int z = event.target.blockZ;

            Block block = event.player.worldObj.getBlock(x, y, z);

            if (block instanceof IHighlightedBlock) {
                List<AxisAlignedBB> bounds = ((IHighlightedBlock) block).getHighlightedBoxes(event.player.worldObj, x, y, z, event.player);

                Vec3 pos = event.player.getPosition(event.partialTicks);

                GL11.glEnable(GL11.GL_BLEND);

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(0f, 0f, 0f, 0.4f);
                GL11.glLineWidth(2f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                for (AxisAlignedBB box : bounds) {
                    RenderGlobal.drawOutlinedBoundingBox(box.copy().offset(x, y, z).offset(-pos.xCoord, -pos.yCoord, -pos.zCoord), -1);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips && LLibraryConfigHandler.showTooltipInfo) {
            event.toolTip.add(EnumChatFormatting.DARK_GRAY + "" + Item.itemRegistry.getNameForObject(event.itemStack.getItem()));
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu) {
            for (JsonModUpdate mod : VersionHandler.getOutdatedMods()) {
                if (!mod.updated) {
                    Toast.makeText("Update available!", mod.name, mod.currentVersion + " -> " + mod.getUpdateVersion().getVersionString()).show();
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (LLibraryConfigHandler.threadedScreenshots && ClientEventHandler.screenshotKeyBinding.isPressed()) {
            ScreenshotHelper.takeScreenshot();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : GuiHelper.getOverrides().entrySet()) {
            if (event.gui != null && event.gui.getClass() == e.getValue()) {
                GuiOverride gui = e.getKey();
                long currentTime = System.nanoTime();
                deltaU += (currentTime - initialTime) / timeU;
                initialTime = currentTime;

                gui.width = event.gui.width;
                gui.height = event.gui.height;
                gui.overriddenScreen = event.gui;

                if (deltaU >= 1) {
                    gui.updateScreen();
                    deltaU--;
                }

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                }

                gui.drawScreen(event.mouseX, event.mouseY, event.renderPartialTicks);

                if (!gui.buttonList.isEmpty()) {
                    for (GuiButton button : (List<GuiButton>) gui.buttonList) {
                        for (int i = 0; i < event.gui.buttonList.size(); ++i) {
                            GuiButton button1 = (GuiButton) event.gui.buttonList.get(i);

                            if (button.id == button1.id) {
                                event.gui.buttonList.remove(button1);
                            }
                        }
                    }

                    event.gui.buttonList.addAll(gui.buttonList);
                }
            }
        }

        for (Toast toast : Toast.getToastList()) {
            toast.getGui().drawToast();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        for (Toast toast : Toast.getToastList()) {
            toast.getGui().drawToast();
        }
    }

    @SubscribeEvent
    public void onButtonPress(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : GuiHelper.getOverrides().entrySet()) {
            if (event.gui.getClass() == e.getValue()) {
                e.getKey().actionPerformed(event.button);
            }
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Pre event) {
        for (Map.Entry<GuiOverride, Class<? extends GuiScreen>> e : GuiHelper.getOverrides().entrySet()) {
            if (event.gui.getClass() == e.getValue()) {
                e.getKey().setWorldAndResolution(mc, event.gui.width, event.gui.height);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<Toast> iterator = Toast.getToastList().iterator();
            while (iterator.hasNext()) {
                Toast toast = iterator.next();
                if (toast.tick() <= 0) {
                    iterator.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null) {
            if (event.phase == TickEvent.Phase.START) {
                if (renderer == null) {
                    renderer = new RenderLLibraryEntity(mc);
                }

                if (mc.entityRenderer != renderer) {
                    mc.entityRenderer = renderer;
                }
            }
        }
    }

    /*@SubscribeEvent
    public void onSurvivalTabClick(SurvivalTab.ClickEvent event) {
        if (event.getSurvivalTab() == LLibrary.tabInventory) {
            mc.displayGuiScreen(new GuiInventory(event.getEntityPlayer()));
        }
    }*/
}
