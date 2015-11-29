package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.ilexiconn.llibrary.common.config.JsonConfigHelper;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.llibrary.common.json.container.JsonModUpdate;
import net.ilexiconn.llibrary.common.json.container.JsonUpdateEntry;
import net.ilexiconn.llibrary.common.update.ChangelogHandler;
import net.ilexiconn.llibrary.common.update.VersionHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author FiskFile
 * @author iLexiconn
 * @see net.ilexiconn.llibrary.common.update.UpdateHelper
 * @since 0.1.0
 */
@SideOnly(Side.CLIENT)
public class GuiModUpdates extends GuiScreen {
    private GuiScreen mainMenu;
    private GuiModUpdateList modList;
    private GuiScrollingList modInfo;
    private int selected = -1;
    private JsonModUpdate selectedMod;
    private int listWidth;
    private ArrayList<JsonModUpdate> mods;
    private GuiButton buttonUpdate;
    private GuiButton buttonDone;
    private int numButtons = SortType.values().length;
    private String lastFilterText = "";
    private GuiTextField search;
    private boolean sorted = false;
    private SortType sortType = SortType.NORMAL;

    public GuiModUpdates(GuiScreen mainMenu) {
        this.mainMenu = mainMenu;
        this.mods = new ArrayList<JsonModUpdate>();
        this.mods.addAll(VersionHandler.getOutdatedMods());
    }

    @Override
    public void initGui() {
        for (JsonModUpdate mod : mods) {
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(mod.name) + 47);
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(mod.currentVersion) + 47);
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(org.apache.commons.lang3.StringUtils.capitalize(mod.updateType.name().toLowerCase())) + 47);
        }
        listWidth = Math.min(listWidth, 150);
        this.modList = new GuiModUpdateList(this, mods, listWidth);

        this.buttonList.add(buttonDone = new GuiButton(6, ((modList.getRight() + this.width) / 2) - 100, this.height - 38, I18n.format("gui.done")));
        this.buttonList.add(buttonUpdate = new GuiButton(20, 10, this.height - 38, this.listWidth, 20, "Update"));

        search = new GuiTextField(0, fontRendererObj, 12, modList.getBottom() + 17, modList.getListWidth() - 4, 14);
        search.setFocused(true);
        search.setCanLoseFocus(true);

        int width = (modList.getListWidth() / numButtons);
        int x = 10, y = 10;
        int buttonMargin = 1;
        GuiButton normalSort = new GuiButton(SortType.NORMAL.buttonID, x, y, width - buttonMargin, 20, I18n.format("fml.menu.mods.normal"));
        normalSort.enabled = false;
        buttonList.add(normalSort);
        x += width + buttonMargin;
        buttonList.add(new GuiButton(SortType.A_TO_Z.buttonID, x, y, width - buttonMargin, 20, "A-Z"));
        x += width + buttonMargin;
        buttonList.add(new GuiButton(SortType.Z_TO_A.buttonID, x, y, width - buttonMargin, 20, "Z-A"));

        updateCache();
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        search.mouseClicked(x, y, button);
        if (button == 1 && x >= search.xPosition && x < search.xPosition + search.width && y >= search.yPosition && y < search.yPosition + search.height) {
            search.setText("");
        }
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        search.textboxKeyTyped(c, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        search.updateCursorCounter();

        if (!search.getText().equals(lastFilterText)) {
            reloadMods();
            sorted = false;
        }

        if (!sorted) {
            reloadMods();
            Collections.sort(mods, sortType);
            selected = mods.indexOf(selectedMod);
            modList.setSelected(mods.indexOf(selectedMod));
            sorted = true;
        }
    }

    private void reloadMods() {
        ArrayList<JsonModUpdate> mods = modList.getMods();
        mods.clear();
        for (JsonModUpdate m : VersionHandler.getOutdatedMods()) {
            if (m.name.toLowerCase().contains(search.getText().toLowerCase())) {
                mods.add(m);
            }
        }
        this.mods = mods;
        lastFilterText = search.getText();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            SortType type = SortType.getTypeForButton(button);

            if (type != null) {
                for (GuiButton b : buttonList) {
                    if (SortType.getTypeForButton(b) != null) {
                        b.enabled = true;
                    }
                }
                button.enabled = false;
                sorted = false;
                sortType = type;
                this.mods = modList.getMods();
            } else {
                switch (button.id) {
                    case 6: {
                        this.mc.displayGuiScreen(this.mainMenu);
                        return;
                    }
                    case 20: {
                        if (selectedMod.getDirectUpdateUrl() != null) {
                            File modfile = selectedMod.modContainer.getSource();
                            File configFile = new File("updatequeue.json");
                            List<JsonUpdateEntry> updateQueue;
                            if (!configFile.exists()) {
                                updateQueue = Lists.newArrayList();
                            } else {
                                try {
                                    updateQueue = JsonFactory.getGson().fromJson(new FileReader(configFile), new TypeToken<List<JsonModUpdate>>(){}.getType());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            updateQueue.add(new JsonUpdateEntry(selectedMod.getDirectUpdateUrl(), selectedMod.modid, selectedMod.name, modfile.getName(), selectedMod.getUpdateVersion().getVersionString()));
                            JsonConfigHelper.saveConfig(updateQueue, configFile);
                            selectedMod.updated = true;
                            buttonUpdate.enabled = false;
                            buttonUpdate.displayString = "Restart to apply";
                        } else {
                            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    desktop.browse(new URI(selectedMod.getUpdateUrl()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (VersionHandler.getOutdatedMods().isEmpty()) {
            drawDefaultBackground();
            int i = width / 2;
            int j = height / 2;
            buttonDone.xPosition = width / 2 - 100;
            buttonDone.yPosition = height - 38;
            buttonList.clear();
            buttonList.add(buttonDone);
            drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line1"), i, j - 40, 0xffffff, 2F);
            drawScaledString(I18n.format("gui.llibrary.updatecheck.no_updates.line2"), i, j - 20, 0xffffff, 1F);
        } else {
            this.modList.drawScreen(mouseX, mouseY, partialTicks);
            if (this.modInfo != null) {
                this.modInfo.drawScreen(mouseX, mouseY, partialTicks);
            }

            int left = ((this.width - this.listWidth - 38) / 2) + this.listWidth + 30;
            this.drawCenteredString(this.fontRendererObj, "Mod Updates", left, 16, 0xFFFFFF);

            String text = I18n.format("fml.menu.mods.search");
            int x = ((10 + modList.getRight()) / 2) - (fontRendererObj.getStringWidth(text) / 2);
            fontRendererObj.drawString(text, x, modList.getBottom() + 5, 0xFFFFFF);
            search.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void selectModIndex(int index) {
        if (index == this.selected) {
            return;
        }
        this.selected = index;
        this.selectedMod = (index >= 0 && index <= mods.size()) ? mods.get(selected) : null;

        updateCache();
    }

    public boolean modIndexSelected(int index) {
        return index == selected;
    }

    private void updateCache() {
        buttonUpdate.visible = false;
        modInfo = null;

        if (selectedMod == null) {
            return;
        }

        List<String> lines = new ArrayList<String>();

        buttonUpdate.visible = true;
        if (selectedMod.updated) {
            buttonUpdate.enabled = false;
            buttonUpdate.displayString = "Restart to apply";
        } else {
            buttonUpdate.enabled = true;
            buttonUpdate.displayString = "Update";
        }

        lines.add(selectedMod.name);
        lines.add(String.format("Current version: %s", selectedMod.currentVersion));
        lines.add(String.format("Newest version: %s (%s)", selectedMod.getUpdateVersion().getVersionString(), selectedMod.updateType.name()));
        lines.add(null);
        Collections.addAll(lines, ChangelogHandler.getChangelog(selectedMod, selectedMod.getUpdateVersion()));

        modInfo = new Info(this.width - this.listWidth - 30, lines);
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawCenteredString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    private class Info extends GuiScrollingList {
        private List<IChatComponent> lines = null;

        public Info(int width, List<String> lines) {
            super(GuiModUpdates.this.mc, width, GuiModUpdates.this.height, 32, GuiModUpdates.this.height - 77 + 4, GuiModUpdates.this.listWidth + 20, 60, GuiModUpdates.this.width, GuiModUpdates.this.height);
            this.lines = resizeContent(lines);
            this.setHeaderInfo(true, getHeaderHeight());
        }

        @Override
        protected int getSize() {
            return 0;
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

        }

        private List<IChatComponent> resizeContent(List<String> lines) {
            List<IChatComponent> ret = new ArrayList<IChatComponent>();
            for (String line : lines) {
                if (line == null) {
                    ret.add(null);
                    continue;
                }

                IChatComponent chat = ForgeHooks.newChatWithLinks(line, false);
                ret.addAll(GuiUtilRenderComponents.func_178908_a(chat, this.listWidth - 8, GuiModUpdates.this.fontRendererObj, false, true));
            }
            return ret;
        }

        private int getHeaderHeight() {
            int height = (lines.size() * 10);
            if (height < this.bottom - this.top - 8) {
                height = this.bottom - this.top - 8;
            }
            return height;
        }

        @Override
        protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
            int top = relativeY;

            for (IChatComponent line : lines) {
                if (line != null) {
                    GlStateManager.enableBlend();
                    GuiModUpdates.this.fontRendererObj.drawStringWithShadow(line.getFormattedText(), this.left + 4, top, 0xFFFFFF);
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                top += 10;
            }
        }

        @Override
        protected void clickHeader(int x, int y) {
            if (y <= 0) {
                return;
            }

            int lineIdx = y / 10;
            if (lineIdx >= lines.size()) {
                return;
            }

            IChatComponent line = lines.get(lineIdx);
            if (line != null) {
                int k = -4;
                for (IChatComponent part : line) {
                    if (!(part instanceof ChatComponentText)) {
                        continue;
                    }
                    k += GuiModUpdates.this.fontRendererObj.getStringWidth(((ChatComponentText) part).getChatComponentText_TextValue());
                    if (k >= x) {
                        GuiModUpdates.this.handleComponentClick(part);
                        break;
                    }
                }
            }
        }
    }

    private enum SortType implements Comparator<JsonModUpdate> {
        NORMAL(24),
        A_TO_Z(25) {
            @Override
            protected int compare(String name1, String name2) {
                return name1.compareTo(name2);
            }
        },
        Z_TO_A(26) {
            @Override
            protected int compare(String name1, String name2) {
                return name2.compareTo(name1);
            }
        };

        private int buttonID;

        SortType(int buttonID) {
            this.buttonID = buttonID;
        }

        public static SortType getTypeForButton(GuiButton button) {
            for (SortType t : values()) {
                if (t.buttonID == button.id) {
                    return t;
                }
            }
            return null;
        }

        protected int compare(String name1, String name2) {
            return 0;
        }

        @Override
        public int compare(JsonModUpdate o1, JsonModUpdate o2) {
            String name1 = StringUtils.stripControlCodes(o1.name).toLowerCase();
            String name2 = StringUtils.stripControlCodes(o2.name).toLowerCase();
            return compare(name1, name2);
        }
    }
}