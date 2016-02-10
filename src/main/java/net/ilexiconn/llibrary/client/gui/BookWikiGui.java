package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.common.book.BookWikiContainer;
import net.ilexiconn.llibrary.client.book.BookWikiAPI;
import net.ilexiconn.llibrary.client.book.IComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class BookWikiGui extends GuiScreen {
    private ResourceLocation texture;
    private BookWiki bookWiki;
    private BookWikiContainer.Category currentCategory;
    private BookWikiContainer.Page currentPage;

    public BookWikiGui(BookWiki bookWiki) {
        this.bookWiki = bookWiki;
        this.currentCategory = bookWiki.getCategoryByID(bookWiki.getGeneralCategory());
        this.currentPage = currentCategory.getDefaultPage();
        this.texture = new ResourceLocation("llibrary", "textures/bookwiki/gui.png");
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(width / 2 - 292 / 2, height / 2 - 180 / 2, 0, 0, 292, 180, 512.0F, 512.0F);
        String hover = null;
        for (int i = 0; i < bookWiki.getContainer().getCategories().length; i++) {
            BookWikiContainer.Category category = bookWiki.getContainer().getCategories()[i];
            int x = width / 2 - 292 / 2 + 14 + 27 * i;
            int y = height / 2 - 180 / 2 - 24 + 7;
            mc.getTextureManager().bindTexture(texture);
            GlStateManager.disableLighting();
            ItemStack stack = category.getIcon();
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().zLevel = 100.0F;
            if (currentCategory == category) {
                drawModalRectWithCustomSizedTexture(x - 3, y, 374, 77, 27, 24, 512.0F, 512.0F);
                startGlScissor(x + 3, y + 9, 16, 12);
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x + 3, y + 5);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x + 3, y + 5, null);
                endGlScissor();
            } else if (mouseX >= x && mouseY >= y && mouseX < x + 27 && mouseY < y + 24) {
                drawModalRectWithCustomSizedTexture(x, y, 319, 77, 27, 24, 512.0F, 512.0F);
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x + 5, y + 3);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x + 5, y + 3, null);
            } else {
                drawModalRectWithCustomSizedTexture(x, y, 292, 77, 27, 24, 512.0F, 512.0F);
                RenderHelper.enableGUIStandardItemLighting();
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x + 5, y + 5);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x + 5, y + 5, null);
            }
            mc.getRenderItem().zLevel = 0.0F;
            RenderHelper.disableStandardItemLighting();
            if (mouseX >= x && mouseY >= y && mouseX < x + 27 && mouseY < y + 24) {
                hover = category.getName();
            }
        }

        if (bookWiki.getPagesFromCategory(currentCategory).length > 1) {
            mc.getTextureManager().bindTexture(texture);
            GlStateManager.color(1.0F, 1.0F, 1.0f, 1.0F);
            int x = width / 2 - 292 / 2;
            int y = height / 2 - 180 / 2;
            int pageNumber = bookWiki.getPageNumber(currentCategory, currentPage);
            if (pageNumber != 0) {
                if (mouseX >= x + 18 && mouseY >= y + 158 && mouseX < x + 18 + 18 && mouseY < y + 158 + 10) {
                    drawModalRectWithCustomSizedTexture(x + 18, y + 158, 292 + 23, 13, 18, 10, 512.0F, 512.0F);
                } else {
                    drawModalRectWithCustomSizedTexture(x + 18, y + 158, 292, 13, 18, 10, 512.0F, 512.0F);
                }
            }
            if (pageNumber < bookWiki.getPagesFromCategory(currentCategory).length - 1) {
                if (mouseX >= x + 248 && mouseY >= y + 158 && mouseX < x + 248 + 18 && mouseY < y + 158 + 10) {
                    drawModalRectWithCustomSizedTexture(x + 248, y + 158, 292 + 23, 0, 18, 10, 512.0F, 512.0F);
                } else {
                    drawModalRectWithCustomSizedTexture(x + 248, y + 158, 292, 0, 18, 10, 512.0F, 512.0F);
                }
            }
        }

        List<String> lines = Lists.newArrayList(splitIntoLines(getFormattedContent(currentPage), 120));
        Map<IComponent, List<Tuple<String, BlockPos>>> componentMap = Maps.newHashMap();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = width / 2 - 292 / 2 + 16;
            int y = height / 2 - 180 / 2 + 14 + fontRendererObj.FONT_HEIGHT * i;
            if (i >= 16) {
                x = width / 2 - 292 / 2 + 16 + 140;
                y = height / 2 - 180 / 2 + 14 + fontRendererObj.FONT_HEIGHT * (i - 16);
            }
            for (IComponent component : BookWikiAPI.getComponents()) {
                Matcher matcher = Pattern.compile("<" + component.getID() + ":[a-zA-Z0-9.:*_-]*>").matcher(line);
                while (matcher.find()) {
                    String group = matcher.group();
                    String arg = group.substring(3, group.length() - 1);
                    List<Tuple<String, BlockPos>> componentsForType = componentMap.get(component);
                    if (componentsForType == null) {
                        componentsForType = new ArrayList<Tuple<String, BlockPos>>();
                    }
                    componentsForType.add(new Tuple<String, BlockPos>(arg, new BlockPos(x, y, 0)));
                    componentMap.put(component, componentsForType);
                    line = line.replace(group, "");
                }
            }
            GlStateManager.disableLighting();
            fontRendererObj.drawString(line, x, y, 0x000);
        }

        for (Map.Entry<IComponent, List<Tuple<String, BlockPos>>> entry : componentMap.entrySet()) {
            List<Tuple<String, BlockPos>> tuples = entry.getValue();
            IComponent component = entry.getKey();
            for (Tuple<String, BlockPos> tuple : tuples) {
                BlockPos blockPos = tuple.getSecond();
                component.render(mc, bookWiki, tuple.getFirst(), this, blockPos.getX(), blockPos.getY(), mouseX, mouseY);
            }
        }

        for (Map.Entry<IComponent, List<Tuple<String, BlockPos>>> entry : componentMap.entrySet()) {
            List<Tuple<String, BlockPos>> tuples = entry.getValue();
            IComponent component = entry.getKey();
            for (Tuple<String, BlockPos> tuple : tuples) {
                BlockPos blockPos = tuple.getSecond();
                component.renderTooltip(mc, bookWiki, tuple.getFirst(), this, blockPos.getX(), blockPos.getY(), mouseX, mouseY);
            }
        }

        if (hover != null) {
            drawCreativeTabHoveringText(StatCollector.translateToLocal(hover), mouseX, mouseY);
        }
    }

    public List<String> splitIntoLines(String text, int maxLength) {
        maxLength += fontRendererObj.getCharWidth(' ');
        List<String> lines = new ArrayList<String>();
        String[] originalLines = text.split("\n");
        for (String originalLine : originalLines) {
            String[] words = originalLine.split(" ");
            int wordIndex = 0;
            while (wordIndex < words.length) {
                int currentLineLength = 0;
                String currentLine = "";
                while (currentLineLength < maxLength && wordIndex < words.length) {
                    String wordString = words[wordIndex] + " ";
                    int length = fontRendererObj.getStringWidth(wordString);
                    if (length >= maxLength) {
                        if (currentLineLength == 0) {
                            currentLine = wordString;
                        }
                        wordIndex++;
                        break;
                    } else {
                        currentLineLength += length;
                        if (currentLineLength < maxLength && wordIndex < words.length) {
                            currentLine += wordString;
                            wordIndex++;
                        }
                    }
                }
                lines.add(currentLine);
            }
        }
        return lines;
    }

    public String getFormattedContent(BookWikiContainer.Page page) {
        String result = page.getContent();
        for (IComponent component : BookWikiAPI.getComponents()) {
            Matcher matcher = Pattern.compile("<" + component.getID() + ":[a-zA-Z0-9.:*_-]*>").matcher(result);
            while (matcher.find()) {
                String group = matcher.group();
                String arg = group.substring(3, group.length() - 1);
                result = component.init(result, arg, group, bookWiki);
            }
        }
        return result;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i = 0; i < bookWiki.getContainer().getCategories().length; i++) {
                BookWikiContainer.Category category = bookWiki.getContainer().getCategories()[i];
                if (currentCategory == category) {
                    continue;
                }
                int x = width / 2 - 292 / 2 + 14 + 27 * i;
                int y = height / 2 - 180 / 2 - 24 + 7;
                if (mouseX >= x && mouseY >= y && mouseX < x + 27 && mouseY < y + 24) {
                    currentCategory = category;
                    currentPage = category.getDefaultPage();
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("llibrary", "page-flip"), 1.0F));
                }
            }
            if (bookWiki.getPagesFromCategory(currentCategory).length > 1) {
                int x = width / 2 - 292 / 2;
                int y = height / 2 - 180 / 2;
                int pageNumber = bookWiki.getPageNumber(currentCategory, currentPage);
                if (pageNumber != 0 && mouseX >= x + 18 && mouseY >= y + 158 && mouseX < x + 18 + 18 && mouseY < y + 158 + 10) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("llibrary", "page-flip"), 1.0F));
                    currentPage = bookWiki.getPagesFromCategory(currentCategory)[pageNumber - 1];
                } else if (pageNumber < bookWiki.getPagesFromCategory(currentCategory).length - 1 && mouseX >= x + 248 && mouseY >= y + 158 && mouseX < x + 248 + 18 && mouseY < y + 158 + 10) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("llibrary", "page-flip"), 1.0F));
                    currentPage = bookWiki.getPagesFromCategory(currentCategory)[pageNumber + 1];
                }
            }
        }
    }

    public void startGlScissor(int x, int y, int width, int height) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        double scaleW = (double) mc.displayWidth / scaledResolution.getScaledWidth_double();
        double scaleH = (double) mc.displayHeight / scaledResolution.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH)), (int) Math.floor((double) (x + width) * scaleW) - (int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) y * scaleH)) - (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH)));
    }

    public void endGlScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        int pageNumber = bookWiki.getPageNumber(currentCategory, currentPage);
        if (keyCode == Keyboard.KEY_LEFT && pageNumber != 0) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("llibrary", "page-flip"), 1.0F));
            currentPage = bookWiki.getPagesFromCategory(currentCategory)[pageNumber - 1];
        } else if (keyCode == Keyboard.KEY_RIGHT && pageNumber < bookWiki.getPagesFromCategory(currentCategory).length - 1) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("llibrary", "page-flip"), 1.0F));
            currentPage = bookWiki.getPagesFromCategory(currentCategory)[pageNumber + 1];
        }
        super.keyTyped(typedChar, keyCode);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void renderToolTip(Minecraft mc, ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            } else {
                list.set(i, EnumChatFormatting.GRAY + list.get(i));
            }
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int i = 0;

        for (String s : list) {
            int j = mc.fontRendererObj.getStringWidth(s);
            if (j > i) {
                i = j;
            }
        }

        int l1 = x + 12;
        int i2 = y - 12;
        int k = 8;

        if (list.size() > 1) {
            k += 2 + (list.size() - 1) * 10;
        }

        zLevel = 300.0F;
        int l = -267386864;
        drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
        drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
        drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
        drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
        drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
        drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
        drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
        zLevel = 0.0F;

        for (int k1 = 0; k1 < list.size(); ++k1) {
            String s1 = list.get(k1);
            mc.fontRendererObj.drawStringWithShadow(s1, (float) l1, (float) i2, -1);
            if (k1 == 0) {
                i2 += 2;
            }
            i2 += 10;
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

    public void drawHoveringText(String text, int x, int y, FontRenderer font) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int i = font.getStringWidth(text);

        int l1 = x + 12;
        int i2 = y - 12;
        int k = 8;

        zLevel = 300.0F;
        int l = -267386864;
        this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
        this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
        this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
        this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
        this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
        this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
        zLevel = 0.0F;

        font.drawStringWithShadow(text, (float) l1, (float) i2, -1);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}
