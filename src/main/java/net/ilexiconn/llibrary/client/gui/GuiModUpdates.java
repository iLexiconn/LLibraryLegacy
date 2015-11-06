package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.common.config.JsonConfigHelper;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.json.container.JsonUpdate;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * @author FiskFile
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.update.UpdateHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiModUpdates extends GuiScreen {
    private GuiSlotModUpdateContainerList modList;
    private int selectedIndex = -1;
    private int listWidth;

    private GuiButton buttonUpdate;
    private GuiButton buttonDone;

    public void initGui() {
        buttonList.clear();

        buttonList.add(buttonDone = new GuiButton(0, width / 2 - 75, height - 38, I18n.format("gui.done")));

        for (JsonModUpdate mod : VersionHandler.getOutdatedMods()) {
            int i = 20 + 32;
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.name) + i);
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.getUpdateVersion().getVersionString()) + i);
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.updateType.name()) + i);
        }

        listWidth = Math.min(listWidth, 200);
        modList = new GuiSlotModUpdateContainerList(this, listWidth);
        modList.registerScrollButtons(buttonList, 7, 8);
        buttonList.add(buttonUpdate = new GuiButton(1, 10, height - 38, listWidth, 20, I18n.format("gui.llibrary.updatecheck.update")));
        buttonUpdate.visible = false;

        centerDoneButton();
    }

    public void centerDoneButton() {
        int j = 0;
        int k = 0;

        for (Object obj : buttonList) {
            GuiButton button = (GuiButton) obj;
            int id = button.id;

            if (id == 0) {
                k = button.xPosition;
            } else if (id == 1) {
                j = button.xPosition + button.width;
            }
        }

        if (j > k) {
            ((GuiButton) buttonList.get(0)).xPosition += j - k + 20;
        }
    }

    protected void actionPerformed(GuiButton button) {
        int id = button.id;

        if (id == 0) {
            mc.displayGuiScreen(new GuiMainMenu());
        } else if (id == 1) {
            if (selectedIndex != -1 && selectedIndex < VersionHandler.getOutdatedMods().size()) {
                final JsonModUpdate mod = VersionHandler.getOutdatedMods().get(selectedIndex);

                if (mod.getDirectUpdateUrl() == null || !mod.getDirectUpdateUrl().startsWith("http://minecraft.curseforge.com/")) {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI(mod.getUpdateUrl()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    new Thread() {
                        public void run() {
                            try {
                                buttonDone.enabled = false;
                                buttonUpdate.enabled = false;
                                URL url = new URL(mod.getDirectUpdateUrl());
                                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                                long completeFileSize = httpConnection.getContentLength();

                                BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                                File file = new File("tempmods");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File modfile = mod.modContainer.getSource();
                                if (!modfile.getName().endsWith(".jar")) {
                                    modfile = new File(mod.name + "-" + mod.getUpdateVersion().getVersionString() + ".jar");
                                }
                                FileOutputStream fos = new FileOutputStream(new File(file, modfile.getName()));
                                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                                byte[] data = new byte[1024];
                                long downloadedFileSize = 0;
                                int x;
                                while ((x = in.read(data, 0, 1024)) >= 0) {
                                    downloadedFileSize += x;
                                    int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100d);
                                    buttonUpdate.displayString = currentProgress + "%";
                                    bout.write(data, 0, x);
                                }
                                bout.close();
                                in.close();
                                File json = new File(file, "update.json");
                                JsonUpdate update = JsonConfigHelper.loadConfig(json, JsonUpdate.class);
                                update.delete.add(modfile.getName());
                                JsonConfigHelper.saveConfig(update, json);
                                mod.updated = true;
                                buttonUpdate.displayString = "Updated";
                                buttonDone.enabled = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int i = width / 2;
        int j = height / 2;

        if (modList != null) {
            modList.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (VersionHandler.getOutdatedMods().isEmpty()) {
            buttonDone.xPosition = width / 2 - 100;
            buttonDone.yPosition = height - 38;
            buttonList.remove(buttonUpdate);
            drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line1"), i, j - 40, 0xffffff, 2F);
            drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line2"), i, j - 20, 0xffffff, 1F);
        } else {
            if (selectedIndex != -1 && selectedIndex < VersionHandler.getOutdatedMods().size()) {
                buttonUpdate.visible = true;

                JsonModUpdate mod = VersionHandler.getOutdatedMods().get(selectedIndex);
                String[] changelog = ChangelogHandler.getChangelog(mod, mod.getUpdateVersion());

                if (mod.updated) {
                    buttonUpdate.displayString = "Updated";
                    buttonUpdate.enabled = false;
                } else {
                    buttonUpdate.displayString = "Update";
                    buttonUpdate.enabled = true;
                }

                int k = modList.getLeft() + listWidth + 20 - width / 2 + 180;
                int l = modList.getTop() - height / 2 + 100;

                drawString(fontRendererObj, mod.name + " " + mod.getUpdateVersion().getVersionString(), k + width / 2 - 190, l + height / 2 - 97, 0xffffff);

                for (int x = 0; x < changelog.length; ++x) {
                    if (changelog[x] != null) {
                        drawString(fontRendererObj, changelog[x], k + width / 2 - 190, l + height / 2 - 65 + x * 10, 0xffffff);
                    }
                }
            }

            drawCenteredString(fontRendererObj, "Mod Updates", width / 2, 16, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawCenteredString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    public void selectItemIndex(int var1) {
        selectedIndex = var1;
    }

    public boolean itemIndexSelected(int var1) {
        return selectedIndex == var1;
    }

    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    public Minecraft getMinecraftInstance() {
        return Minecraft.getMinecraft();
    }
}